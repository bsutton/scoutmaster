package au.org.scoutmaster.views;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.CrudAction;
import au.com.vaadinutils.crud.CrudActionDelete;
import au.com.vaadinutils.crud.FormHelper;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.Organisation;
import au.org.scoutmaster.domain.OrganisationType;
import au.org.scoutmaster.domain.OrganisationType_;
import au.org.scoutmaster.domain.Organisation_;
import au.org.scoutmaster.domain.PhoneType;
import au.org.scoutmaster.domain.Phone_;
import au.org.scoutmaster.security.Action;
import au.org.scoutmaster.security.eSecurityRole;
import au.org.scoutmaster.security.annotations.iFeature;
import au.org.scoutmaster.security.annotations.iPermission;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;
import au.org.scoutmaster.util.SMNotification;

@Menu(display = "Organisations", path = "Admin.Lists")
/** @formatter:off **/
@iFeature(permissions =
	{ @iPermission(action = Action.ACCESS, roles = { eSecurityRole.TECH_SUPPORT, eSecurityRole.GROUP_LEADER, eSecurityRole.COMMITTEE_MEMBER, eSecurityRole.LEADER})
	, @iPermission(action = Action.DELETE, roles = { eSecurityRole.TECH_SUPPORT, eSecurityRole.GROUP_LEADER, eSecurityRole.COMMITTEE_MEMBER, eSecurityRole.LEADER})
	, @iPermission(action = Action.EDIT, roles = { eSecurityRole.TECH_SUPPORT, eSecurityRole.GROUP_LEADER, eSecurityRole.COMMITTEE_MEMBER, eSecurityRole.LEADER})
	, @iPermission(action = Action.NEW, roles = { eSecurityRole.TECH_SUPPORT, eSecurityRole.GROUP_LEADER, eSecurityRole.COMMITTEE_MEMBER, eSecurityRole.LEADER})
	} )
/** @formatter:on **/

public class OrganisationView extends BaseCrudView<Organisation> implements View, Selected<Organisation>
{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(OrganisationView.class);

	public static final String NAME = "Organisation";

	private CheckBox primaryPhone1;
	private CheckBox primaryPhone2;
	private CheckBox primaryPhone3;

	@Override
	protected AbstractLayout buildEditor(final ValidatingFieldGroup<Organisation> fieldGroup2)
	{
		final VerticalLayout layout = new VerticalLayout();

		final SMMultiColumnFormLayout<Organisation> overviewForm = new SMMultiColumnFormLayout<Organisation>(3,
				this.fieldGroup);
		overviewForm.setColumnLabelWidth(0, 100);
		overviewForm.setColumnLabelWidth(1, 0);
		overviewForm.setColumnLabelWidth(2, 60);
		overviewForm.setColumnFieldWidth(0, 100);
		overviewForm.setColumnFieldWidth(1, 100);
		overviewForm.setColumnFieldWidth(2, 20);
		overviewForm.setSizeFull();

		final FormHelper<Organisation> formHelper = overviewForm.getFormHelper();

		overviewForm.colspan(3);
		overviewForm.bindTextField("Name", Organisation_.name);
		overviewForm.colspan(3);
		overviewForm.bindTextField("Description", Organisation_.description);
		overviewForm.colspan(3);
		formHelper.new EntityFieldBuilder<OrganisationType>().setLabel("Type").setField(Organisation_.organisationType)
				.setListFieldName(OrganisationType_.name).build();

		overviewForm.newLine();

		overviewForm.bindTextField("Phone 1", "phone1.phoneNo");
		overviewForm.bindEnumField(null, "phone1.phoneType", PhoneType.class);
		this.primaryPhone1 = overviewForm.bindBooleanField("Primary",
				Contact_.phone1.getName() + "." + Phone_.primaryPhone.getName());

		overviewForm.newLine();
		overviewForm.bindTextField("Phone 2", "phone2.phoneNo");
		overviewForm.bindEnumField(null, "phone2.phoneType", PhoneType.class);
		this.primaryPhone2 = overviewForm.bindBooleanField("Primary", "phone2.primaryPhone");

		overviewForm.newLine();
		overviewForm.bindTextField("Phone 3", "phone3.phoneNo");
		overviewForm.bindEnumField(null, "phone3.phoneType", PhoneType.class);
		this.primaryPhone3 = overviewForm.bindBooleanField("Primary", "phone3.primaryPhone");

		overviewForm.colspan(3);
		overviewForm.bindTextField("Street", "location.street");
		overviewForm.newLine();
		overviewForm.colspan(3);
		overviewForm.bindTextField("City", "location.city");
		overviewForm.newLine();
		overviewForm.bindTextField("State", "location.state");

		overviewForm.bindTextField("Postcode", "location.postcode");

		this.primaryPhone1.addValueChangeListener(new PhoneChangeListener());
		this.primaryPhone2.addValueChangeListener(new PhoneChangeListener());
		this.primaryPhone3.addValueChangeListener(new PhoneChangeListener());

		layout.addComponent(overviewForm);

		return layout;
	}

	@Override
	public void enter(final ViewChangeEvent event)
	{
		final JPAContainer<Organisation> container = new DaoFactory().getOrganisationDao().createVaadinContainer();
		container.sort(new String[]
		{ Organisation_.name.getName() }, new boolean[]
		{ true });

		final Builder<Organisation> builder = new HeadingPropertySet.Builder<Organisation>();
		builder.addColumn("Organisation", Organisation_.name).addColumn("Phone", Organisation.PRIMARY_PHONE);

		super.init(Organisation.class, container, builder.build());
	}

	@Override
	protected Filter getContainerFilter(final String filterString, final boolean advancedSearchActive)
	{
		return new Or(
				new Or(new Or(new SimpleStringFilter(Organisation_.name.getName(), filterString, true, false),
						new SimpleStringFilter(Organisation_.phone1, filterString, true, false)),
						new SimpleStringFilter(Organisation_.phone2, filterString, true, false)),
				new SimpleStringFilter(Organisation_.phone3, filterString, true, false));
	}

	@Override
	protected boolean interceptAction(final CrudAction<Organisation> action, final EntityItem<Organisation> entity)
	{
		boolean allow = true;
		if (action instanceof CrudActionDelete)
		{
			if (entity.getEntity().isOurScoutGroup())
			{
				allow = false;
				SMNotification.show("You can't delete your own Scout Group.", Type.ERROR_MESSAGE);
			}
		}
		return allow;
	}

	@Override
	protected String getTitleText()
	{
		return "Organisations";
	}

	public class PhoneChangeListener implements ValueChangeListener
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(final ValueChangeEvent event)
		{
			final CheckBox property = (CheckBox) event.getProperty();
			final Boolean value = property.getValue();
			if (value != null)
			{

				if (property == OrganisationView.this.primaryPhone1 && value == true)
				{
					OrganisationView.this.primaryPhone2.setValue(false);
					OrganisationView.this.primaryPhone3.setValue(false);
				}
				else if (property == OrganisationView.this.primaryPhone2 && value == true)
				{
					OrganisationView.this.primaryPhone1.setValue(false);
					OrganisationView.this.primaryPhone3.setValue(false);
				}
				else if (property == OrganisationView.this.primaryPhone3 && value == true)
				{
					OrganisationView.this.primaryPhone2.setValue(false);
					OrganisationView.this.primaryPhone1.setValue(false);
				}
			}
		}
	}

}
