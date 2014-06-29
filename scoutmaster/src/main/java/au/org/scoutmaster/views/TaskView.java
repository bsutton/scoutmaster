package au.org.scoutmaster.views;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.FormHelper;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.dao.JpaBaseDao;
import au.com.vaadinutils.dao.Path;
import au.com.vaadinutils.fields.CKEditorEmailField;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.Section;
import au.org.scoutmaster.domain.Section_;
import au.org.scoutmaster.domain.Task;
import au.org.scoutmaster.domain.TaskStatus;
import au.org.scoutmaster.domain.TaskStatus_;
import au.org.scoutmaster.domain.TaskType;
import au.org.scoutmaster.domain.TaskType_;
import au.org.scoutmaster.domain.Task_;
import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.domain.access.User_;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.VerticalLayout;

@Menu(display = "Tasks", path = "Admin")
public class TaskView extends BaseCrudView<Task> implements View, Selected<Task>
{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger();

	public static final String NAME = "Tasks";

	@Override
	protected AbstractLayout buildEditor(final ValidatingFieldGroup<Task> fieldGroup2)
	{
		final VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		final SMMultiColumnFormLayout<Task> overviewForm = new SMMultiColumnFormLayout<Task>(2, this.fieldGroup);
		overviewForm.setColumnFieldWidth(0, 280);
		overviewForm.setColumnLabelWidth(0, 70);
		overviewForm.setColumnExpandRatio(1, 1.0f);
		overviewForm.setSizeFull();

		final FormHelper<Task> formHelper = overviewForm.getFormHelper();

		overviewForm.bindTextField("Subject", Task_.subject);
		overviewForm.newLine();

		formHelper.new EntityFieldBuilder<User>().setLabel("Added By").setField(Task_.addedBy)
		.setListFieldName(User_.username).build();

		overviewForm.newLine();

		overviewForm.bindBooleanField("Private Task", Task_.privateTask);
		overviewForm.newLine();

		overviewForm.bindDateField("Due Date", Task_.dueDate, "yyyy-MM-dd hh:mm", Resolution.MINUTE);
		overviewForm.newLine();

		overviewForm.bindDateField("Date Completed", Task_.completionDate, "yyyy-MM-dd hh:mm", Resolution.MINUTE);
		overviewForm.newLine();

		formHelper.new EntityFieldBuilder<TaskStatus>().setLabel("Status").setField(Task_.taskStatus)
		.setListFieldName(TaskStatus_.name).build();
		overviewForm.newLine();

		formHelper.new EntityFieldBuilder<TaskType>().setLabel("Type").setField(Task_.taskType)
		.setListFieldName(TaskType_.name).build();
		overviewForm.newLine();

		formHelper.new EntityFieldBuilder<Contact>().setLabel("With Contact").setField(Task_.withContact)
		.setListFieldName("fullname").build();

		overviewForm.newLine();
		formHelper.new EntityFieldBuilder<Section>().setLabel("Section").setField(Task_.section)
		.setListFieldName(Section_.name).build();

		overviewForm.newLine();
		overviewForm.colspan(2);

		final CKEditorEmailField detailsEditor = overviewForm.bindEditorField(Task_.details, true);
		detailsEditor.setSizeFull();
		overviewForm.setExpandRatio(1.0f);

		layout.addComponent(overviewForm);

		return layout;
	}

	@Override
	protected Task preNew() throws InstantiationException, IllegalAccessException
	{
		Task task = super.preNew();
		final JpaBaseDao<TaskStatus, Long> daoStatus = DaoFactory.getGenericDao(TaskStatus.class);
		final TaskStatus result = daoStatus.findOneByAttribute(TaskStatus_.name, TaskStatus.NOT_STARTED);
		task.setStatus(result);
		task.setAddedBy(SMSession.INSTANCE.getLoggedInUser());
		return task;
	}

	@Override
	public void enter(final ViewChangeEvent event)
	{
		final JPAContainer<Task> container = DaoFactory.getGenericDao(Task.class).createVaadinContainer();
		container.sort(new String[]
				{ Task_.dueDate.getName() }, new boolean[]
						{ false });

		final Builder<Task> builder = new HeadingPropertySet.Builder<Task>();
		builder.addColumn("Owner", Task_.addedBy).addColumn("Subject", Task_.subject)
		.addColumn("Due Date", Task_.dueDate).addColumn("Status", Task_.taskStatus)
		.addColumn("Private", Task_.privateTask).addColumn("Type", Task_.taskType);

		super.init(Task.class, container, builder.build());

	}

	@Override
	protected Filter getContainerFilter(final String filterString, final boolean advancedSearchActive)
	{
		if (filterString.trim().length() > 0)
		{
			return new Or(new SimpleStringFilter(Task_.dueDate.getName(), filterString, true, false),
					new SimpleStringFilter(new Path(Task_.taskType, TaskType_.name).getName(), filterString, true,
							false), new SimpleStringFilter(new Path(Task_.withContact, Contact_.lastname).getName(),
									filterString, true, false), new SimpleStringFilter(new Path(Task_.withContact,
											Contact_.firstname).getName(), filterString, true, false), new SimpleStringFilter(new Path(
													Task_.addedBy, User_.username).getName(), filterString, true, false),
													new SimpleStringFilter(Task_.subject.getName(), filterString, true, false));
		}
		else
		{
			return null;
		}
	}

	@Override
	protected String getTitleText()
	{
		return "Tasks";
	}

}
