package au.org.scoutmaster.views;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import au.org.scoutmaster.dao.ContactDao;
import au.org.scoutmaster.dao.TagDao;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Tag;
import au.org.scoutmaster.filter.EntityManagerProvider;

import com.vaadin.ui.Notification;

public class ContactTokenFieldTest
{

	private EntityManagerFactory entityManagerFactory;
	private EntityManager em;

	@Before
	public void init() throws ServletException
	{

		entityManagerFactory = Persistence.createEntityManagerFactory("scoutmastertest");
		em = entityManagerFactory.createEntityManager();
		EntityManagerProvider.INSTANCE.setCurrentEntityManager(em);
	}

	@After
	public void finalise()
	{
		em.close();
	}


	@Test
	public void test()
	{
		String tokenName = "test";
		Contact contact = new Contact();

		TagDao daoTag = new TagDao(em);
		Tag tag = daoTag.findByName(tokenName);
		if (tag != null)
		{

			// Now check if the Tag is already associated with the contact
			ContactDao daoContact = new ContactDao();
			if (daoContact.hasTag((Contact) contact, tag))
				Notification.show(tag.getId() + " is already associated with this contact");
			else
			{
				daoContact.attachTag((Contact) contact, tag);
				daoContact.persist((Contact) contact);
			}
		}
		else
		{
			tag = new Tag(tokenName);
			// TagEditor<T> editor = new TagEditor<>(contact, tag, this);
		}

	}


}
