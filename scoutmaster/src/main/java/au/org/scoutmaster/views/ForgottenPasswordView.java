package au.org.scoutmaster.views;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

import au.org.scoutmaster.application.Menu;
import au.org.scoutmaster.util.RandomString;

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;


@Menu (display="Forgotten Password", path="MenuBar.Admin")
public class ForgottenPasswordView extends CustomComponent implements View, Button.ClickListener
{
	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(ForgottenPasswordView.class);
	
	public static final String NAME = "Forgotten";

	private final TextField user;

	private final Button retrieveButton;

	public ForgottenPasswordView()
	{
		setSizeFull();

		// Create the user input field
		user = new TextField("User:");
		user.setWidth("300px");
		user.setRequired(true);
		user.setInputPrompt("Your username (eg. joe@email.com)");
		user.addValidator(new EmailValidator("Username must be an email address"));
		user.setInvalidAllowed(false);

		// Create login button
		retrieveButton = new Button("Retrieve", this);

		// Add both to a panel
		VerticalLayout fields = new VerticalLayout(user, retrieveButton);
		fields.setCaption("Enter your email address to retrieve your password. (e.g. test@test.com)");
		fields.setSpacing(true);
		fields.setMargin(new MarginInfo(true, true, true, false));
		fields.setSizeUndefined();

		// The view root layout
		VerticalLayout viewLayout = new VerticalLayout(fields);
		viewLayout.setSizeFull();
		viewLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
		viewLayout.setStyleName(Reindeer.LAYOUT_BLUE);
		setCompositionRoot(viewLayout);
	}

	@Override
	public void enter(ViewChangeEvent event)
	{
		// focus the username field when user arrives to the login view
		user.focus();
	}

	
	@Override
	public void buttonClick(ClickEvent event)
	{
		String username = user.getValue();

		Email email = new SimpleEmail();
		email.setHostName("smtp.scoutmaster.org.au");
		email.setSmtpPort(465);
		email.setAuthenticator(new DefaultAuthenticator("username", "password"));
		email.setSSLOnConnect(true);
		try
		{
			email.setFrom("info@scoutmaster.org");
			email.setSubject("[Scoutmaster] Please reset your password");
			
			RandomString rs = new RandomString(RandomString.Type.ALPHANUMERIC, 32);
			String resetid = rs.nextString();
			// TODO save reset id to database with expiry date.
			
			
			//TODO get domain from db
			String domain = "scoutmaster.org";
			
			StringBuilder sb = new StringBuilder();
			sb.append("So you forgot your password, surely not :))\n");
			sb.append("Use the following link within the next 24 hours to reset your password:");
			sb.append("https://" + domain + ResetPasswordView.NAME + "?resetid=" + resetid);
			email.setMsg(sb.toString());
			email.addTo(username);
			email.send();
		}
		catch (EmailException e)
		{
			logger.error(e,e);
			Notification.show("Error sending email", e.getMessage(), Type.ERROR_MESSAGE);
		}
	}
}