package au.org.scoutmaster.views.wizards.raffle.importBooks;

import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import au.com.vaadinutils.crud.FormHelper;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.dao.JpaBaseDao;
import au.com.vaadinutils.ui.SingleEntityWizardStep;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.RaffleDao;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.Raffle;
import au.org.scoutmaster.domain.Raffle_;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;
import au.org.scoutmaster.util.SMNotification;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;

public class SelectRaffleStep extends SingleEntityWizardStep<Raffle> implements ValueChangeListener
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(SelectRaffleStep.class);

	private final RaffleBookImportWizardView setupWizardView;

	private ComboBox raffleField;

	private Raffle newRaffle = null;

	private VerticalLayout layout;

	private OptionGroup options;

	private AbstractLayout existingLayout;

	private AbstractLayout newLayout;

	public SelectRaffleStep(final RaffleBookImportWizardView setupWizardView)
	{
		super(DaoFactory.getGenericDao(Raffle.class), Raffle.class);
		this.setupWizardView = setupWizardView;
	}

	@Override
	public String getCaption()
	{
		return "Select Raffle";
	}

	enum RaffleType
	{
		New("New Raffle"), Existing("Existing Raffle");

		private String description;

		RaffleType(final String description)
		{
			this.description = description;
		}

		@Override
		public String toString()
		{
			return this.description;
		}

	}

	@Override
	public Component getContent(final ValidatingFieldGroup<Raffle> fieldGroup)
	{
		if (this.layout == null)
		{
			this.layout = new VerticalLayout();

			this.layout.setMargin(true);
			final Label label = new Label("<h1>Select Raffle to Import Books into.</h1>", ContentMode.HTML);
			label.setContentMode(ContentMode.HTML);

			this.layout.addComponent(label);

			this.options = new OptionGroup();
			this.options.addItem(RaffleType.New);
			this.options.addItem(RaffleType.Existing);
			this.options.setImmediate(true);
			this.options.addValueChangeListener(this);
			this.layout.addComponent(this.options);

			Raffle raffle = null;
			final RaffleDao daoRaffle = new DaoFactory().getRaffleDao();
			@SuppressWarnings("unchecked")
			final List<Raffle> list = daoRaffle.findAll(new SingularAttribute[]
					{ Raffle_.startDate }, new boolean[]
							{ false });
			if (list.size() > 0)
			{
				raffle = list.get(0);
				final DateTime created = new DateTime(raffle.getCreated());
				// If the raffle is older than six months we assume they will
				// be wanting to create a new raffle.
				if (created.isBefore(new DateTime().minusMonths(6)))
				{
					raffle = null;
				}
			}

			this.existingLayout = buildExistingRaffleLayout(raffle);
			this.newLayout = buildNewRaffleLayout(fieldGroup);
			this.layout.addComponent(this.existingLayout);
			this.layout.addComponent(this.newLayout);

			if (raffle == null)
			{
				this.options.setValue(RaffleType.New);
			}
			else
			{
				this.options.setValue(RaffleType.Existing);
			}

		}

		return this.layout;
	}

	private AbstractLayout buildExistingRaffleLayout(final Raffle raffle)
	{
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		final RaffleDao daoRaffle = new DaoFactory().getRaffleDao();
		this.raffleField = new ComboBox();
		this.raffleField.setContainerDataSource(daoRaffle.createVaadinContainer());
		// raffleField.setConverter(Raffle.class);
		this.raffleField.setItemCaptionPropertyId(Raffle_.name.getName());
		this.raffleField.setRequired(true);
		this.raffleField.setNullSelectionAllowed(false);

		this.raffleField.setValue(raffle.getId());

		layout.addComponent(this.raffleField);
		return layout;

	}

	AbstractLayout buildNewRaffleLayout(final ValidatingFieldGroup<Raffle> fieldGroup)
	{
		final SMMultiColumnFormLayout<Raffle> overviewForm = new SMMultiColumnFormLayout<Raffle>(1, fieldGroup);
		overviewForm.setColumnFieldWidth(0, 300);
		overviewForm.setSizeFull();

		overviewForm.bindTextField("Name", Raffle_.name);
		overviewForm.bindTextAreaField("Notes", Raffle_.notes, 6);
		overviewForm.bindDateField("Start Date", Raffle_.startDate, "yyyy/MM/dd", Resolution.DAY);
		overviewForm.bindDateField("Collect By Date", Raffle_.collectionsDate, "yyyy/MM/dd", Resolution.DAY)
		.setDescription("The date the raffle ticksets need to be collected by.");
		overviewForm.bindDateField("Return Date", Raffle_.returnDate, "yyyy/MM/dd", Resolution.DAY).setDescription(
				"The date the raffle ticksets need to be returned to Branch.");

		final FormHelper<Raffle> formHelper = overviewForm.getFormHelper();
		final ComboBox groupRaffleManager = formHelper.new EntityFieldBuilder<Contact>()
				.setLabel("Group Raffle Manager").setField(Raffle_.groupRaffleManager)
				.setListFieldName(Contact_.fullname).build();
		groupRaffleManager.setFilteringMode(FilteringMode.CONTAINS);
		groupRaffleManager.setTextInputAllowed(true);
		groupRaffleManager.setDescription("The Group member responsible for organising the Raffle.");

		final ComboBox branchRaffleConact = formHelper.new EntityFieldBuilder<Contact>()
				.setLabel("Branch Raffle Contact").setField(Raffle_.branchRaffleContact)
				.setListFieldName(Contact_.fullname).build();
		branchRaffleConact.setFilteringMode(FilteringMode.CONTAINS);
		branchRaffleConact.setTextInputAllowed(true);
		branchRaffleConact.setDescription("The Branch person who is a main contact for Raffle issues.");

		overviewForm.bindTextField("Book No. Prefix", Raffle_.raffleNoPrefix).setDescription(
				"If raffle books have a non-numeric prefix for the ticket no's enter it here.");
		overviewForm.bindTextField("Tickets per Book", Raffle_.ticketsPerBook);
		overviewForm.bindTextField("Total Tickets Sold", Raffle_.totalTicketsSold).setReadOnly(true);
		overviewForm.bindTextField("Tickets Outstanding", Raffle_.ticketsOutstanding).setReadOnly(true);
		overviewForm.bindTextField("Sales Price per Ticket", Raffle_.salePricePerTicket).setDescription(
				"The amount each ticket is to be sold for.");
		overviewForm.bindTextField("Revenue Target", Raffle_.revenueTarget).setDescription(
				"The amount the Group is aiming to raise via the Raffle.");

		overviewForm.bindTextField("Revenue Raised", Raffle_.revenueRaised).setReadOnly(true);

		return overviewForm;

	}

	@Override
	public void valueChange(final ValueChangeEvent event)
	{
		final RaffleType method = (RaffleType) this.options.getValue();
		switch (method)
		{
			case New:
				this.existingLayout.setVisible(false);
				this.newLayout.setVisible(true);
				break;
			case Existing:
				this.existingLayout.setVisible(true);
				this.newLayout.setVisible(false);
				break;
		}

	}

	@Override
	public boolean onAdvance()
	{
		boolean advance = false;
		if (this.options.getValue() == RaffleType.Existing)
		{
			if (this.raffleField.isValid())
			{
				final Long id = (Long) this.raffleField.getValue();
				final JpaBaseDao<Raffle, Long> raffleDao = DaoFactory.getGenericDao(Raffle.class);
				final Raffle raffle = raffleDao.findById(id);
				this.setupWizardView.setRaffle(raffle);
				advance = true;
			}
			else
			{
				SMNotification.show("Please select a Raffle", Type.WARNING_MESSAGE);
			}
		}
		else
		{
			// call super to create the new raffle
			advance = super.onAdvance();
			this.newRaffle = super.getEntity();

			this.setupWizardView.setRaffle(this.newRaffle);
		}

		return advance;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

	@Override
	protected void initEntity(final Raffle entity)
	{
		// nothing to initialise for a new raffle.
	}

	@Override
	protected Raffle findEntity()
	{
		// If we create a raffle we need to return it when someone clicks
		// back so we don't keep adding new raffles.
		return this.newRaffle;
	}

}
