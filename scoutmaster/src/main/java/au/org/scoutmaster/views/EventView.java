package au.org.scoutmaster.views;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.VerticalLayout;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.CrudAction;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.domain.iColorFactory;
import au.com.vaadinutils.fields.AutoCompleteParent;
import au.com.vaadinutils.fields.CKEditorEmailField;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.dao.ColorDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.EventDao;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Event;
import au.org.scoutmaster.domain.Event_;
import au.org.scoutmaster.security.Action;
import au.org.scoutmaster.security.eSecurityRole;
import au.org.scoutmaster.security.annotations.iFeature;
import au.org.scoutmaster.security.annotations.iPermission;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;
import au.org.scoutmaster.validator.DateRangeValidator;
import au.org.scoutmaster.views.actions.EventActionCopy;

@Menu(display = "Events", path = "Calendar")

/** @formatter:off **/
@iFeature(permissions =
	{ @iPermission(action = Action.ACCESS, roles = { eSecurityRole.TECH_SUPPORT, eSecurityRole.GROUP_LEADER, eSecurityRole.COMMITTEE_MEMBER, eSecurityRole.LEADER})
	, @iPermission(action = Action.DELETE, roles = { eSecurityRole.TECH_SUPPORT, eSecurityRole.GROUP_LEADER, eSecurityRole.COMMITTEE_MEMBER, eSecurityRole.LEADER})
	, @iPermission(action = Action.EDIT, roles = { eSecurityRole.TECH_SUPPORT, eSecurityRole.GROUP_LEADER, eSecurityRole.COMMITTEE_MEMBER, eSecurityRole.LEADER})
	, @iPermission(action = Action.NEW, roles = { eSecurityRole.TECH_SUPPORT, eSecurityRole.GROUP_LEADER, eSecurityRole.COMMITTEE_MEMBER, eSecurityRole.LEADER})
	} )
/** @formatter:on **/

