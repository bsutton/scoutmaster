package au.org.scoutmaster.views.wizards.raffle.allocateBooks;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import net.sf.jasperreports.engine.JRException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import au.com.vaadinutils.crud.EntityTable;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.MultiColumnFormLayout;
import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.dao.Path;
import au.com.vaadinutils.jasper.JasperManager;
import au.com.vaadinutils.jasper.PrintWindow;
import au.com.vaadinutils.listener.ClickEventLogged;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.RaffleAllocationDao;
import au.org.scoutmaster.dao.RaffleBookDao;
import au.org.scoutmaster.dao.Transaction;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Organisation;
import au.org.scoutmaster.domain.Raffle;
import au.org.scoutmaster.domain.RaffleAllocation;
import au.org.scoutmaster.domain.RaffleAllocation_;
import au.org.scoutmaster.domain.RaffleBook;
import au.org.scoutmaster.domain.RaffleBook_;
import au.org.scoutmaster.jasper.JasperSettingsImpl;
import au.org.scoutmaster.util.SMNotification;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class ManualAllocationStep implements WizardStep, ClickListener, AllocationStep
{
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(ManualAllocationStep.class);
	private RaffleBookAllocationWizardView wizard;
	private RaffleAllocation raffleAllocation;

	public ManualAllocationStep(RaffleBookAllocationWizardView setupWizardView)
	{
		this.wizard = setupWizardView;
	}

	@Override
	public String getCaption()
	{
		return "Books Allocated";
	}

	@Override
	public Component getContent()
	{
		Raffle raffle = wizard.getRaffle();

		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		MultiColumnFormLayout<Organisation> formLayout = new MultiColumnFormLayout<>(1, null);
		formLayout.setColumnLabelWidth(0, 150);
		formLayout.setColumnFieldWidth(0, 250);
		formLayout.setSizeFull();

		Label label = new Label("<h1>The following books have been allocated.</h1>", ContentMode.HTML);

		layout.addComponent(label);
		layout.addComponent(formLayout);

		Contact issuedByContact = wizard.getSelectStep().getIssuedByContact();
		List<Allocation> allocations = wizard.getSelectStep().getAllocations();

		allocateBooks(raffle, issuedByContact, allocations);

		JPAContainer<RaffleBook> container = new DaoFactory().getRaffleBookDao().createVaadinContainer();
		container.addContainerFilter(new Compare.Equal(new Path(RaffleBook_.raffleAllocation, RaffleAllocation_.id)
				.getName(), this.raffleAllocation.getId()));
		container.sort(new Object[]
		{ RaffleBook_.firstNo.getName() }, new boolean[]
		{ true });

		Builder<RaffleBook> builder = new HeadingPropertySet.Builder<>();
		builder.addColumn("First Ticket No.", RaffleBook_.firstNo);
		builder.addColumn("Allocated To",
				new Path(RaffleBook_.raffleAllocation, RaffleAllocation_.allocatedTo).getName());
		builder.addColumn("Issued By", new Path(RaffleBook_.raffleAllocation, RaffleAllocation_.issuedBy).getName());
		builder.addColumn("Date Issued", new Path(RaffleBook_.raffleAllocation, RaffleAllocation_.dateIssued).getName());

		EntityTable<RaffleBook> allocatedBooks = new EntityTable<RaffleBook>(container, builder.build());
		allocatedBooks.init();

		allocatedBooks.setSortEnabled(true);
		allocatedBooks.setColumnCollapsingAllowed(true);

		layout.addComponent(allocatedBooks);

		Label labelPrint = new Label(
				"<p></p><h3>You can now print an 'Allocation Acknowledgement Form' for the Member to sign acknowledging that they have recieved the tickets.</h3>"
						+ "You should print two copies, one for the parent and the second should be signed by the Parent and retained by Group.",
				ContentMode.HTML);

		layout.addComponent(labelPrint);

		Button printButton = new Button("Print Preview");
		printButton.addClickListener(new ClickEventLogged.ClickAdaptor(this));
		layout.addComponent(printButton);
		layout.setComponentAlignment(printButton, Alignment.BOTTOM_RIGHT);

		return layout;
	}

	// private void allocateBooks(Raffle raffle, Contact issuedByContact,
	// List<Allocation> allocations)
	// {
	// RaffleBookDao daoRaffleBook = new DaoFactory().getRaffleBookDao();
	// RaffleAllocationDao daoRaffleAllocation = new
	// DaoFactory().getRaffleAllocationDao();
	//
	// assert allocations.size() == 0 : "Only a single allocation is expected.";
	//
	// Allocation allocation = allocations.get(0);
	// RaffleAllocation raffleAllocation = new RaffleAllocation();
	// raffleAllocation.setRaffle(raffle);
	// raffleAllocation.setAllocatedTo(allocation.getAllocatedTo());
	// raffleAllocation.setDateAllocated(new Date(new
	// java.util.Date().getTime()));
	// raffleAllocation.setDateIssued(new Date(new java.util.Date().getTime()));
	// raffleAllocation.setIssuedBy(issuedByContact);
	// daoRaffleAllocation.persist(raffleAllocation);
	//
	// for (RaffleBook book : allocation.getBooks())
	// {
	// if (book.getRaffleAllocation() == null)
	// {
	// book.setRaffleAllocation(raffleAllocation);
	// daoRaffleBook.merge(book);
	// }
	// }
	// this.raffleAllocation = raffleAllocation;
	// }

	private void allocateBooks(Raffle raffle, Contact issuedByContact, List<Allocation> preallocation)
	{
		RaffleBookDao daoRaffleBook = new DaoFactory().getRaffleBookDao();

		Preconditions.checkArgument(preallocation.size() == 1, "Only one Allocations should have been made");

		EntityManager em = EntityManagerProvider.createEntityManager();
		RaffleAllocationDao daoLocalRaffleAllocation = new DaoFactory(em).getRaffleAllocationDao();
		RaffleBookDao daoLocalRaffleBook = new DaoFactory(em).getRaffleBookDao();

		Allocation allocation = preallocation.get(0);
		// Placed this in its own transaction as I need the allocation id
		// which is only available after we commit.
		try (Transaction t = new Transaction(em))
		{
			List<RaffleBook> books = new ArrayList<>();
			// Move the books from the request em to our local em.
			for (RaffleBook book : allocation.getBooks())
			{
				daoRaffleBook.detach(book);
				books.add(daoLocalRaffleBook.merge(book));
			}

			RaffleAllocation raffleAllocation = new RaffleAllocation();
			raffleAllocation.setRaffle(raffle);
			raffleAllocation.setAllocatedTo(allocation.getAllocatedTo());
			raffleAllocation.setDateAllocated(new Date(new java.util.Date().getTime()));
			raffleAllocation.setDateIssued(new Date(new java.util.Date().getTime()));
			raffleAllocation.setIssuedBy(issuedByContact);
			daoLocalRaffleAllocation.persist(raffleAllocation);

			for (RaffleBook book : books)
			{
				if (book.getRaffleAllocation() == null)
				{
					book.setRaffleAllocation(raffleAllocation);
					book = daoLocalRaffleBook.merge(book);
					raffleAllocation.addBook(book);
				}
			}
			raffleAllocation = daoLocalRaffleAllocation.merge(raffleAllocation);
			this.raffleAllocation = raffleAllocation;
			t.commit();
			daoLocalRaffleAllocation.detach(raffleAllocation);
		}
		finally
		{

		}
	}

	@Override
	public boolean onAdvance()
	{
		return true;
	}

	@Override
	public boolean onBack()
	{
		return false;
	}

	@Override
	public void buttonClick(ClickEvent event)
	{
		JasperManager manager;
		try
		{
			manager = new JasperManager(EntityManagerProvider.getEntityManager(), "RaffleAllocation.jasper",
					new JasperSettingsImpl());
			manager.bindParameter("allocationIds", this.raffleAllocation.getId());
			PrintWindow window = new PrintWindow(manager);
			UI.getCurrent().addWindow(window);
		}
		catch (JRException e)
		{
			SMNotification.show(e.getMessage(), Type.ERROR_MESSAGE);
		}

	}

}
