package au.org.scoutmaster.forms;

import java.util.Date;

import org.apache.log4j.Logger;

import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.dao.EntityManagerProvider;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.EventDao;
import au.org.scoutmaster.domain.Event_;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;
import au.org.scoutmaster.util.SMNotification;
import au.org.scoutmaster.validator.DateRangeValidator;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class EventForm extends VerticalLayout implements com.vaadin.ui.Button.ClickListener
{
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(EventForm.class);
	private static final long serialVersionUID = 1L;

	private Window owner;
	private ValidatingFieldGroup<au.org.scoutmaster.domain.Event> fieldGroup;
	private DateField startDateField;
	private DateField endDateField;
	private JPAContainer<au.org.scoutmaster.domain.Event> container;
	private EntityItem<au.org.scoutmaster.domain.Event> entityItem;
	private Button save;
	private Button cancel;
	private au.org.scoutmaster.domain.Event event;
	private SaveEventListener newEventListener;

	/**
	 * 
	 * @param newButtonListener 
	 * @param sender
	 *            the user who is sending this email.
	 */
	public EventForm(Window owner, au.org.scoutmaster.domain.Event event, SaveEventListener newEventListener)
	{
		this.owner = owner;
		this.event = event;
		this.newEventListener = newEventListener;
		
		EventDao daoEvent = new DaoFactory().getEventDao();
		// we are going to manipulate the event on a different thread.
		EntityManagerProvider.detach(event);
		
		this.container = daoEvent.createVaadinContainer();
		entityItem = container.createEntityItem(event);
		fieldGroup = new ValidatingFieldGroup<au.org.scoutmaster.domain.Event>(au.org.scoutmaster.domain.Event.class);
		fieldGroup.setItemDataSource(entityItem);
		
		buildForm();
	}

	void buildForm()
	{
		SMMultiColumnFormLayout<au.org.scoutmaster.domain.Event> overviewForm = new SMMultiColumnFormLayout<au.org.scoutmaster.domain.Event>(
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
		TextField subject = overviewForm.bindTextField("Subject", Event_.subject);
		overviewForm.newLine();

		overviewForm.bindColorPicker("Colour", Event_.color);
		overviewForm.newLine();

		overviewForm.bindBooleanField("All Day Event", Event_.allDayEvent);
		overviewForm.newLine();

		overviewForm.colspan(3);
		startDateField = overviewForm.bindDateField("Start", Event_.eventStartDateTime, "yyyy-MM-dd hh:mm a",
				Resolution.MINUTE);
		startDateField.setRangeStart(new Date());
		overviewForm.newLine();

		overviewForm.colspan(3);
		endDateField = overviewForm.bindDateField("End", Event_.eventEndDateTime, "yyyy-MM-dd hh:mm a",
				Resolution.MINUTE);
		endDateField.setRangeStart(new Date());

		startDateField.addValidator(new DateRangeValidator(startDateField.getCaption(), endDateField));

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

		this.addComponent(overviewForm);
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		buttonLayout.setMargin(true);
		
		save = new Button("Save");
		save.addClickListener(this);
		cancel = new Button("Cancel");
		cancel.addClickListener(this);
		
		buttonLayout.addComponent(cancel);
		buttonLayout.addComponent(save);
		buttonLayout.setComponentAlignment(cancel, Alignment.MIDDLE_LEFT);
		buttonLayout.setComponentAlignment(save, Alignment.MIDDLE_RIGHT);
		
		this.addComponent(buttonLayout);
		
		subject.focus();

	}

	@Override
	public void buttonClick(ClickEvent event)
	{
		if (event.getButton() == save)
		{
			EventDao daoEvent = new DaoFactory().getEventDao();
			try
			{
				this.fieldGroup.commit();
				this.event = daoEvent.merge(this.event);
				EventForm.this.newEventListener.eventSaved(this.event);
				this.owner.close();
			}
			catch (CommitException e)
			{
				SMNotification.show(e, Type.ERROR_MESSAGE);
			}
		}
		
		if (event.getButton() == cancel)
		{
			this.owner.close();
		}
	}
	
	public interface SaveEventListener
	{
		/**
		 * Called when the user clicks save to notify the listener that a new event was added.
		 * @param event
		 */
		void eventSaved(au.org.scoutmaster.domain.Event event);
	}

}
