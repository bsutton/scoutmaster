package au.org.scoutmaster.views;

import org.apache.log4j.Logger;

import au.com.vaadinutils.listener.ClickEventLogged;
import au.com.vaadinutils.listener.CompleteListener;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.ForgottenPasswordResetDao;
import au.org.scoutmaster.dao.SMTPSettingsDao;
import au.org.scoutmaster.dao.access.UserDao;
import au.org.scoutmaster.domain.ForgottenPasswordReset;
import au.org.scoutmaster.domain.SMTPServerSettings;
import au.org.scoutmaster.domain.access.User;
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
	private static Logger logger = Logger.getLogger(ForgottenPasswordView.class);

	public static final String NAME = "Forgotten";

	private final TextField emailAddress;

	private final Button retrieveButton;

	private VerticalLayout viewLayout;

	private Label sentLabel;

	public ForgottenPasswordView()
	{
		setSizeFull();

		// Create the user input field
		emailAddress = new TextField("Email Address");
		emailAddress.setWidth("300px");
		emailAddress.setRequired(true);
		emailAddress.setInputPrompt("Your email address.");
		emailAddress.addValidator(new EmailValidator("Enter a valid email address."));
		emailAddress.setInvalidAllowed(false);

		// Create login button
		retrieveButton = new Button("Reset", new ClickEventLogged.ClickAdaptor(this));
		retrieveButton.addStyleName(Reindeer.BUTTON_DEFAULT);
		retrieveButton.setClickShortcut(KeyCode.ENTER);

		// Add both to a panel
		VerticalLayout fields = new VerticalLayout(emailAddress, retrieveButton);
		fields.setCaption("Enter your email address to reset your password. (e.g. test@test.com)");
		fields.setSpacing(true);
		fields.setMargin(new MarginInfo(true, true, true, false));
		fields.setSizeUndefined();
		sentLabel = new Label("<b>An email has been sent with details on resetting your password.</b>");
		sentLabel.setContentMode(ContentMode.HTML);
		fields.addComponent(sentLabel);
		sentLabel.setVisible(false);


		viewLayout = new VerticalLayout(fields);
		viewLayout.setSizeFull();
		viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
		viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
		setCompositionRoot(viewLayout);
	}

	@Override
	public void enter(ViewChangeEvent event)
	{
		// focus the username field when user arrives to the login view
		emailAddress.focus();
	}

	@Override
	public void buttonClick(ClickEvent event)
	{
		// just in case the user goes around a second time.
		sentLabel.setVisible(false);
		String emailAddressValue = emailAddress.getValue();

		UserDao userDao = new DaoFactory().getUserDao();
		User user = userDao.findByEmail(emailAddressValue);
		if (user == null)
		{
			SMNotification.show("The entered email address does not exist.", Type.WARNING_MESSAGE);
		}
		else
		{
			SendEmailWorkingDialog dialog = new SendEmailWorkingDialog("Sending",
					"Sending a reset link to you via email.<br> It should arrive in a few moments.");
			SMTPSettingsDao settingsDao = new DaoFactory().getSMTPSettingsDao();
			SMTPServerSettings settings = settingsDao.findSettings();

			dialog.setFrom(settings.getFromEmailAddress());
			dialog.setSubject("[Scoutmaster] Password reset request");

			ForgottenPasswordResetDao forgottenPasswordResetDao = new DaoFactory().getForgottenPasswordResetDao();
			ForgottenPasswordReset reset = forgottenPasswordResetDao.createReset(emailAddressValue);

			forgottenPasswordResetDao.persist(reset);

			String url = VaadinServletService.getCurrentServletRequest().getRequestURL().toString();
			
			// The click event seesm to come from a vaading PUSH service so we need to remove the /PUSH/ from the path.
			if (url.contains("/PUSH"))
			{
				int pushStartIndex = url.indexOf("/PUSH/");
				int pushEndIndex = pushStartIndex + 6;
				url = url.substring(0, pushStartIndex) + url.substring(pushEndIndex);
			}

			StringBuilder sb = new StringBuilder();
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
		sentLabel.setVisible(true);
	}
}