public class EventView extends BaseCrudView<Event>
		implements View, Selected<Event>, AutoCompleteParent<Contact>, ValueChangeListener
{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(EventView.class);

	public static final String NAME = "Event";

	private DateField endDateField;

	private DateField startDateField;

	// private AttachedDocuments attachedDocuments;

	private JPAContainer<au.org.scoutmaster.domain.Event> container;

	@Override
	public void enter(final ViewChangeEvent event)
	{
		this.container = new DaoFactory().getDao(EventDao.class).createVaadinContainer();
		this.container.sort(new String[]
		{ Event_.eventStartDateTime.getName() }, new boolean[]
		{ false });

		final Builder<au.org.scoutmaster.domain.Event> builder = new HeadingPropertySet.Builder<au.org.scoutmaster.domain.Event>();
		builder.addColumn("Subject", Event_.subject).addColumn("All Day", Event_.allDayEvent)
				.addColumn("Start Date", Event_.eventStartDateTime).addColumn("End Date", Event_.eventEndDateTime);

		super.init(au.org.scoutmaster.domain.Event.class, this.container, builder.build());

	}

	@Override
	protected AbstractLayout buildEditor(final ValidatingFieldGroup<au.org.scoutmaster.domain.Event> fieldGroup2)
	{
		final VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		final SMMultiColumnFormLayout<au.org.scoutmaster.domain.Event> overviewForm = new SMMultiColumnFormLayout<au.org.scoutmaster.domain.Event>(
				3, this.fieldGroup);
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

		// overviewForm.bindBooleanField("Section Meeting",
		// Event_.sectionMeeting);

		// SectionTypeDao daoSectionType = new
		// DaoFactory().getDao(SectionTypeDao.class);

		// overviewForm.bindEntityField("Section", fieldName, listClazz,
		// listFieldName);

		// overviewForm.getFormHelper().new
		// ListSelectBuilder<QualificationType>()
		// .setField(Event_.sectionType)
		// .setContainer(daoSectionType.createVaadinContainer())
		// .setForm(overviewForm)
		// .setListFieldName(QualificationType_.name)
		// .setLabel("Leader Qualifications")
		// .build();
		overviewForm.newLine();

		final iColorFactory factory = new ColorDao.ColorFactory();
		overviewForm.bindColorPicker(factory, "Colour", Event_.color.getName());
		overviewForm.newLine();

		overviewForm.bindBooleanField("All Day Event", Event_.allDayEvent);
		overviewForm.newLine();

		overviewForm.colspan(3);
		this.startDateField = overviewForm.bindDateField("Start", Event_.eventStartDateTime, "yyyy-MM-dd hh:mm a",
				Resolution.MINUTE);
		this.startDateField.setRangeStart(new Date());
		overviewForm.newLine();

		overviewForm.colspan(3);
		this.endDateField = overviewForm.bindDateField("End", Event_.eventEndDateTime, "yyyy-MM-dd hh:mm a",
				Resolution.MINUTE);
		this.endDateField.setRangeStart(new Date());
		this.endDateField.setValue(new DateTime().plusHours(2).toDate());

		this.startDateField.addValidator(new DateRangeValidator(this.startDateField.getCaption(), this.endDateField));
		this.startDateField.addValueChangeListener(this);

		overviewForm.newLine();

		// overviewForm.bindAutoCompleteField("Co-ordinators",
		// Event_.coordinators, Contact.class);

		// ContactDao daoContact = new DaoFactory().getContactDao();
		// EntityAutoCompleteField<Contact, ContactDao> contactSelect = new
		// EntityAutoCompleteField<Contact,
		// ContactDao>(daoContact.createVaadinContainer(), daoContact,
		// "Coordinators", this);
		// overviewForm.addComponent(contactSelect);

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
		final CKEditorEmailField detailsEditor = overviewForm.bindEditorField(Event_.details, true);
		detailsEditor.setSizeFull();
		overviewForm.setExpandRatio(1.0f);

		layout.addComponent(overviewForm);

		// attachedDocuments = new AttachedDocuments();
		// layout.addComponent(attachedDocuments);

		return layout;
	}

	@Override
	public void rowChanged(final EntityItem<au.org.scoutmaster.domain.Event> item)
	{
		// if (item != null)
		// attachedDocuments.setDocuments(item.getEntity().getDocuments());
		super.rowChanged(item);
	}

	@Override
	protected Filter getContainerFilter(final String filterString, final boolean advancedSearchActive)
	{
		return new Or(
				new Or(new SimpleStringFilter(Event_.eventStartDateTime.getName(), filterString, true, false),
						new SimpleStringFilter(Event_.eventEndDateTime.getName(), filterString, true, false)),
				new SimpleStringFilter(Event_.subject.getName(), filterString, true, false));
	}

	@Override
	protected String getTitleText()
	{
		return "Events";
	}

	@Override
	public boolean hasEntity(final Contact entity)
	{
		final EventDao daoEvent = new DaoFactory().getEventDao();
		return daoEvent.hasCoordinator(getCurrent(), entity);
	}

	@Override
	public void attachEntity(final Contact entity)
	{
		final EventDao daoEvent = new DaoFactory().getEventDao();
		daoEvent.attachCoordinator(getCurrent(), entity);
	}

	@Override
	public void detachEntity(final Contact entity)
	{
		final EventDao daoEvent = new DaoFactory().getEventDao();
		daoEvent.detachCoordinator(getCurrent(), entity);
	}

	@Override
	protected List<CrudAction<au.org.scoutmaster.domain.Event>> getCrudActions()
	{
		final List<CrudAction<au.org.scoutmaster.domain.Event>> actions = super.getCrudActions();

		actions.add(new EventActionCopy());
		return actions;
	}

	@Override
	public void valueChange(final ValueChangeEvent event)
	{
		// The start date has just changed so make certain the end date is in
		// the future
		// by default we set it to two hours into the future.

		final DateField startDateField = (DateField) event.getProperty();

		final DateTime startDate = new DateTime(startDateField.getValue());

		if (this.endDateField.getValue() != null && this.endDateField.getValue().before(startDate.toDate()))
		{
			this.endDateField.setValue(startDate.plusHours(2).toDate());
		}

	}

}
