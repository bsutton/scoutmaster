package au.org.scoutmaster.views;

import org.apache.log4j.Logger;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.Tag;
import au.org.scoutmaster.domain.Tag_;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.VerticalLayout;

@Menu(display = "Tag")
public class TagView extends BaseCrudView<Tag> implements View, Selected<Tag>
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(TagView.class);

	public static final String NAME = "Tag";

	private CheckBox detachable;

	private SMMultiColumnFormLayout<Tag> overviewForm;

	private CheckBox builtin;

	@Override
	protected AbstractLayout buildEditor(ValidatingFieldGroup<Tag> fieldGroup2)
	{
		VerticalLayout layout = new VerticalLayout();

		overviewForm = new SMMultiColumnFormLayout<Tag>(2, this.fieldGroup);
		overviewForm.setColumnFieldWidth(0, 280);
		overviewForm.setColumnLabelWidth(0, 70);
		overviewForm.setSizeFull();
		overviewForm.getFieldGroup().setReadOnly(true);

		overviewForm.bindTextField("Nanme", Tag_.name);
		overviewForm.newLine();
		overviewForm.bindTextField("Description", Tag_.description);
		overviewForm.newLine();
		builtin = overviewForm.bindBooleanField("Built In", Tag_.builtin);
		builtin.setReadOnly(true);
		overviewForm.newLine();
		detachable = overviewForm.bindBooleanField("Detachable", Tag_.detachable.getName());
		overviewForm.newLine();
		layout.addComponent(overviewForm);

		return layout;
	}

	
	@Override
	public void enter(ViewChangeEvent event)
	{
		JPAContainer<Tag> container = new DaoFactory().getTagDao().makeJPAContainer();
		container.sort(new String[]
		{ Tag_.name.getName() }, new boolean[]
		{ true });

		Builder<Tag> builder = new HeadingPropertySet.Builder<Tag>();
		builder.addColumn("Contact", Tag_.name).addColumn("Description", Tag_.description)
				.addColumn("Built In", Tag_.builtin).addColumn("Detachable", Tag_.detachable);

		super.init(Tag.class, container, builder.build());

	}

	@Override
	protected Filter getContainerFilter(String filterString)
	{
		return new Or(new SimpleStringFilter(Tag_.name.getName(), filterString, true, false), new SimpleStringFilter(
				Tag_.description.getName(), filterString, true, false));
	}

	@Override
	public void rowChanged(EntityItem<Tag> item)
	{
		if (item != null)
		{
			Tag entity = item.getEntity();

			if (entity != null && entity.getBuiltin())
			{

				// You can't edit builin tags.
				detachable.setReadOnly(false);
				super.showActions(false);
				super.showSaveCancel(false);
			}
			else
			{
				overviewForm.setReadOnly(false);
				detachable.setReadOnly(false);
				super.showActions(true);
				super.showSaveCancel(true);
			}
			builtin.setReadOnly(true);

			super.rowChanged(item);

		}
	}

}
