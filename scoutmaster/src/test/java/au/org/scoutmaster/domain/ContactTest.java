package au.org.scoutmaster.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import au.org.scoutmaster.dao.ContactDao;
import au.org.scoutmaster.dao.TagDao;
import au.org.scoutmaster.filter.EntityManagerProvider;
import au.org.scoutmaster.filter.Transaction;

public class ContactTest
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
	public void testInsert()
	{
		try (Transaction t = new Transaction(em))
		{
			ContactDao daoContact = new ContactDao(em);
			Contact contact = new Contact();
			contact.setFirstname("Brett");
			contact.setLastname("Sutton");
			daoContact.addNote(contact, "My first note goes here", "The note body 1 is here");
			daoContact.addNote(contact, "My second note goes here", "The note body 2 is here");
			contact.setAddress( new Address("10 smith drv", "Sometown", "Victoria", "3000"));
			daoContact.addTag(contact, new Tag("Tag1", "The Tag1"));
			daoContact.addTag(contact, new Tag("Tag2", "The Tag2"));

			em.persist(contact);
			t.commit();
			t.begin();

			// Check the contact notes and Tags exists
			Assert.assertTrue(contactExists("Brett", "Sutton"));
			Assert.assertTrue(noteExists("Brett", "Sutton", "My first note goes here"));
			Assert.assertTrue(noteExists("Brett", "Sutton", "My second note goes here"));
			Assert.assertTrue(addressExists("Brett", "Sutton", "10 smith drv", "Sometown", "Victoria", "3000"));
			Assert.assertTrue(tagExists("Brett", "Sutton", "Tag1"));
			Assert.assertTrue(tagExists("Brett", "Sutton", "Tag2"));

			// Delete the resulting contact
			em.remove(contact);
			t.commit();

			t.begin();
			Assert.assertFalse(contactExists("Brett", "Sutton"));
			Assert.assertTrue(Tag.findTag("Tag2") == null);
			Assert.assertTrue(Tag.findTag("Tag1") == null);
			Assert.assertTrue(Note.findNote("My first note goes here").size() == 0);
			Assert.assertTrue(Note.findNote("My second note goes here").size() == 0);
			Assert.assertTrue(Address.findAddress("10 smith drv", "Sometown", "Victoria", "3000").size() == 0);
			t.commit();

			t.begin();
			Tag tag = Tag.findTag("Tag1");
			em.remove(tag);
			tag = Tag.findTag("Tag2");
			em.remove(tag);
			t.commit();
		}
		finally
		{
		}
	}

	@Test
	public void testUpdate()
	{
		try (Transaction t = new Transaction(em))
		{
			ContactDao daoContact = new ContactDao(em);

			// create a contact
			Contact contact = new Contact();
			contact.setFirstname("Brett");
			contact.setLastname("Sutton");
			daoContact.addNote(contact, "My first note goes here", "The note body 1 is here");
			daoContact.addNote(contact, "My second note goes here", "The note body 2 is here");
			contact.setAddress(new Address("10 smith drv", "Sometown", "Victoria", "3000"));
			daoContact.addTag(contact, new Tag("Tag1", "The Tag1"));
			daoContact.addTag(contact, new Tag("Tag2", "The Tag2"));
			daoContact.persist(contact);
			t.commit();
			t.begin();

			// Check the contact notes and Tags exists
			Assert.assertTrue(contactExists("Brett", "Sutton"));
			Assert.assertTrue(noteExists("Brett", "Sutton", "My first note goes here"));
			Assert.assertTrue(noteExists("Brett", "Sutton", "My second note goes here"));
			Assert.assertTrue(addressExists("Brett", "Sutton", "10 smith drv", "Sometown", "Victoria", "3000"));
			Assert.assertTrue(tagExists("Brett", "Sutton", "Tag1"));
			Assert.assertTrue(tagExists("Brett", "Sutton", "Tag2"));

			// update the contact.
			List<Contact> list = daoContact.findAll();
			if (list.size() == 1)
			{
				Contact result = list.get(0);
				result.setFirstname("Stephen");
				result.setMiddlename("Bret");
				daoContact.addNote(contact, "Note 3", "Note 3 body");
				daoContact.detachTag(contact, "Tag2");
				daoContact.addTag(contact, new Tag("Tag3", "The Tag3"));
				contact.setAddress(new Address("20 replacement drv", "Othertown", "Victoria", "3000"));
				daoContact.persist(result);
			}
			t.commit();
			t.begin();

			// Check the contact notes and Tags exists
			Assert.assertTrue(contactExists("Stephen", "Sutton"));
			Assert.assertTrue(noteExists("Stephen", "Sutton", "My first note goes here"));
			Assert.assertTrue(noteExists("Stephen", "Sutton", "My second note goes here"));
			Assert.assertTrue(addressExists("Stephen", "Sutton", "20 replacement drv", "Othertown", "Victoria", "3000"));
			Assert.assertTrue(tagExists("Stephen", "Sutton", "Tag1"));
			Assert.assertFalse(tagExists("Stephen", "Sutton", "Tag2"));
			Assert.assertTrue(tagExists("Stephen", "Sutton", "Tag3"));
			t.commit();
			t.begin();

			// Now cleanup
			daoContact.remove(contact);
			t.commit();
			t.begin();

			Assert.assertTrue(!contactExists("Stephen", "Sutton"));
			Assert.assertTrue(Note.findNote("My first note goes here").size() == 0);
			Assert.assertTrue(Note.findNote("My second note goes here").size() == 0);
			Assert.assertTrue(Address.findAddress("20 replacement drv", "Othertown", "Victoria", "3000").size() == 0);
			// tags exists even after they are detached from the contact.
			Assert.assertTrue(Tag.findTag("Tag1") != null);
			Assert.assertTrue(Tag.findTag("Tag2") != null);
			Assert.assertTrue(Tag.findTag("Tag2") != null);
			t.commit();

			Organisation org = new Organisation();
			org.setName("Heidelberg Scouts");
			org.setLocation(new Address("31 Outhwaite Rd", "Heidelberg Heights", "Victoria", "3081"));
			Tag tag4 = TagDao.addTag("Tag4", "Yet another tag");
			org.addTag(tag4);
			Tag tag2 = Tag.findTag("Tag2");
			org.addTag(tag2);

		}
		finally
		{
		}
	}

	@Test
	public void testJPAInsertAddress()
	{
		try (Transaction t = new Transaction(em))
		{
			ContactDao daoContact = new ContactDao(em);
			Contact contact = new Contact();
			contact.setFirstname("Rhiannon");
			contact.setLastname("Sutton");
			daoContact.persist(contact);
			t.commit();
			t.begin();

			// Check the contact notes and Tags exists
			Assert.assertTrue(contactExists("Rhiannon", "Sutton"));
			List<Contact> foundContacts = daoContact.findContactByName("Rhiannon", "Sutton");
			Assert.assertTrue(foundContacts.size() == 1);
			contact = foundContacts.get(0);
			Assert.assertTrue(contact.getNotes().size() == 0);
			Assert.assertTrue(contact.getAddress() == null);
			Assert.assertTrue(contact.getTags().size() == 0);

			contact.setMiddlename("Paige");
			contact.setAddress(new Address("10 Mossman Drv", "Eaglemont", "Victoria", "3084"));
			daoContact.persist(contact);
			t.commit();
			t.begin();

			// Check the contact notes and Tags exists
			Assert.assertTrue(contactExists("Rhiannon", "Sutton"));
			foundContacts = daoContact.findContactByName("Rhiannon", "Sutton");
			Assert.assertTrue(foundContacts.size() == 1);
			contact = foundContacts.get(0);

			Assert.assertEquals("Paige", contact.getMiddlename());
			Assert.assertTrue(addressExists("Rhiannon", "Sutton", "10 Mossman Drv", "Eaglemont", "Victoria", "3084"));
			Assert.assertTrue(contact.getNotes().size() == 0);
			Assert.assertTrue(contact.getTags().size() == 0);

			// Now cleanup
			daoContact.remove(contact);
			t.commit();
			t.begin();

			Assert.assertFalse(contactExists("Rhiannon", "Sutton"));
			Assert.assertTrue(Tag.findTag("Tag2") == null);
			Assert.assertTrue(Tag.findTag("Tag1") == null);
			Assert.assertTrue(Note.findNote("My first note goes here").size() == 0);
			Assert.assertTrue(Note.findNote("My second note goes here").size() == 0);
			Assert.assertTrue(Address.findAddress("10 Mossman Drv", "Eaglemont", "Victoria", "3084").size() == 0);
			t.commit();
		}
		finally
		{
		}
	}

	@Test
	public void testJPAAddNote()
	{
		try (Transaction t = new Transaction(em))
		{
			ContactDao daoContact = new ContactDao(em);
			Contact contact = new Contact();
			contact.setFirstname("Tristan");
			contact.setLastname("Sutton");
			daoContact.persist(contact);

			// Check the contact notes and Tags exists
			Assert.assertTrue(contactExists("Tristan", "Sutton"));
			List<Contact> foundContacts = daoContact.findContactByName("Tristan", "Sutton");
			Assert.assertTrue(contact.getNotes().size() == 0);
			Assert.assertTrue(contact.getAddress() == null);
			Assert.assertTrue(contact.getTags().size() == 0);

			Query query = em.createNamedQuery("Contact.findByName");
			query.setParameter("firstname", "Tristan");
			query.setParameter("lastname", "Sutton");
			Contact result = (Contact) query.getSingleResult();
			if (result != null)
			{
				result.setMiddlename("Ryan");
				daoContact.addNote(result, "My first note goes here", "The note body 1 is here");
				daoContact.addNote(result, "My second note goes here", "The note body 2 is here");
			}
			em.persist(result);

			// Check the contact notes exists
			Assert.assertTrue(contactExists("Tristan", "Sutton"));
			foundContacts = daoContact.findContactByName("Tristan", "Sutton");
			Assert.assertTrue(foundContacts.size() == 1);
			contact = foundContacts.get(0);
			Assert.assertEquals("Ryan", contact.getMiddlename());
			Assert.assertTrue(noteExists("Tristan", "Sutton", "My first note goes here"));
			Assert.assertTrue(noteExists("Tristan", "Sutton", "My second note goes here"));

			// check that we have no address or tag.
			Assert.assertTrue(contact.getAddress() == null);
			Assert.assertTrue(contact.getTags().size() == 0);

			em.remove(contact);
			em.remove(result);
			t.commit();

		}
		finally
		{
		}
	}

	private boolean tagExists(String firstname, String lastname, String tagName)
	{
		boolean exists = false;

		ContactDao daoContact = new ContactDao(em);

		List<Contact> foundContacts = daoContact.findContactByName(firstname, lastname);
		Assert.assertTrue(foundContacts.size() == 1);
		Contact contact = foundContacts.get(0);
		Tag tag = contact.getTag(tagName);
		if (tag != null)
			exists = true;

		return exists;
	}

	private boolean addressExists(String firstname, String lastname, String street, String suburb, String state,
			String postcode)
	{
		boolean exists = false;

		ContactDao daoContact = new ContactDao(em);

		List<Contact> foundContacts = daoContact.findContactByName(firstname, lastname);
		Assert.assertTrue(foundContacts.size() == 1);
		Contact contact = foundContacts.get(0);
		Address address = contact.getAddress();
		if (address != null && address.getStreet().equals(street) && address.getCity().equals(suburb)
				&& address.getState().equals(state) && address.getPostcode().equals(postcode))
		{
			exists = true;
		}

		return exists;
	}

	private boolean noteExists(String firstname, String lastname, String noteSubject)
	{
		boolean exists = false;

		ContactDao daoContact = new ContactDao(em);

		List<Contact> foundContacts = daoContact.findContactByName(firstname, lastname);
		Assert.assertTrue(foundContacts.size() == 1);
		Contact contact = foundContacts.get(0);
		Note note = daoContact.getNote(contact, noteSubject);
		if (note != null)
			exists = true;

		return exists;
	}

	private boolean contactExists(String firstname, String lastname)
	{
		ContactDao daoContact = new ContactDao(em);

		List<Contact> foundContacts = daoContact.findContactByName(firstname, lastname);
		return foundContacts.size() == 1;
	}

}
