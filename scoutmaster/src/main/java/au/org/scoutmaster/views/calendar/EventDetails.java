package au.org.scoutmaster.views.calendar;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.CrudAction;
import au.com.vaadinutils.crud.CrudEntity;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.domain.iColorFactory;
import au.org.scoutmaster.dao.ColorDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.EventDao;
import au.org.scoutmaster.domain.Event_;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;
import au.org.scoutmaster.util.SMNotification;
import au.org.scoutmaster.validator.DateRangeValidator;

public class EventDetails extends VerticalLayout implements com.vaadin.ui.Button.ClickListener, ValueChangeListener
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(EventDetails.class);
	private static final long serialVersionUID = 1L;

	private final ValidatingFieldGroup<au.org.scoutmaster.domain.Event> fieldGroup;
	private DateField startDateField;
	private DateField endDateField;
	private final JPAContainer<au.org.scoutmaster.domain.Event> container;
	private EntityItem<au.org.scoutmaster.domain.Event> entityItem;
	private Button save;
	private Button cancel;
	private au.org.scoutmaster.domain.Event event;
	private final SaveEventListener newEventListener;
	private Button delete;
	private boolean newEvent;
	private final boolean readonly;
	private RichTextArea detailsEditor;
	private Label clickToViewLabel;
	private RichTextArea message;

	/**
	 *
	 * @param readonly
	 * @param newButtonListener
	 * @param sender
	 *            the user who is sending this email.
	 */
	public EventDetails(final SaveEventListener newEventListener, final boolean readonly)
	{
		this.readonly = readonly;
		final EventDao daoEvent = new DaoFactory().getEventDao();
		this.container = daoEvent.createVaadinContainer();
		this.fieldGroup = new ValidatingFieldGroup<au.org.scoutmaster.domain.Event>(
				au.org.scoutmaster.domain.Event.class);
		this.newEventListener = newEventListener;
		buildForm();
		this.newEvent = false;
		this.fieldGroup.setEnabled(false);
		if (!readonly)
		{
			this.delete.setEnabled(false);
			this.cancel.setEnabled(false);
			this.save.setEnabled(false);
		}

	}

	void setEvent(final au.org.scoutmaster.domain.Event event, final boolean newEvent)
	{
		this.event = event;
		this.newEvent = newEvent;

		if (event != null)
		{
			this.clickToViewLabel.setVisible(false);
			this.entityItem = this.container.createEntityItem(event);

			// startDateField.removeValueChangeListener(this);
			this.fieldGroup.setItemDataSource(this.entityItem);
			// startDateField.addValueChangeListener(this);

			if (!this.readonly)
			{
				if (newEvent)
				{
					this.delete.setEnabled(false);
				}
				else
				{
					this.delete.setEnabled(true);
				}
				this.save.setEnabled(true);
				this.cancel.setEnabled(true);
				this.fieldGroup.setEnabled(true);
				this.detailsEditor.setReadOnly(false);
			}
			else
			{
				this.fieldGroup.setEnabled(true);
				this.fieldGroup.setReadOnly(true);
			}

		}
		else
		{
			this.clickToViewLabel.setVisible(false);
			this.fieldGroup.discard();
			this.entityItem = this.container.createEntityItem(new au.org.scoutmaster.domain.Event());
			this.fieldGroup.setItemDataSource(this.entityItem);
			this.fieldGroup.setEnabled(false);
			this.delete.setEnabled(false);
			this.cancel.setEnabled(false);
			this.save.setEnabled(false);
		}
	}

	void buildForm()
	{
		this.clickToViewLabel = new Label("<b>Click an event in the Calendar to view its details.</b>");
		this.clickToViewLabel.setContentMode(ContentMode.HTML);
		this.clickToViewLabel.setWidth(null);
		this.addComponent(this.clickToViewLabel);
		setComponentAlignment(this.clickToViewLabel, Alignment.MIDDLE_RIGHT);

		final SMMultiColumnFormLayout<au.org.scoutmaster.domain.Event> overviewForm = new SMMultiColumnFormLayout<au.org.scoutmaster.domain.Event>(
				3, this.fieldGroup);
		overviewForm.setColumnLabelWidth(0, 100);
		overviewForm.setColumnLabelWidth(1, 0);
		overviewForm.setColumnLabelWidth(2, 60);
		overviewForm.setColumnFieldWidth(0, 100);
		overviewForm.setColumnFieldWidth(1, 100);
		overviewForm.setColumnFieldWidth(2, 20);
		overviewForm.setColumnExpandRatio(1, 1.0f);
		overviewForm.colspan(3);
		final TextField subject = overviewForm.bindTextField("Subject", Event_.subject);
		overviewForm.newLine();

		if (!this.readonly)
		{

			final iColorFactory factory = new ColorDao.ColorFactory();
			overviewForm.bindColorPicker(factory, "Colour", Event_.color.getName());
			overviewForm.newLine();

			overviewForm.bindBooleanField("All Day Event", Event_.allDayEvent);
			overviewForm.newLine();
		}

		overviewForm.colspan(3);
		this.startDateField = overviewForm.bindDateField("Start", Event_.eventStartDateTime, "yyyy-MM-dd hh:mm a",
				Resolution.MINUTE);
		this.startDateField.setRangeStart(new Date());
		this.startDateField.addValueChangeListener(this);
		overviewForm.newLine();

		overviewForm.colspan(3);
		this.endDateField = overviewForm.bindDateField("End", Event_.eventEndDateTime, "yyyy-MM-dd hh:mm a",
				Resolution.MINUTE);
		this.endDateField.setRangeStart(new Date());

		this.startDateField.addValidator(new DateRangeValidator(this.startDateField.getCaption(), this.endDateField));

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

		if (!this.readonly)
		{
			final VerticalLayout detailsEditorLayout = new VerticalLayout();
			detailsEditorLayout.setMargin(new MarginInfo(false, false, false, true));
			detailsEditorLayout.setSizeFull();
			this.detailsEditor = new RichTextArea();
			this.detailsEditor.setNullRepresentation("");
			this.detailsEditor.setSizeFull();
			this.fieldGroup.bind(this.detailsEditor, Event_.details.getName());

			detailsEditorLayout.addComponent(this.detailsEditor);
			this.addComponent(detailsEditorLayout);
			setComponentAlignment(detailsEditorLayout, Alignment.MIDDLE_RIGHT);
		}
		else
		{
			this.message = new RichTextArea();
			this.message.setNullRepresentation("");
			this.message.setReadOnly(true);

			this.setMargin(true);
			this.fieldGroup.bind(this.message, Event_.details.getName());
			this.addComponent(this.message);
			setExpandRatio(this.message, 1.0f);

		}

		if (!this.readonly)
		{
			final HorizontalLayout buttonLayout = new HorizontalLayout();
			buttonLayout.setMargin(new MarginInfo(false, false, false, true));
			buttonLayout.setSpacing(true);
			this.save = new Button("Save");
			this.save.addClickListener(this);
			this.cancel = new Button("Cancel");
			this.cancel.addClickListener(this);

			this.delete = new Button("Delete");
			this.delete.addClickListener(this);

			buttonLayout.addComponent(this.cancel);
			buttonLayout.addComponent(this.save);
			buttonLayout.setComponentAlignment(this.cancel, Alignment.MIDDLE_LEFT);
			buttonLayout.setComponentAlignment(this.save, Alignment.MIDDLE_RIGHT);

			final HorizontalLayout buttonGroupLayout = new HorizontalLayout();
			buttonGroupLayout.setMargin(true);
			buttonGroupLayout.addComponent(this.delete);
			buttonGroupLayout.addComponent(buttonLayout);
			buttonGroupLayout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_RIGHT);
			buttonGroupLayout.setComponentAlignment(this.delete, Alignment.MIDDLE_LEFT);
			buttonGroupLayout.setWidth("100%");
			buttonGroupLayout.setExpandRatio(buttonLayout, 1.0f);

			this.addComponent(buttonGroupLayout);
			setComponentAlignment(buttonGroupLayout, Alignment.BOTTOM_RIGHT);
		}

		subject.focus();

	}

	@Override
	public void buttonClick(final ClickEvent event)
	{
		if (event.getButton() == this.save)
		{
			final EventDao daoEvent = new DaoFactory().getEventDao();
			try
			{
				this.fieldGroup.commit();
				EntityManagerProvider.getEntityManager().detach(this.event);
				this.event = daoEvent.merge(this.event);

				// the merge returns a new event so we have to reattach it to
				// the field group.
				setEvent(this.event, this.newEvent);
				this.newEventListener.eventSaved(this.event, this.newEvent);
				this.newEvent = false;
			}
			catch (final CommitException e)
			{
				SMNotification.show(e, Type.ERROR_MESSAGE);
			}
		}

		else if (event.getButton() == this.cancel)
		{
			if (this.newEvent)
			{
				this.newEvent = false;
				setEvent(null, false);
			}
			else
			{
				this.fieldGroup.discard();
			}
		}
		else if (event.getButton() == this.delete)
		{
			if (EventDetails.this.event != null)
			{

				ConfirmDialog.show(UI.getCurrent(), "Confirm Delete",
						"Are you sure you want to delete " + EventDetails.this.event.getSubject() + "?", "Delete",
						"Cancel", new ConfirmDialog.Listener()
						{
							private static final long serialVersionUID = 1L;

							@Override
							public void onClose(final ConfirmDialog dialog)
							{
								if (dialog.isConfirmed())
								{

									EventDetails.this.delete();
								}
							}

						});
			}
		}

	}

	protected void delete()
	{
		final EventDao daoEvent = new DaoFactory().getEventDao();
		// we can't delete an unmanaged entity so merge and then delete
		// it.
		this.event = daoEvent.merge(EventDetails.this.event);
		daoEvent.remove(EventDetails.this.event);
		EventDetails.this.newEventListener.eventDeleted(EventDetails.this.event);
		setEvent(null, false);
	}

	public interface SaveEventListener
	{
		/**
		 * Called when the user clicks save to notify the listener that a new
		 * event was added or an existing event updated..
		 *
		 * @param event
		 */
		void eventSaved(au.org.scoutmaster.domain.Event event, boolean newEvent);

		/**
		 * Called when the user clicks delete to notify the listener that the
		 * event was delete.
		 *
		 * @param event
		 */

		void eventDeleted(au.org.scoutmaster.domain.Event event);
	}

	@Override
	public void valueChange(final ValueChangeEvent event)
	{
		if (!this.readonly)
		{
			// The start date has just changed so make certain the end date is
			// in
			// the future
			// by default we set it to two hours into the future.

			final DateField startDateField = (DateField) event.getProperty();

			final DateTime startDate = new DateTime(startDateField.getValue());

			if (this.endDateField.getValue() != null && this.endDateField.getValue().before(startDateField.getValue()))
			{
				this.endDateField.setValue(startDate.plusHours(2).toDate());
			}
		}
	}

	public class CrudActionDelete<E extends CrudEntity> implements CrudAction<E>
	{
		private static final long serialVersionUID = 1L;
		private boolean isDefault = true;

		@Override
		public void exec(final BaseCrudView<E> crud, final EntityItem<E> entity)
		{
			ConfirmDialog.show(UI.getCurrent(), "Confirm Delete",
					"Are you sure you want to delete " + entity.getEntity().getName() + "?", "Delete", "Cancel",
					new ConfirmDialog.Listener()
					{
						private static final long serialVersionUID = 1L;

						@Override
						public void onClose(final ConfirmDialog dialog)
						{
							if (dialog.isConfirmed())
							{

								crud.delete();
							}
						}

					});

		}

		@Override
		public String toString()
		{
			return "Delete";
		}

		@Override
		public boolean isDefault()
		{
			return this.isDefault;
		}

		public void setIsDefault(final boolean isDefault)
		{
			this.isDefault = isDefault;
		}

		@Override
		public boolean showPreparingDialog()
		{
			return false;
		}
	}

}
