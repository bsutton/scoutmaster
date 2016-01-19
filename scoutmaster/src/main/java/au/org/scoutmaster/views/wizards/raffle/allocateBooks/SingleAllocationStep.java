package au.org.scoutmaster.views.wizards.raffle.allocateBooks;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

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
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import au.com.vaadinutils.crud.EntityTable;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.MultiColumnFormLayout;
import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.dao.Path;
import au.com.vaadinutils.jasper.filter.ReportFilterUIBuilder;
import au.com.vaadinutils.jasper.parameter.ReportParameterConstant;
import au.com.vaadinutils.jasper.ui.JasperReportPopUp;
import au.com.vaadinutils.listener.ClickEventLogged;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.application.ScoutmasterViewEnum;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.RaffleAllocationDao;
import au.org.scoutmaster.dao.RaffleBookDao;
import au.org.scoutmaster.dao.Transaction;
import au.org.scoutmaster.domain.BaseEntity_;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Organisation;
import au.org.scoutmaster.domain.Raffle;
import au.org.scoutmaster.domain.RaffleAllocation;
import au.org.scoutmaster.domain.RaffleAllocation_;
import au.org.scoutmaster.domain.RaffleBook;
import au.org.scoutmaster.domain.RaffleBook_;
import au.org.scoutmaster.jasper.SMJasperReportProperties;

public class SingleAllocationStep implements WizardStep, ClickListener, AllocationStep
{
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(SingleAllocationStep.class);
	private final RaffleBookAllocationWizardView wizard;
	private RaffleAllocation raffleAllocation;

	public SingleAllocationStep(final RaffleBookAllocationWizardView setupWizardView)
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
		final Raffle raffle = this.wizard.getRaffle();

		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		final MultiColumnFormLayout<Organisation> formLayout = new MultiColumnFormLayout<>(1, null);
		formLayout.setColumnLabelWidth(0, 150);
		formLayout.setColumnFieldWidth(0, 250);
		formLayout.setSizeFull();

		final Label label = new Label("<h1>The following books have been allocated.</h1>", ContentMode.HTML);

		layout.addComponent(label);
		layout.addComponent(formLayout);

		final Contact issuedByContact = this.wizard.getSelectStep().getIssuedByContact();
		final List<Allocation> allocations = this.wizard.getSelectStep().getAllocations();

		allocateBooks(raffle, issuedByContact, allocations);

		final JPAContainer<RaffleBook> container = new DaoFactory().getRaffleBookDao().createVaadinContainer();
		container.addContainerFilter(new Compare.Equal(new Path(RaffleBook_.raffleAllocation, BaseEntity_.id).getName(),
				this.raffleAllocation.getId()));
		container.sort(new Object[]
		{ RaffleBook_.firstNo.getName() }, new boolean[]
		{ true });

		final Builder<RaffleBook> builder = new HeadingPropertySet.Builder<>();
		builder.addColumn("First Ticket No.", RaffleBook_.firstNo);
		builder.addColumn("Allocated To",
				new Path(RaffleBook_.raffleAllocation, RaffleAllocation_.allocatedTo).getName());
		builder.addColumn("Issued By", new Path(RaffleBook_.raffleAllocation, RaffleAllocation_.issuedBy).getName());
		builder.addColumn("Date Issued",
				new Path(RaffleBook_.raffleAllocation, RaffleAllocation_.dateIssued).getName());

		final EntityTable<RaffleBook> allocatedBooks = new EntityTable<RaffleBook>(container, builder.build());
		allocatedBooks.init("SingleAllocationStep");

		allocatedBooks.setSortEnabled(true);
		allocatedBooks.setColumnCollapsingAllowed(true);

		layout.addComponent(allocatedBooks);

		final Label labelPrint = new Label(
				"<p></p><h3>You can now print an 'Allocation Acknowledgement Form' for the Member to sign acknowledging that they have recieved the tickets.</h3>"
						+ "You should print two copies, one for the parent and the second should be signed by the Parent and retained by Group.",
				ContentMode.HTML);

		layout.addComponent(labelPrint);

		final Button printButton = new Button("Print Preview");
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
	// Preconditions.checkArgument(allocations.size() == 1,
	// "Only one Allocations should have been made");
	//
	//
	// Allocation allocation = allocations.get(0);
	//
	// raffleAllocation = new RaffleAllocation();
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
	// book.setRaffleAllocation(raffleAllocation);
	// daoRaffleBook.merge(book);
	//
	// }
	// }

	private void allocateBooks(final Raffle raffle, final Contact issuedByContact, final List<Allocation> preallocation)
	{
		final RaffleBookDao daoRaffleBook = new DaoFactory().getRaffleBookDao();

		Preconditions.checkArgument(preallocation.size() == 1, "Only one Allocations should have been made");

		final RaffleAllocationDao daoLocalRaffleAllocation = new DaoFactory().getRaffleAllocationDao();
		final RaffleBookDao daoLocalRaffleBook = new DaoFactory().getRaffleBookDao();

		final Allocation allocation = preallocation.get(0);
		// Placed this in its own transaction as I need the allocation id
		// which is only available after we commit.
		try (Transaction t = new Transaction(EntityManagerProvider.getEntityManager()))
		{
			final List<RaffleBook> books = new ArrayList<>();
			// Move the books from the request em to our local em.
			// Not certain this is still needed as moved to using the thread
			// local em.
			for (final RaffleBook book : allocation.getBooks())
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
	public void buttonClick(final ClickEvent event)
	{
		final ReportFilterUIBuilder builder = new ReportFilterUIBuilder();
		builder.addField(new ReportParameterConstant<Long>("allocationIds", this.raffleAllocation.getId()));

		final SMJasperReportProperties properties = new SMJasperReportProperties(
				ScoutmasterViewEnum.RaffleAllocationsReport)
		{
			@Override
			public String getReportTitle()
			{
				return "Raffle Allocation";
			}

			@Override
			public String getReportFileName()
			{
				return "RaffleAllocation.jasper";
			}

			@Override
			public ReportFilterUIBuilder getFilterBuilder()
			{
				ReportFilterUIBuilder builder = new ReportFilterUIBuilder();

				ReportParameterConstant<String> param = new ReportParameterConstant<String>("group_id",
						"" + SMSession.INSTANCE.getGroup().getId());
				builder.getReportParameters().add(param);

				return builder;
			}
		};

		final JasperReportPopUp window = new JasperReportPopUp(properties);
		UI.getCurrent().addWindow(window);
	}

}
