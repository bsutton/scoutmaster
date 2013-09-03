package au.org.scoutmaster.views;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

import au.com.vaadinutils.listener.ClickAdaptorLogged;
import au.com.vaadinutils.menu.Menu;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.EMailServerSettingsDao;
import au.org.scoutmaster.dao.ForgottenPasswordResetDao;
import au.org.scoutmaster.domain.EMailServerSettings;
import au.org.scoutmaster.domain.ForgottenPasswordReset;
import au.org.scoutmaster.util.SMNotification;

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinServletService;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
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

	private final TextField emailAddress;

	private final Button retrieveButton;

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
		retrieveButton = new Button("Retrieve", new ClickAdaptorLogged(this));

		// Add both to a panel
		VerticalLayout fields = new VerticalLayout(emailAddress, retrieveButton);
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
		emailAddress.focus();
	}

	
	@Override
	public void buttonClick(ClickEvent event)
	{
		String emailAddressValue = emailAddress.getValue();
		
		EMailServerSettingsDao settingsDao = new DaoFactory().getEMailServerSettingsDao();
		EMailServerSettings settings = settingsDao.findSettings();

		Email email = new SimpleEmail();
		email.setHostName(settings.getSmtpFQDN());
		email.setSmtpPort(settings.getSmtpPort());
		if (settings.isAuthRequired())
			email.setAuthenticator(new DefaultAuthenticator(settings.getUsername(), settings.getPassword()));
		email.setSSLOnConnect(true);
		try
		{
			email.setFrom(settings.getFromEmailAddress());
			email.setSubject("[Scoutmaster] Password reset request");
			
			ForgottenPasswordResetDao forgottenPasswordResetDao= new DaoFactory().getForgottenPasswordResetDao();
			ForgottenPasswordReset reset =forgottenPasswordResetDao.createReset(emailAddressValue);
			
			forgottenPasswordResetDao.persist(reset);
			
			StringBuffer url = VaadinServletService.getCurrentServletRequest().getRequestURL();
			
			StringBuilder sb = new StringBuilder();
			sb.append("So you forgot your password, surely not :))\n");
			sb.append("Use the following link within the next 24 hours to reset your password:");
			sb.append(url + "/" + ResetPasswordView.NAME + "?resetid=" + reset.getResetid());
			email.setMsg(sb.toString());
			email.addTo(emailAddressValue);
			email.send();
		}
		catch (IllegalArgumentException e)
		{
			SMNotification.show(e, Type.ERROR_MESSAGE);
		}
		catch (EmailException e)
		{
			logger.error(e,e);
			SMNotification.show(e, Type.ERROR_MESSAGE);
		}
	}
}