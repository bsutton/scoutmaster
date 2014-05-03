package au.org.scoutmaster.views;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.TaskType;
import au.org.scoutmaster.domain.TaskType_;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.VerticalLayout;

@Menu(display = "Task Types", path = "Admin.Lists")
public class TaskTypeView extends BaseCrudView<TaskType> implements View, Selected<TaskType>
{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(TaskTypeView.class);

	public static final String NAME = "TaskType";

	private SMMultiColumnFormLayout<TaskType> overviewForm;

	@Override
	protected AbstractLayout buildEditor(final ValidatingFieldGroup<TaskType> fieldGroup2)
	{
		final VerticalLayout layout = new VerticalLayout();

		this.overviewForm = new SMMultiColumnFormLayout<TaskType>(2, this.fieldGroup);
		this.overviewForm.setColumnFieldWidth(0, 280);
		this.overviewForm.setColumnLabelWidth(0, 70);
		this.overviewForm.setSizeFull();

		this.overviewForm.bindTextField("Name", TaskType_.name);
		this.overviewForm.newLine();
		this.overviewForm.bindTextField("Description", TaskType_.description);
		this.overviewForm.newLine();
		layout.addComponent(this.overviewForm);

		return layout;
	}

	@Override
	public void enter(final ViewChangeEvent event)
	{
		final JPAContainer<TaskType> container = DaoFactory.getGenericDao(TaskType.class).createVaadinContainer();
		container.sort(new String[]
		{ TaskType_.name.getName() }, new boolean[]
		{ true });

		final Builder<TaskType> builder = new HeadingPropertySet.Builder<TaskType>();
		builder.addColumn("TaskType", TaskType_.name).addColumn("Description", TaskType_.description);

		super.init(TaskType.class, container, builder.build());

	}

	@Override
	protected Filter getContainerFilter(final String filterString, final boolean advancedSearchActive)
	{
		return new Or(new SimpleStringFilter(TaskType_.name.getName(), filterString, true, false),
				new SimpleStringFilter(TaskType_.description.getName(), filterString, true, false));
	}

	@Override
	protected String getTitleText()
	{
		return "Task Types";
	}
}
