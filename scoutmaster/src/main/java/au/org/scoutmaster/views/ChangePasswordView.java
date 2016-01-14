package au.org.scoutmaster.views;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

import au.com.vaadinutils.listener.ClickEventLogged;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.access.UserDao;
import au.org.scoutmaster.domain.access.User;
import au.org.scoutmaster.validator.PasswordValidator;

@Menu(display = "Change Password", path = "Admin")
public class ChangePasswordView extends CustomComponent implements View, Button.ClickListener
{
	private static final long serialVersionUID = 1L;

	public static final String NAME = "ChangePassword";

	private final PasswordField password;

	private final PasswordField confirm;

	private final Button changeButton;

	private Button cancelButton;

	public ChangePasswordView()
	{
		setSizeFull();

		// Create the password input field
		this.password = new PasswordField("Password:");
		this.password.setWidth("300px");
		this.password.addValidator(new PasswordValidator("Password"));
		this.password.setRequired(true);
		this.password.setValue("");
		this.password.setNullRepresentation("");

		// Create the password input field
		this.confirm = new PasswordField("Confirm Password:");
		this.confirm.setWidth("300px");
		this.confirm.addValidator(new PasswordValidator("Confirm Password"));
		this.confirm.setRequired(true);
		this.confirm.setValue("");
		this.confirm.setNullRepresentation("");

		this.cancelButton = new Button("Cancel", (ClickListener) event -> {
			getUI().getNavigator().navigateTo(ContactView.NAME);

		});

		// Create change password button
		this.changeButton = new Button("Change", new ClickEventLogged.ClickAdaptor(this));
		this.changeButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		this.changeButton.setClickShortcut(KeyCode.ENTER);

		HorizontalLayout buttons;
		buttons = new HorizontalLayout(this.cancelButton, this.changeButton);
		buttons.setSpacing(true);

		// Add both to a panel
		final Label title = new Label("<b>Please enter your new password.</b>");
		title.setContentMode(ContentMode.HTML);
		final VerticalLayout fields = new VerticalLayout(title, this.password, this.confirm, buttons);

		fields.setSpacing(true);
		fields.setMargin(new MarginInfo(true, true, true, false));
		fields.setSizeUndefined();

		// The view root layout
		final VerticalLayout viewLayout = new VerticalLayout(fields);
		viewLayout.setSizeFull();
		viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
		viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
		setCompositionRoot(viewLayout);
	}

	@Override
	public void enter(final ViewChangeEvent event)
	{
		// focus the username field when user arrives to the login view
		this.password.focus();
	}

	@Override
	public void buttonClick(final ClickEvent event)
	{

		//
		// Validate the fields using the navigator. By using validators for the
		// fields we reduce the amount of queries we have to make to the
		// database
		// for wrongly entered passwords
		//
		if (!this.password.isValid())
		{
			return;
		}

		final String password = this.password.getValue();

		//
		// Validate username and password with database here. For examples sake
		// I use a dummy username and password.
		//
		if (!password.equals(this.confirm.getValue()))
		{
			Notification.show("The password and confirm passwords do not match", Type.ERROR_MESSAGE);
			this.password.focus();
		}
		else
		{
			final UserDao daoUser = new DaoFactory().getUserDao();

			final User user = SMSession.INSTANCE.getLoggedInUser();

			daoUser.updatePassword(user, password);

			// Navigate to main view
			getUI().getNavigator().navigateTo(ContactView.NAME);

		}
	}
}