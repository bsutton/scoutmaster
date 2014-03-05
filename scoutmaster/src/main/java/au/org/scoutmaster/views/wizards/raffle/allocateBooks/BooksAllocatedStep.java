package au.org.scoutmaster.views.wizards.raffle.allocateBooks;

import java.sql.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import au.com.vaadinutils.crud.EntityTable;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.MultiColumnFormLayout;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.Path;
import au.org.scoutmaster.dao.RaffleAllocationDao;
import au.org.scoutmaster.dao.RaffleBookDao;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Organisation;
import au.org.scoutmaster.domain.Raffle;
import au.org.scoutmaster.domain.RaffleAllocation;
import au.org.scoutmaster.domain.RaffleAllocation_;
import au.org.scoutmaster.domain.RaffleBook;
import au.org.scoutmaster.domain.RaffleBook_;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class BooksAllocatedStep implements WizardStep
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(BooksAllocatedStep.class);
	private RaffleBookAllocationWizardView wizard;

	public BooksAllocatedStep(RaffleBookAllocationWizardView setupWizardView)
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

		Contact allocatedContact = wizard.getSelectContactStep().getAllocatedContact();
		int bookCount = wizard.getSelectContactStep().getBookCount();
		Contact issuedByContact = wizard.getSelectContactStep().getIssuedByContact();

		allocateBooks(raffle, allocatedContact, bookCount, issuedByContact);

		JPAContainer<RaffleBook> container = new DaoFactory().getRaffleBookDao().createVaadinContainer();
		container.addContainerFilter(new Compare.Equal("allocatedTo", allocatedContact));
		container.sort(new Object[]
		{ RaffleBook_.firstNo.getName() }, new boolean[]
		{ true });

		Builder<RaffleBook> builder = new HeadingPropertySet.Builder<>();
		builder.addColumn("First Ticket No.", RaffleBook_.firstNo);
		builder.addColumn("Allocated To", new Path(RaffleBook_.raffleAllocation, RaffleAllocation_.allocatedTo).getName());
		builder.addColumn("Issued By", new Path(RaffleBook_.raffleAllocation, RaffleAllocation_.issuedBy).getName());
		builder.addColumn("Date Issued",new Path(RaffleBook_.raffleAllocation, RaffleAllocation_.dateIssued).getName());
		
		EntityTable<RaffleBook> allocatedBooks = new EntityTable<RaffleBook>(container, builder.build());
		allocatedBooks.init();

		allocatedBooks.setSortEnabled(true);
		allocatedBooks.setColumnCollapsingAllowed(true);

		layout.addComponent(allocatedBooks);

		return layout;
	}

	private void allocateBooks(Raffle raffle, Contact allocatedTo, int bookCount, Contact issuedByContact)
	{
		RaffleBookDao daoRaffleBook = new DaoFactory().getRaffleBookDao();
		RaffleAllocationDao daoRaffleAllocation = new DaoFactory().getRaffleAllocationDao();

		List<RaffleBook> books = daoRaffleBook.findFirstUnallocated(raffle);

		RaffleAllocation allocation = new RaffleAllocation();
		allocation.setDateAllocated(new Date(new java.util.Date().getTime()));
		allocation.setDateIssued(new Date(new java.util.Date().getTime()));
		allocation.setIssuedBy(issuedByContact);
		daoRaffleAllocation.persist(allocation);
		

		
		for (RaffleBook book : books)
		{
			if (book.getRaffleAllocation() == null)
			{
				book.setRaffleAllocation(allocation);
				daoRaffleBook.merge(book);
				bookCount--;

				if (bookCount == 0)
					break;
			}
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

}
