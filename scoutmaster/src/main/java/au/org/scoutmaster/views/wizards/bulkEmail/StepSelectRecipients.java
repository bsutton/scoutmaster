package au.org.scoutmaster.views.wizards.bulkEmail;

import java.util.ArrayList;

import org.vaadin.teemu.wizards.WizardStep;

import au.com.vaadinutils.crud.HeadingPropertySet;
import au.com.vaadinutils.crud.HeadingPropertySet.Builder;
import au.org.scoutmaster.dao.ContactDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.views.SearchableContactTable;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

public class StepSelectRecipients implements WizardStep
{

	private final SearchableContactTable contactTable;
	private final VerticalLayout layout;

	public StepSelectRecipients(final BulkEmailWizardView messagingWizardView)
	{
		this.layout = new VerticalLayout();
		this.layout.setMargin(true);
		this.layout.setSizeFull();

		final ContactDao daoContact = new DaoFactory().getContactDao();

		final JPAContainer<Contact> contactContainer = daoContact.createVaadinContainer();

		final Builder<Contact> builder = new HeadingPropertySet.Builder<Contact>();
		builder.addColumn("First Name", Contact_.firstname).addColumn("Lastname", Contact_.lastname)
		.addColumn("Birth Date", Contact_.birthDate).addColumn("Section", Contact_.section)
		.addColumn("Email", Contact_.homeEmail);

		this.contactTable = new SearchableContactTable(contactContainer, builder.build());
		this.contactTable.excludeDoNotSendBulkCommunications(true);
		this.layout.addComponent(this.contactTable);

	}

	@Override
	public String getCaption()
	{
		return "Select Recipients";
	}

	@Override
	public Component getContent()
	{

		return this.layout;
	}

	@Override
	public boolean onAdvance()
	{
		boolean advance = true;
		if (this.contactTable.size() == 0)
		{
			advance = false;
			Notification.show("You must select at least one recipient.");
		}
		return advance;
	}

	@Override
	public boolean onBack()
	{
		return true;
	}

	public ArrayList<Contact> getRecipients()
	{
		return this.contactTable.getFilteredContacts();
	}

	public int getRecipientCount()
	{
		return this.contactTable.size();
	}
}
