package au.org.scoutmaster.views.wizards.raffle.allocateBooks;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Or;
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
import au.org.scoutmaster.jasper.SMJasperReportProperties;

public class BulkAllocationStep implements WizardStep, ClickListener, AllocationStep
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger();
	private final RaffleBookAllocationWizardView wizard;

	private final List<RaffleAllocation> raffleAllocations = new ArrayList<>();

	public BulkAllocationStep(final RaffleBookAllocationWizardView setupWizardView)
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

		final Label label = new Label("<h1>The following Allocations have been made.</h1>", ContentMode.HTML);

		layout.addComponent(label);
		layout.addComponent(formLayout);

		final Contact issuedByContact = this.wizard.getSelectStep().getIssuedByContact();

		allocateBooks(raffle, issuedByContact, this.wizard.getSelectStep().getAllocations());

		final JPAContainer<RaffleAllocation> container = new DaoFactory().getRaffleAllocationDao()
				.createVaadinContainer();

		final List<Filter> filters = new ArrayList<>();
		// build filter
		for (final RaffleAllocation allocation : this.raffleAllocations)
		{
			final Filter filter = new Compare.Equal(BaseEntity_.id.getName(), allocation.getId());
			filters.add(filter);
		}
		container.addContainerFilter(new Or(filters.toArray(new Filter[]
		{})));
		final Builder<RaffleAllocation> builder = new HeadingPropertySet.Builder<>();

		builder.addColumn("Allocated To", RaffleAllocation_.allocatedTo);
		builder.addColumn("Issued By", RaffleAllocation_.issuedBy);
		builder.addColumn("Date Allocated", RaffleAllocation_.dateAllocated);
		builder.addColumn("Book Count.", "bookCount");

		final EntityTable<RaffleAllocation> allocatedBooks = new EntityTable<RaffleAllocation>(container,
				builder.build());
		allocatedBooks.setWidth("100%");
		allocatedBooks.init("BulkAllocationStep");

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

	/**
	 * Man what a pain this method was to wwrite. I couldn't get the id on
	 * raffleallocation to return after the persist even after calling flush
	 * which all of the doco says should work. The eventual trick was to merge
	 * the allocation after the persist which then had the correct id.
	 *
	 * @param raffle
	 * @param issuedByContact
	 * @param preallocation
	 */
	private void allocateBooks(final Raffle raffle, final Contact issuedByContact, final List<Allocation> preallocation)
	{
		final RaffleBookDao daoRaffleBook = new DaoFactory().getRaffleBookDao();

		final RaffleAllocationDao daoLocalRaffleAllocation = new DaoFactory().getRaffleAllocationDao();
		final RaffleBookDao daoLocalRaffleBook = new DaoFactory().getRaffleBookDao();

		for (final Allocation allocation : preallocation)
		{
			// Placed this in its own transaction as I need the allocation id
			// which is only available after we commit.
			try (Transaction t = new Transaction(EntityManagerProvider.getEntityManager()))
			{
				final List<RaffleBook> books = new ArrayList<>();
				// Move the books from the request em to our local em.
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
				this.raffleAllocations.add(raffleAllocation);
				t.commit();
				daoLocalRaffleAllocation.detach(raffleAllocation);
			}
			finally
			{

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

	@Override
	public void buttonClick(final ClickEvent event)
	{
		final StringBuilder ids = new StringBuilder();

		for (final RaffleAllocation allocation : this.raffleAllocations)
		{
			if (ids.length() > 0)
			{
				ids.append(",");
			}
			ids.append(allocation.getId());
		}

		final ReportFilterUIBuilder builder = new ReportFilterUIBuilder();
		builder.addField(new ReportParameterConstant<String>("allocationIds", ids.toString()));

		final SMJasperReportProperties properties = new SMJasperReportProperties(
				ScoutmasterViewEnum.RaffleBookAllocationWizard)
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
