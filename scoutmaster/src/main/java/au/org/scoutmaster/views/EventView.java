package au.org.scoutmaster.views;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.CrudAction;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.fields.AutoCompleteParent;
import au.com.vaadinutils.fields.CKEditorEmailField;
import au.com.vaadinutils.fields.ColorPickerField;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.EventDao;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Event;
import au.org.scoutmaster.domain.Event_;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;
import au.org.scoutmaster.validator.DateRangeValidator;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.VerticalLayout;

@Menu(display = "Events")
public class EventView extends BaseCrudView<Event> implements View, Selected<Event>, AutoCompleteParent<Contact>
{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(EventView.class);

	public static final String NAME = "Event";

	private DateField endDateField;

	private DateField startDateField;
	
	private ColorPickerField colourPickerField;

//	private AttachedDocuments attachedDocuments;

	private JPAContainer<au.org.scoutmaster.domain.Event> container;



	@Override
	public void enter(ViewChangeEvent event)
	{
		container = new DaoFactory().getDao( EventDao.class).createVaadinContainer();
		container.sort(new String[] {Event_.eventStartDateTime.getName()},new boolean[] {false});

		Builder< au.org.scoutmaster.domain.Event> builder = new HeadingPropertySet.Builder< au.org.scoutmaster.domain.Event>();
		builder.addColumn("Subject", Event_.subject)
				.addColumn("All Day", Event_.allDayEvent)
				.addColumn("Start Date", Event_.eventStartDateTime)
				.addColumn("End Date", Event_.eventEndDateTime);

		super.init( au.org.scoutmaster.domain.Event.class, container, builder.build());

	}


	@Override
	protected AbstractLayout buildEditor(ValidatingFieldGroup< au.org.scoutmaster.domain.Event> fieldGroup2)
	{
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		SMMultiColumnFormLayout< au.org.scoutmaster.domain.Event> overviewForm = new SMMultiColumnFormLayout< au.org.scoutmaster.domain.Event>(3, this.fieldGroup);
		overviewForm.setColumnLabelWidth(0, 100);
		overviewForm.setColumnLabelWidth(1, 0);
		overviewForm.setColumnLabelWidth(2, 60);
		overviewForm.setColumnFieldWidth(0, 100);
		overviewForm.setColumnFieldWidth(1, 100);
		overviewForm.setColumnFieldWidth(2, 20);
		overviewForm.setColumnExpandRatio(1, 1.0f);
		overviewForm.setSizeFull();

		overviewForm.colspan(3);
		overviewForm.bindTextField("Subject", Event_.subject);
		overviewForm.newLine();
		
		colourPickerField = overviewForm.bindColorPicker("Colour", Event_.color);
		overviewForm.newLine();
		
		overviewForm.bindBooleanField("All Day Event", Event_.allDayEvent);
		overviewForm.newLine();
		
		overviewForm.colspan(3);
		startDateField = overviewForm.bindDateField("Start", Event_.eventStartDateTime, "yyyy-MM-dd hh:mm a", Resolution.MINUTE);
		startDateField.setRangeStart(new Date());
		overviewForm.newLine();

		overviewForm.colspan(3);
		endDateField = overviewForm.bindDateField("End", Event_.eventEndDateTime, "yyyy-MM-dd hh:mm a", Resolution.MINUTE);
		endDateField.setRangeStart(new Date());
		
		startDateField.addValidator(new DateRangeValidator(startDateField.getCaption(), endDateField));
		
		overviewForm.newLine();
		
//		overviewForm.bindAutoCompleteField("Co-ordinators", Event_.coordinators, Contact.class);
		
//		ContactDao daoContact = new DaoFactory().getContactDao();
//		EntityAutoCompleteField<Contact, ContactDao> contactSelect = new EntityAutoCompleteField<Contact, ContactDao>(daoContact.createVaadinContainer(), daoContact, "Coordinators", this);
//		overviewForm.addComponent(contactSelect);
		
		overviewForm.newLine();
		overviewForm.colspan(3);
		overviewForm.bindTextField("Street", "location.street");
		overviewForm.newLine();
		overviewForm.colspan(3);
		overviewForm.bindTextField("City", "location.city");

		overviewForm.newLine();
		overviewForm.bindTextField("State", "location.state");
		overviewForm.newLine();
		overviewForm.bindTextField("Postcode", "location.postcode");
		overviewForm.newLine();

		overviewForm.colspan(3);
		CKEditorEmailField detailsEditor = overviewForm.bindEditorField(Event_.details, true);
		detailsEditor.setSizeFull();
		overviewForm.setExpandRatio(1.0f);
		
		layout.addComponent(overviewForm);
		
//		attachedDocuments = new AttachedDocuments();
//		layout.addComponent(attachedDocuments);

		return layout;
	}

	@Override
	public void rowChanged(EntityItem<au.org.scoutmaster.domain.Event> item)
	{
//		if (item != null)
//			attachedDocuments.setDocuments(item.getEntity().getDocuments());
		super.rowChanged(item);
	}


	@Override
	protected Filter getContainerFilter(String filterString, boolean advancedSearchActive)
	{
		return new Or(new Or(new SimpleStringFilter(Event_.eventStartDateTime.getName(), filterString, true,
				false), new SimpleStringFilter(Event_.eventEndDateTime.getName(), filterString, true, false)),
				new SimpleStringFilter(Event_.subject.getName(), filterString, true, false));
	}

	@Override
	protected String getTitleText()
	{
		return "Events";
	}


	@Override
	public boolean hasEntity(Contact entity)
	{
		EventDao daoEvent = new DaoFactory().getEventDao();
		return daoEvent.hasCoordinator(this.getCurrent(), entity);
	}


	@Override
	public void attachEntity(Contact entity)
	{
		EventDao daoEvent = new DaoFactory().getEventDao();
		daoEvent.attachCoordinator(this.getCurrent(), entity);
	}


	@Override
	public void detachEntity(Contact entity)
	{
		EventDao daoEvent = new DaoFactory().getEventDao();
		daoEvent.detachCoordinator(this.getCurrent(), entity);
	}


	@Override
	protected List<CrudAction<au.org.scoutmaster.domain.Event>> getCrudActions()
	{
		List<CrudAction<au.org.scoutmaster.domain.Event>> actions = super.getCrudActions();
		
		actions.add(new CopyEventAction());
		return actions;
	}
	
	
}
