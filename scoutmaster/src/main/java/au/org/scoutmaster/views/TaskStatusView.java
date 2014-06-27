package au.org.scoutmaster.views;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.TaskStatus;
import au.org.scoutmaster.domain.TaskStatus_;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.VerticalLayout;

@Menu(display = "Task Status'", path = "Admin.Lists")
public class TaskStatusView extends BaseCrudView<TaskStatus> implements View, Selected<TaskStatus>
{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(TaskStatusView.class);

	public static final String NAME = "TaskStatus";

	private SMMultiColumnFormLayout<TaskStatus> overviewForm;

	@Override
	protected AbstractLayout buildEditor(final ValidatingFieldGroup<TaskStatus> fieldGroup2)
	{
		final VerticalLayout layout = new VerticalLayout();

		this.overviewForm = new SMMultiColumnFormLayout<TaskStatus>(2, this.fieldGroup);
		this.overviewForm.setColumnFieldWidth(0, 280);
		this.overviewForm.setColumnLabelWidth(0, 70);
		this.overviewForm.setSizeFull();

		this.overviewForm.bindTextField("Nanme", TaskStatus_.name);
		this.overviewForm.newLine();
		this.overviewForm.bindTextField("Description", TaskStatus_.description);
		this.overviewForm.newLine();
		layout.addComponent(this.overviewForm);

		return layout;
	}

	@Override
	public void enter(final ViewChangeEvent event)
	{
		final JPAContainer<TaskStatus> container = DaoFactory.getGenericDao(TaskStatus.class).createVaadinContainer();
		container.sort(new String[]
				{ TaskStatus_.name.getName() }, new boolean[]
						{ true });

		final Builder<TaskStatus> builder = new HeadingPropertySet.Builder<TaskStatus>();
		builder.addColumn("TaskStatus", TaskStatus_.name).addColumn("Description", TaskStatus_.description);

		super.init(TaskStatus.class, container, builder.build());

	}

	@Override
	protected Filter getContainerFilter(final String filterString, final boolean advancedSearchActive)
	{
		return new Or(new SimpleStringFilter(TaskStatus_.name.getName(), filterString, true, false),
				new SimpleStringFilter(TaskStatus_.description.getName(), filterString, true, false));
	}

	@Override
	protected String getTitleText()
	{
		return "Task Status'";
	}
}
