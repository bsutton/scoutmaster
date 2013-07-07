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

import au.org.scoutmaster.domain.Address;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Note;
import au.org.scoutmaster.domain.Organisation;
import au.org.scoutmaster.domain.Tag;
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
			Contact contact = new Contact();
			contact.setFirstname("Brett");
			contact.setLastname("Sutton");
			contact.addNote("My first note goes here", "The note body 1 is here");
			contact.addNote("My second note goes here", "The note body 2 is here");
			contact.setAddress(new Address("10 smith drv", "Sometown", "Victoria", "3000"));
			contact.addTag(new Tag("Tag1", "The Tag1"));
			contact.addTag(new Tag("Tag2", "The Tag2"));

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

			// create a contact
			Contact contact = new Contact();
			contact.setFirstname("Brett");
			contact.setLastname("Sutton");
			contact.addNote("My first note goes here", "The note body 1 is here");
			contact.addNote("My second note goes here", "The note body 2 is here");
			contact.setAddress(new Address("10 smith drv", "Sometown", "Victoria", "3000"));
			contact.addTag(new Tag("Tag1", "The Tag1"));
			contact.addTag(new Tag("Tag2", "The Tag2"));
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

			// update the contact.
			Query query = em.createNamedQuery("Contact.findAll");
			Contact result = (Contact) query.getSingleResult();
			if (result != null)
			{
				result.setFirstname("Stephen");
				result.setMiddleName("Bret");
				result.addNote("Note 3", "Note 3 body");
				result.detachTag("Tag2");
				result.addTag(new Tag("Tag3", "The Tag3"));
				result.setAddress(new Address("20 replacement drv", "Othertown", "Victoria", "3000"));
			}
			em.persist(result);
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
			em.remove(contact);
			em.remove(result);
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
			Tag tag4 = Tag.addTag("Tag4", "Yet another tag");
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
			Contact contact = new Contact();
			contact.setFirstname("Rhiannon");
			contact.setLastname("Sutton");
			em.persist(contact);
			t.commit();
			t.begin();

			// Check the contact notes and Tags exists
			Assert.assertTrue(contactExists("Rhiannon", "Sutton"));
			List<Contact> foundContacts = Contact.findContactByName("Rhiannon", "Sutton");
			Assert.assertTrue(foundContacts.size() == 1);
			contact = foundContacts.get(0);
			Assert.assertTrue(contact.getNotes().size() == 0);
			Assert.assertTrue(contact.getAddress() == null);
			Assert.assertTrue(contact.getTags().size() == 0);

			Query query = em.createNamedQuery("Contact.findByName");
			query.setParameter("firstname", "Rhiannon");
			query.setParameter("lastname", "Sutton");
			Contact result = (Contact) query.getSingleResult();
			if (result != null)
			{
				result.setMiddleName("Paige");
				result.setAddress(new Address("10 Mossman Drv", "Eaglemont", "Victoria", "3084"));
			}
			em.persist(result);
			t.commit();
			t.begin();

			// Check the contact notes and Tags exists
			Assert.assertTrue(contactExists("Rhiannon", "Sutton"));
			foundContacts = Contact.findContactByName("Rhiannon", "Sutton");
			Assert.assertTrue(foundContacts.size() == 1);
			contact = foundContacts.get(0);

			Assert.assertEquals("Paige", contact.getMiddleName());
			Assert.assertTrue(addressExists("Rhiannon", "Sutton", "10 Mossman Drv", "Eaglemont", "Victoria", "3084"));
			Assert.assertTrue(contact.getNotes().size() == 0);
			Assert.assertTrue(contact.getTags().size() == 0);

			// Now cleanup
			em.remove(contact);
			em.remove(result);
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
			Contact contact = new Contact();
			contact.setFirstname("Tristan");
			contact.setLastname("Sutton");
			em.persist(contact);

			// Check the contact notes and Tags exists
			Assert.assertTrue(contactExists("Tristan", "Sutton"));
			List<Contact> foundContacts = Contact.findContactByName("Tristan", "Sutton");
			Assert.assertTrue(contact.getNotes().size() == 0);
			Assert.assertTrue(contact.getAddress() == null);
			Assert.assertTrue(contact.getTags().size() == 0);

			Query query = em.createNamedQuery("Contact.findByName");
			query.setParameter("firstname", "Tristan");
			query.setParameter("lastname", "Sutton");
			Contact result = (Contact) query.getSingleResult();
			if (result != null)
			{
				result.setMiddleName("Ryan");
				result.addNote("My first note goes here", "The note body 1 is here");
				result.addNote("My second note goes here", "The note body 2 is here");
			}
			em.persist(result);

			// Check the contact notes exists
			Assert.assertTrue(contactExists("Tristan", "Sutton"));
			foundContacts = Contact.findContactByName("Tristan", "Sutton");
			Assert.assertTrue(foundContacts.size() == 1);
			contact = foundContacts.get(0);
			Assert.assertEquals("Ryan", contact.getMiddleName());
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

		List<Contact> foundContacts = Contact.findContactByName(firstname, lastname);
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

		List<Contact> foundContacts = Contact.findContactByName(firstname, lastname);
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

		List<Contact> foundContacts = Contact.findContactByName(firstname, lastname);
		Assert.assertTrue(foundContacts.size() == 1);
		Contact contact = foundContacts.get(0);
		Note note = contact.getNote(noteSubject);
		if (note != null)
			exists = true;

		return exists;
	}

	private boolean contactExists(String firstname, String lastname)
	{
		List<Contact> foundContacts = Contact.findContactByName(firstname, lastname);
		return foundContacts.size() == 1;
	}

}
