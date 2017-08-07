package au.org.scoutmaster.views.security;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.FormHelper;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.dao.Path;
import au.com.vaadinutils.help.HelpProvider;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.security.Feature_;
import au.org.scoutmaster.domain.security.Permission;
import au.org.scoutmaster.domain.security.Permission_;
import au.org.scoutmaster.help.HelpPageIdentifier;
import au.org.scoutmaster.security.Action;
import au.org.scoutmaster.security.eSecurityRole;
import au.org.scoutmaster.security.annotations.iFeature;
import au.org.scoutmaster.security.annotations.iPermission;

/**
 * @formatter:off
 */
@iFeature(permissions =
{ @iPermission(action = Action.ACCESS, roles = { eSecurityRole.TECH_SUPPORT, eSecurityRole.GROUP_LEADER})
	, @iPermission(action = Action.EDIT, roles = { eSecurityRole.TECH_SUPPORT }) })

/**
 * @formatter:on
 */
@Menu(display = "Features", path = "System")
/** A start view for navigating to the main view */
public class PermissionView extends BaseCrudView<Permission> implements HelpProvider, View
{
	private static final long serialVersionUID = 1L;
	public static final String NAME = "PermissionView";

	private void setup()
	{
		JPAContainer<Permission> container = DaoFactory.getGenericDao(Permission.class).createVaadinContainer();

		HeadingPropertySet<Permission> headings = new HeadingPropertySet.Builder<Permission>()
				.addColumn("Name", new Path(Permission_.feature, Feature_.name).getName())
				.addColumn("Action", Permission_.action).addColumn("Edited", Permission_.editedByUser).build();

		this.disallowNew(true);
		init(Permission.class, container, headings);
		this.disallowDelete(true);
	}

	public JPAContainer<Permission> makeJPAContainer()
	{
		JPAContainer<Permission> tmp = DaoFactory.getGenericDao(Permission.class).createVaadinContainer();

		tmp.sort(new Object[]
		{ new Path(Permission_.feature, Feature_.name).getName() }, new boolean[]
		{ true });

		return tmp;
	}

	@Override
	public AbstractLayout buildEditor(ValidatingFieldGroup<Permission> validatingFieldGroup)
	{

		FormLayout main = new FormLayout();
		main.setMargin(true);

		FormHelper<Permission> helper = new FormHelper<>(main, validatingFieldGroup);

		final TextField name = helper.bindTextField("Name", new Path(Permission_.feature, Feature_.name).getName());
		name.setReadOnly(true);

		PermissionRoleEdit child = new PermissionRoleEdit(main);
		this.addChildCrudListener(child);

		return main;

	}

	@Override
	protected Filter getContainerFilter(String filterText, boolean advancedSearchActive)
	{
		Filter filter = null;
		String[] searchFields = new String[]
		{ new Path(Permission_.feature, Feature_.name).getName() };
		for (String property : searchFields)
		{
			if (filter == null)
			{
				filter = new SimpleStringFilter(property, filterText, true, false);
			}
			filter = new Or(new SimpleStringFilter(property, filterText, true, false), filter);
		}

		return filter;
	}

	@Override
	public void enter(ViewChangeEvent event)
	{
		setup();

	}

	@Override
	public HelpPageIdentifier getHelpId()
	{
		return HelpPageIdentifier.PermissionView;
	}

}