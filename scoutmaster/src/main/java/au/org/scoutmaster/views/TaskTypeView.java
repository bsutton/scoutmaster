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

@Menu(display = "Task Types", path="Admin.Lists")
public class TaskTypeView extends BaseCrudView<TaskType> implements View, Selected<TaskType>
{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(TaskTypeView.class);

	public static final String NAME = "TaskType";

	private SMMultiColumnFormLayout<TaskType> overviewForm;


	@Override
	protected AbstractLayout buildEditor(ValidatingFieldGroup<TaskType> fieldGroup2)
	{
		VerticalLayout layout = new VerticalLayout();

		overviewForm = new SMMultiColumnFormLayout<TaskType>(2, this.fieldGroup);
		overviewForm.setColumnFieldWidth(0, 280);
		overviewForm.setColumnLabelWidth(0, 70);
		overviewForm.setSizeFull();

		overviewForm.bindTextField("Name", TaskType_.name);
		overviewForm.newLine();
		overviewForm.bindTextField("Description", TaskType_.description);
		overviewForm.newLine();
		layout.addComponent(overviewForm);

		return layout;
	}

	
	@Override
	public void enter(ViewChangeEvent event)
	{
		JPAContainer<TaskType> container = DaoFactory.getGenericDao(TaskType.class).createVaadinContainer();
		container.sort(new String[]
		{ TaskType_.name.getName() }, new boolean[]
		{ true });

		Builder<TaskType> builder = new HeadingPropertySet.Builder<TaskType>();
		builder.addColumn("TaskType", TaskType_.name).addColumn("Description", TaskType_.description);

		super.init(TaskType.class, container, builder.build());

	}

	@Override
	protected Filter getContainerFilter(String filterString, boolean advancedSearchActive)
	{
		return new Or(new SimpleStringFilter(TaskType_.name.getName(), filterString, true, false), new SimpleStringFilter(
				TaskType_.description.getName(), filterString, true, false));
	}

	@Override
	protected String getTitleText()
	{
		return "Task Types";
	}
}
