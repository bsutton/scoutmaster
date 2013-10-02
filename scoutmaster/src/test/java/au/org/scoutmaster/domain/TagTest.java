package au.org.scoutmaster.domain;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import au.com.vaadinutils.dao.EntityManagerProvider;
import au.org.scoutmaster.dao.ContactDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.TagDao;

import com.vaadin.ui.Notification;

public class TagTest
{

	private EntityManagerFactory entityManagerFactory;
	private EntityManager em;

	@Before
	public void init() throws ServletException
	{

		entityManagerFactory = Persistence.createEntityManagerFactory("scoutmastertest");
		em = entityManagerFactory.createEntityManager();
		EntityManagerProvider.setCurrentEntityManager(em);
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

		TagDao daoTag = new DaoFactory(em).getTagDao();
		Tag tag = daoTag.findByName(tokenName);
		if (tag != null)
		{

			// Now check if the Tag is already associated with the contact
			ContactDao daoContact = new DaoFactory().getContactDao();
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
