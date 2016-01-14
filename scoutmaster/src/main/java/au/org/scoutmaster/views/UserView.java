package au.org.scoutmaster.views;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import au.com.vaadinutils.crud.BaseCrudView;
import au.com.vaadinutils.crud.CrudAction;
import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.com.vaadinutils.help.HelpProvider;
import au.com.vaadinutils.menu.Menu;
import au.com.vaadinutils.validator.MobilePhoneValidator;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.domain.access.User_;
import au.org.scoutmaster.help.HelpPageIdentifier;
import au.org.scoutmaster.util.SMMultiColumnFormLayout;
import au.org.scoutmaster.validator.PasswordValidator;
import au.org.scoutmaster.views.actions.UserActionInviteUser;

@Menu(display = "Users", path = "Admin.Security")
public class UserView extends BaseCrudView<User>
		implements View, Selected<User>, TextChangeListener, FocusListener, HelpProvider
{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(UserView.class);

	public static final String NAME = "User";

	private PasswordField password;

	private PasswordField confirmPassword;

	private boolean passwordChanged = false;

	@Override
	protected AbstractLayout buildEditor(final ValidatingFieldGroup<User> fieldGroup2)
	{
		final VerticalLayout layout = new VerticalLayout();

		final SMMultiColumnFormLayout<User> overviewForm = new SMMultiColumnFormLayout<User>(1, this.fieldGroup);
		overviewForm.setColumnFieldWidth(0, 380);
		overviewForm.setColumnLabelWidth(0, 100);
		overviewForm.setSizeFull();

		overviewForm.bindTextField("Username", User_.username);
		this.password = overviewForm.addPasswordField("Password");
		this.password.addTextChangeListener(this);
		this.password.addFocusListener(this);
		this.confirmPassword = overviewForm.addPasswordField("Confirm Password");
		this.confirmPassword.addFocusListener(this);
		this.confirmPassword.addTextChangeListener(this);
		overviewForm.bindBooleanField("Enabled", User_.enabled);
		final TextField emailAddress = overviewForm.bindTextField("Email Address", User_.emailAddress);
		emailAddress.addValidator(new com.vaadin.data.validator.EmailValidator("Enter a valid Email Address."));
		layout.addComponent(overviewForm);
		overviewForm.bindTextField("Firstname", User_.firstname);
		overviewForm.bindTextField("Lastname", User_.lastname);
		final TextField mobile = overviewForm.bindTextField("Sender Mobile", User_.senderMobile);
		mobile.addValidator(new MobilePhoneValidator("Enter a valid Mobile No."));
		mobile.setDescription("Used when sending bulk emails as the sender phone no.");
		overviewForm.bindEditorField(User_.emailSignature, false);

		return layout;
	}

	@Override
	protected void interceptSaveValues(final EntityItem<User> item)
	{
		if (this.passwordChanged)
		{
			item.getEntity().setPassword(this.password.getValue());
		}
	}

	@Override
	public void enter(final ViewChangeEvent event)
	{
		final JPAContainer<User> container = new DaoFactory().getUserDao().createVaadinContainer();

		container.sort(new String[]
		{ User_.username.getName() }, new boolean[]
		{ true });

		final Builder<User> builder = new HeadingPropertySet.Builder<User>();
		builder.addColumn("Username", User_.username).addColumn("Enabled", User_.enabled).addColumn("Email",
				User_.emailAddress);

		super.init(User.class, container, builder.build());
	}

	@Override
	protected void resetFilters()
	{
		super.resetFilters();
		// We must force the group filter as the user table is not multi-tenant.
		getContainer().addContainerFilter(new Compare.Equal(User_.group.getName(), SMSession.INSTANCE.getGroup()));
	}

	@Override
	protected Filter getContainerFilter(final String filterString, final boolean advancedSearchActive)
	{
		//
		return new Or(new SimpleStringFilter(User_.username.getName(), filterString, true, false),
				new SimpleStringFilter(User_.emailAddress.getName(), filterString, true, false));
	}

	@Override
	protected void formValidate() throws InvalidValueException
	{
		this.password.validate();
		this.confirmPassword.validate();
	}

	@Override
	public void rowChanged(final EntityItem<User> item)
	{
		if (item != null)
		{

			if (super.isNew())
			{
				this.password.removeAllValidators();
				this.confirmPassword.removeAllValidators();
				this.password.addValidator(new PasswordValidator("Password"));
				this.confirmPassword.addValidator(new PasswordValidator("Confirm Password"));
				this.password.setValue("");
				this.confirmPassword.setValue("");

			}
			else
			{
				// Clear the validators as we only enable them if the user
				// clicks into one of the password fields.
				this.password.removeAllValidators();
				this.confirmPassword.removeAllValidators();

				// push a value so it looks like the password is filled out.
				this.password.setValue("********");
				this.confirmPassword.setValue("********");
			}
		}
		else
		{
			this.password.setValue("");
			this.confirmPassword.setValue("");
		}
		super.rowChanged(item);
		this.passwordChanged = false;

	}

	/**
	 * One of the password fields has changed so we need to inject the
	 * validators. We only inject the validator is a user attempts to edit the
	 * password.
	 *
	 */
	@Override
	public void textChange(final TextChangeEvent event)
	{
		this.passwordChanged = true;

		this.password.removeAllValidators();
		this.confirmPassword.removeAllValidators();
		this.password.addValidator(new PasswordValidator("Password"));
		this.confirmPassword.addValidator(new PasswordValidator("Confirm Password"));

	}

	@Override
	protected List<CrudAction<User>> getCrudActions()
	{
		final List<CrudAction<User>> actions = super.getCrudActions();

		actions.add(new UserActionInviteUser());
		return actions;
	}

	@Override
	public void focus(final FocusEvent event)
	{
		if (this.passwordChanged == false)
		{
			this.password.setValue("");
			this.confirmPassword.setValue("");
		}

	}

	@Override
	protected String getTitleText()
	{
		return "Users";
	}

	@Override
	public HelpPageIdentifier getHelpId()
	{
		return HelpPageIdentifier.UserView;
	};

}
