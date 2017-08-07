package au.org.scoutmaster.views;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.com.vaadinutils.listener.ClickEventLogged;
import au.com.vaadinutils.listener.CompleteListener;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.ForgottenPasswordResetDao;
import au.org.scoutmaster.dao.SMTPSettingsDao;
import au.org.scoutmaster.dao.security.UserDao;
import au.org.scoutmaster.domain.ForgottenPasswordReset;
import au.org.scoutmaster.domain.SMTPServerSetting;
import au.org.scoutmaster.domain.security.User;
import au.org.scoutmaster.ui.SendEmailWorkingDialog;
import au.org.scoutmaster.util.SMNotification;

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinServletService;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class ForgottenPasswordView extends CustomComponent implements View, Button.ClickListener, CompleteListener
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(ForgottenPasswordView.class);

	public static final String NAME = "Forgotten";

	private final TextField emailAddress;

	private final Button retrieveButton;

	private final VerticalLayout viewLayout;

	private final Label sentLabel;

	public ForgottenPasswordView()
	{
		setSizeFull();

		// Create the user input field
		this.emailAddress = new TextField("Email Address");
		this.emailAddress.setWidth("300px");
		this.emailAddress.setRequired(true);
		this.emailAddress.setInputPrompt("Your email address.");
		this.emailAddress.addValidator(new EmailValidator("Enter a valid email address."));
		this.emailAddress.setInvalidAllowed(false);

		// Create login button
		this.retrieveButton = new Button("Reset", new ClickEventLogged.ClickAdaptor(this));
		this.retrieveButton.addStyleName(Reindeer.BUTTON_DEFAULT);
		this.retrieveButton.setClickShortcut(KeyCode.ENTER);

		// Add both to a panel
		final VerticalLayout fields = new VerticalLayout(this.emailAddress, this.retrieveButton);
		fields.setCaption("Enter your email address to reset your password. (e.g. test@test.com)");
		fields.setSpacing(true);
		fields.setMargin(new MarginInfo(true, true, true, false));
		fields.setSizeUndefined();
		this.sentLabel = new Label("<b>An email has been sent with details on resetting your password.</b>");
		this.sentLabel.setContentMode(ContentMode.HTML);
		fields.addComponent(this.sentLabel);
		this.sentLabel.setVisible(false);

		this.viewLayout = new VerticalLayout(fields);
		this.viewLayout.setSizeFull();
		this.viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
		this.viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
		setCompositionRoot(this.viewLayout);
	}

	@Override
	public void enter(final ViewChangeEvent event)
	{
		// focus the username field when user arrives to the login view
		this.emailAddress.focus();
	}

	@Override
	public void buttonClick(final ClickEvent event)
	{
		// just in case the user goes around a second time.
		this.sentLabel.setVisible(false);
		final String emailAddressValue = this.emailAddress.getValue();

		final UserDao userDao = new DaoFactory().getUserDao();
		final User user = userDao.findByEmail(emailAddressValue);
		if (user == null)
		{
			SMNotification.show("The entered email address does not exist.", Type.WARNING_MESSAGE);
		}
		else
		{
			final SendEmailWorkingDialog dialog = new SendEmailWorkingDialog("Sending",
					"Sending a reset link to you via email.<br> It should arrive in a few moments.");
			final SMTPSettingsDao settingsDao = new DaoFactory().getSMTPSettingsDao();
			final SMTPServerSetting settings = settingsDao.findSettings();

			dialog.setFrom(settings.getFromEmailAddress());
			dialog.setSubject("[Scoutmaster] Password reset request");

			final ForgottenPasswordResetDao forgottenPasswordResetDao = new DaoFactory().getForgottenPasswordResetDao();
			final ForgottenPasswordReset reset = forgottenPasswordResetDao.createReset(emailAddressValue);

			forgottenPasswordResetDao.persist(reset);

			String url = VaadinServletService.getCurrentServletRequest().getRequestURL().toString();

			// The click event seems to come from a vaading PUSH service so we
			// need to remove the /PUSH/ from the path.
			if (url.contains("/PUSH"))
			{
				final int pushStartIndex = url.indexOf("/PUSH/");
				final int pushEndIndex = pushStartIndex + 6;
				url = url.substring(0, pushStartIndex) + url.substring(pushEndIndex);
			}

			final StringBuilder sb = new StringBuilder();
			sb.append("So you forgot your password, surely not :))\n\n");
			sb.append("Use the following link within the next 24 hours to reset your password:\n");
			sb.append(url + "/?resetid=" + reset.getResetid() + "#!" + ResetPasswordView.NAME);
			dialog.setMessage(sb.toString());
			dialog.addTo(emailAddressValue);
			dialog.setCompleteListener(this);
			dialog.send();

		}
	}

	@Override
	public void complete()
	{
		this.sentLabel.setVisible(true);
	}
}