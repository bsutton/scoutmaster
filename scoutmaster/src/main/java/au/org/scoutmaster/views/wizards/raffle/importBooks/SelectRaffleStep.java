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

	private RaffleBookImportWizardView setupWizardView;

	private ComboBox raffleField;

	private VerticalLayout layout;

	private OptionGroup options;

	private AbstractLayout existingLayout;

	private AbstractLayout newLayout;

	public SelectRaffleStep(RaffleBookImportWizardView setupWizardView)
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

		RaffleType(String description)
		{
			this.description = description;
		}

		public String toString()
		{
			return description;
		}

	}

	@Override
	public Component getContent(ValidatingFieldGroup<Raffle> fieldGroup)
	{
		if (layout == null)
		{
			layout = new VerticalLayout();

			layout.setMargin(true);
			Label label = new Label("<h1>Select Raffle to Import Books into.</h1>", ContentMode.HTML);
			label.setContentMode(ContentMode.HTML);

			layout.addComponent(label);
			
			options = new OptionGroup();
			options.addItem(RaffleType.New);
			options.addItem(RaffleType.Existing);
			options.setImmediate(true);
			options.addValueChangeListener( this);
			layout.addComponent(options);

			Raffle raffle = null;
			RaffleDao daoRaffle = new DaoFactory().getRaffleDao();
			@SuppressWarnings("unchecked")
			List<Raffle> list = daoRaffle.findAll(new SingularAttribute []{Raffle_.startDate}, new boolean[] {false});
			if (list.size() > 0)
			{
				raffle = list.get(0);
				DateTime created = new DateTime(raffle.getCreated());
				// If the raffle is older than six months we assume they will
				// be wanting to create a new raffle.
				if (created.isBefore(new DateTime().minusMonths(6)))
					raffle = null;
			}


			
			existingLayout = buildExistingRaffleLayout(raffle);
			newLayout = buildNewRaffleLayout(fieldGroup);
			layout.addComponent(existingLayout);
			layout.addComponent(newLayout);
			
			if (raffle == null)
				options.setValue(RaffleType.New);
			else
				options.setValue(RaffleType.Existing);

		}

		return layout;
	}

	private AbstractLayout buildExistingRaffleLayout(Raffle raffle)
	{
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		RaffleDao daoRaffle = new DaoFactory().getRaffleDao();
		raffleField = new ComboBox();
		raffleField.setContainerDataSource(daoRaffle.createVaadinContainer());
		// raffleField.setConverter(Raffle.class);
		raffleField.setItemCaptionPropertyId(Raffle_.name.getName());
		raffleField.setRequired(true);
		raffleField.setNullSelectionAllowed(false);
		
		raffleField.setValue(raffle.getId());
		
		layout.addComponent(raffleField);
		return layout;
		
	}
	
	AbstractLayout buildNewRaffleLayout(ValidatingFieldGroup<Raffle> fieldGroup)
	{
		SMMultiColumnFormLayout<Raffle> overviewForm = new SMMultiColumnFormLayout<Raffle>(1, fieldGroup);
		overviewForm.setColumnFieldWidth(0, 300);
		overviewForm.setSizeFull();

		overviewForm.bindTextField("Name", Raffle_.name);
		 overviewForm.bindTextAreaField("Notes", Raffle_.notes, 6);
		overviewForm.bindDateField("Start Date", Raffle_.startDate, "yyyy/MM/dd", Resolution.DAY);
		overviewForm.bindDateField("Collect By Date", Raffle_.collectionsDate, "yyyy/MM/dd", Resolution.DAY)
				.setDescription("The date the raffle ticksets need to be collected by.");
		overviewForm.bindDateField("Return Date", Raffle_.returnDate, "yyyy/MM/dd", Resolution.DAY).setDescription(
				"The date the raffle ticksets need to be returned to Branch.");
		
		

		FormHelper<Raffle> formHelper = overviewForm.getFormHelper();
		ComboBox groupRaffleManager = formHelper.new EntityFieldBuilder<Contact>()
				.setLabel("Group Raffle Manager").setField(Raffle_.groupRaffleManager).setListFieldName(Contact_.fullname).build();
		groupRaffleManager.setFilteringMode(FilteringMode.CONTAINS);
		groupRaffleManager.setTextInputAllowed(true);
		groupRaffleManager.setDescription("The Group member responsible for organising the Raffle.");
		
		ComboBox branchRaffleConact = formHelper.new EntityFieldBuilder<Contact>()
				.setLabel("Branch Raffle Contact").setField(Raffle_.branchRaffleContact).setListFieldName(Contact_.fullname).build();
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
	public void valueChange(ValueChangeEvent event)
	{
		RaffleType method = (RaffleType) options.getValue();
		switch (method)
		{
			case New:
				existingLayout.setVisible(false);
				newLayout.setVisible(true);
				break;
			case Existing:
				existingLayout.setVisible(true);
				newLayout.setVisible(false);
				break;
		}

	}


	@Override
	public boolean onAdvance()
	{
		boolean advance = false;
		if (raffleField.isValid())
		{
			Long id = (Long) raffleField.getValue();
			JpaBaseDao<Raffle, Long> raffleDao = DaoFactory.getGenericDao(Raffle.class);
			Raffle raffle = raffleDao.findById(id);
			setupWizardView.setRaffle(raffle);
			advance = true;
		}
		else
			SMNotification.show("Please select a Raffle", Type.WARNING_MESSAGE);

		return advance;
	}

	public boolean onBack()
	{
		return true;
	}

	@Override
	protected void initEntity(Raffle entity)
	{
		// nothing to initialise for a new raffle.
	}

	@Override
	protected Raffle findEntity()
	{
		// We only ever create a new entity.
		return null;
	}

	

}

