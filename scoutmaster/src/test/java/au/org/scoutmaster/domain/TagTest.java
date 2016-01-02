package au.org.scoutmaster.domain;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.vaadin.ui.Notification;

import au.com.vaadinutils.dao.EntityManagerProvider;
import au.org.scoutmaster.dao.ContactDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.TagDao;

public class TagTest
{

	private EntityManagerFactory entityManagerFactory;
	private EntityManager em;

	@Before
	public void init() throws ServletException
	{

		this.entityManagerFactory = Persistence.createEntityManagerFactory("scoutmastertest");
		this.em = this.entityManagerFactory.createEntityManager();
		EntityManagerProvider.setCurrentEntityManager(this.em);
	}

	@After
	public void finalise()
	{
		this.em.close();
	}

	@Test
	public void test()
	{
		final String tokenName = "test";
		final Contact contact = new Contact();

		final TagDao daoTag = new DaoFactory().getTagDao();
		final Tag tag = daoTag.findByName(tokenName);
		if (tag != null)
		{

			// Now check if the Tag is already associated with the contact
			final ContactDao daoContact = new DaoFactory().getContactDao();
			if (daoContact.hasTag(contact, tag))
			{
				Notification.show(tag.getId() + " is already associated with this contact");
			}
			else
			{
				daoContact.attachTag(contact, tag);
				daoContact.persist(contact);
			}
		}

	}

}
