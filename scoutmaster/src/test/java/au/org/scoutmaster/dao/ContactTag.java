package au.org.scoutmaster.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;

import au.com.vaadinutils.dao.EntityManagerProvider;
import au.com.vaadinutils.dao.Transaction;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Tag;

public class ContactTag
{

	@Test
	public void test()
	{
		final EntityManagerFactory emf = Persistence.createEntityManagerFactory("scoutmastertest");
		EntityManagerProvider.setEntityManagerFactory(emf);

		final EntityManager em = EntityManagerProvider.createEntityManager();
		EntityManagerProvider.setCurrentEntityManager(em);

		final Transaction t = new Transaction(em);
		try
		{
			// Tag the contact
			final ContactDao daoContact = new DaoFactory().getContactDao();
			final TagDao daoTag = new DaoFactory().getTagDao();

			final Tag testTag = new Tag();
			testTag.setName("test tag");
			testTag.setDescription("A descriptoin");
			daoTag.persist(testTag);
			System.out.println(testTag.getName());

			Contact brett = new Contact();
			brett.setFirstname("brett");
			brett.setLastname("sutton");
			daoContact.persist(brett);

			daoContact.attachTag(brett, testTag);
			t.commit();

			t.begin();
			final List<Contact> contacts = daoContact.findByName("brett", "sutton");
			brett = contacts.get(0);
			for (final Tag tag : brett.getTags())
			{
				System.out.println(tag.getName());
			}

		}
		finally
		{

		}

	}

}
