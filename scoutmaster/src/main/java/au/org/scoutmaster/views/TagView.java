package au.org.scoutmaster.views;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.Tag;
import au.org.scoutmaster.domain.Tag_;
import au.org.scoutmaster.security.Action;
import au.org.scoutmaster.security.eSecurityRole;
import au.org.scoutmaster.security.annotations.iFeature;
import au.org.scoutmaster.security.annotations.iPermission;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;

@Menu(display = "Tags", path = "Members")
/** @formatter:off **/
@iFeature(permissions =
	{ @iPermission(action = Action.ACCESS, roles = { eSecurityRole.TECH_SUPPORT, eSecurityRole.GROUP_LEADER, eSecurityRole.COMMITTEE_MEMBER, eSecurityRole.LEADER})
	, @iPermission(action = Action.DELETE, roles = { eSecurityRole.TECH_SUPPORT, eSecurityRole.GROUP_LEADER, eSecurityRole.COMMITTEE_MEMBER, eSecurityRole.LEADER})
	, @iPermission(action = Action.EDIT, roles = { eSecurityRole.TECH_SUPPORT, eSecurityRole.GROUP_LEADER, eSecurityRole.COMMITTEE_MEMBER, eSecurityRole.LEADER})
	, @iPermission(action = Action.NEW, roles = { eSecurityRole.TECH_SUPPORT, eSecurityRole.GROUP_LEADER, eSecurityRole.COMMITTEE_MEMBER, eSecurityRole.LEADER})
	} )
/** @formatter:on **/
public class TagView extends BaseCrudView<Tag> implements View, Selected<Tag>
{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(TagView.class);

	public static final String NAME = "Tag";

	private CheckBox detachable;

	private SMMultiColumnFormLayout<Tag> overviewForm;

	private CheckBox builtin;

	@Override
	protected AbstractLayout buildEditor(final ValidatingFieldGroup<Tag> fieldGroup2)
	{
		final VerticalLayout layout = new VerticalLayout();

		this.overviewForm = new SMMultiColumnFormLayout<Tag>(2, this.fieldGroup);
		this.overviewForm.setColumnFieldWidth(0, 280);
		this.overviewForm.setColumnLabelWidth(0, 80);
		this.overviewForm.setSizeFull();
		this.overviewForm.getFieldGroup().setReadOnly(true);

		this.overviewForm.bindTextField("Nanme", Tag_.name);
		this.overviewForm.newLine();
		this.overviewForm.bindTextField("Description", Tag_.description);
		this.overviewForm.newLine();
		this.builtin = this.overviewForm.bindBooleanField("Built In", Tag_.builtin);
		this.builtin.setReadOnly(true);
		this.overviewForm.newLine();
		this.detachable = this.overviewForm.bindBooleanField("Detachable", Tag_.detachable.getName());
		this.overviewForm.newLine();
		layout.addComponent(this.overviewForm);

		return layout;
	}

	@Override
	public void enter(final ViewChangeEvent event)
	{
		final JPAContainer<Tag> container = new DaoFactory().getTagDao().createVaadinContainer();
		container.sort(new String[]
		{ Tag_.name.getName() }, new boolean[]
		{ true });

		final Builder<Tag> builder = new HeadingPropertySet.Builder<Tag>();
		builder.addColumn("Tag", Tag_.name).addColumn("Description", Tag_.description)
				.addColumn("Built In", Tag_.builtin).addColumn("Detachable", Tag_.detachable)
				.addColumn("Created", Tag_.created.getName(), "YYYY-MM-dd", 100);

		super.init(Tag.class, container, builder.build());

	}

	@Override
	protected Filter getContainerFilter(final String filterString, final boolean advancedSearchActive)
	{
		return new Or(new SimpleStringFilter(Tag_.name.getName(), filterString, true, false),
				new SimpleStringFilter(Tag_.description.getName(), filterString, true, false));
	}

	@Override
	public boolean allowCurrentRowEdit(final EntityItem<Tag> item)
	{
		boolean allowEditing = false;

		if (item != null)
		{
			final Tag entity = item.getEntity();

			if (entity != null && entity.getBuiltin())
			{
				// You can't edit builin tags.
				this.detachable.setReadOnly(true);
				this.overviewForm.setReadOnly(true);
			}
			else
			{
				allowEditing = true;
				this.overviewForm.setReadOnly(false);
				this.detachable.setReadOnly(false);
			}
			// You can never modify the built in flag
			this.builtin.setReadOnly(true);

		}

		return allowEditing;
	}

	@Override
	protected String getTitleText()
	{
		return "Tags";
	}

	@Override
	protected void postDelete(final Tag entity)
	{
		// need to flush the cache as a db cascade delete removed child records.
		EntityManagerProvider.getEntityManager().getEntityManagerFactory().getCache().evictAll();
		super.postDelete(entity);
	}

}
