package au.com.noojee.scouts.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import junit.framework.Assert;

import org.junit.Test;

import com.vaadin.addon.jpacontainer.EntityProvider;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;

public class ContactTest
{

	@Test
	public void testInsert()
	{
		JPAContainer<Contact> contacts = JPAContainerFactory.make(Contact.class, "scouts");
		EntityProvider<Contact> ep = contacts.getEntityProvider();
		EntityManager em = ep.getEntityManager();

		try
		{
			Contact contact = new Contact();
			contact.setFirstname("Brett");
			contact.setLastname("Sutton");
			contact.addNote("My first note goes here", "The note body 1 is here");
			contact.addNote("My second note goes here", "The note body 2 is here");
			contact.setAddress(new Address("10 smith drv", "Sometown", "Victoria", "3000"));
			contact.addTag(new Tag("Tag1", "The Tag1"));
			contact.addTag(new Tag("Tag2", "The Tag2"));
			contacts.addEntity(contact);
			contacts.commit();

			// Check the contact notes and Tags exists
			Assert.assertTrue(contactExists("Brett", "Sutton"));
			Assert.assertTrue(noteExists("Brett", "Sutton", "My first note goes here"));
			Assert.assertTrue(noteExists("Brett", "Sutton", "My second note goes here"));
			Assert.assertTrue(addressExists("Brett", "Sutton", "10 smith drv", "Sometown", "Victoria", "3000"));
			Assert.assertTrue(tagExists("Brett", "Sutton", "Tag1"));
			Assert.assertTrue(tagExists("Brett", "Sutton", "Tag2"));
			
			// Delete the resulting contact
			Assert.assertTrue(contacts.removeAllItems());
			contacts.commit();
			
			Assert.assertFalse(contactExists("Brett", "Sutton"));
			Assert.assertTrue(Tag.findTag("Tag2") == null);
			Assert.assertTrue(Tag.findTag("Tag1") == null);
			Assert.assertTrue(Note.findNote("My first note goes here").size() == 0);
			Assert.assertTrue(Note.findNote("My second note goes here").size() == 0);
			Assert.assertTrue(Address.findAddress("10 smith drv", "Sometown", "Victoria", "3000").size() == 0);
		}
		finally
		{
			if (em != null)
				em.close();
		}
	}








	@Test
	public void testUpdate()
	{
		JPAContainer<Contact> contacts = JPAContainerFactory.make(Contact.class, "scouts");
		EntityProvider<Contact> ep = contacts.getEntityProvider();
		EntityManager em = ep.getEntityManager();

		try
		{
			
			// create a contact
			Contact contact = new Contact();
			contact.setFirstname("Brett");
			contact.setLastname("Sutton");
			contact.addNote("My first note goes here", "The note body 1 is here");
			contact.addNote("My second note goes here", "The note body 2 is here");
			contact.setAddress(new Address("10 smith drv", "Sometown","Victoria",  "3000"));
			contact.addTag(new Tag("Tag1", "The Tag1"));
			contact.addTag(new Tag("Tag2", "The Tag2"));
			contacts.addEntity(contact);
			contacts.commit();

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
				result.deleteTag("Tag2");
				result.addTag(new Tag("Tag3", "The Tag3"));
				result.setAddress(new Address("20 replacement drv", "Othertown", "Victoria", "3000"));
			}
			contacts.addEntity(result);
			contacts.commit();

			
			// Check the contact notes and Tags exists
			Assert.assertTrue(contactExists("Stephen", "Sutton"));
			Assert.assertTrue(noteExists("Stephen", "Sutton", "My first note goes here"));
			Assert.assertTrue(noteExists("Stephen", "Sutton", "My second note goes here"));
			Assert.assertTrue(addressExists("Stephen", "Sutton", "10 smith drv", "Sometown", "Victoria", "3000"));
			Assert.assertTrue(tagExists("Stephen", "Sutton", "Tag1"));
			Assert.assertTrue(tagExists("Stephen", "Sutton", "Tag2"));

			// Now cleanup
			Assert.assertTrue(contacts.removeAllItems());
			contacts.commit();

			Assert.assertTrue(!contactExists("Stephen", "Sutton"));
			Assert.assertTrue(Tag.findTag("Tag2") == null);
			Assert.assertTrue(Tag.findTag("Tag1") == null);
			Assert.assertTrue(Note.findNote("My first note goes here").size() == 0);
			Assert.assertTrue(Note.findNote("My second note goes here").size() == 0);
			Assert.assertTrue(Address.findAddress("10 smith drv", "Sometown", "Victoria", "3000").size() == 0);


		}
		finally
		{
			if (em != null)
				em.close();
		}
	}

	@Test
	public void testJPAInsertAddress()
	{
		JPAContainer<Contact> contacts = JPAContainerFactory.make(Contact.class, "scouts");
		EntityProvider<Contact> ep = contacts.getEntityProvider();
		EntityManager em = ep.getEntityManager();

		try
		{
			Contact contact = new Contact();
			contact.setFirstname("Rhiannon");
			contact.setLastname("Sutton");
			contacts.addEntity(contact);
			contacts.commit();

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
			contacts.addEntity(result);
			contacts.commit();

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
			Assert.assertTrue(contacts.removeAllItems());
			contacts.commit();

			Assert.assertFalse(contactExists("Rhiannon", "Sutton"));
			Assert.assertTrue(Tag.findTag("Tag2") == null);
			Assert.assertTrue(Tag.findTag("Tag1") == null);
			Assert.assertTrue(Note.findNote("My first note goes here").size() == 1);
			Assert.assertTrue(Note.findNote("My second note goes here").size() == 1);
			Assert.assertTrue(Address.findAddress("10 Mossman Drv", "Eaglemont", "Victoria", "3084").size() == 0);
		}
		finally
		{
			em.close();
		}
	}

	@Test
	public void testJPAAddNote()
	{
		JPAContainer<Contact> contacts = JPAContainerFactory.make(Contact.class, "scouts");
		EntityProvider<Contact> ep = contacts.getEntityProvider();
		EntityManager em = ep.getEntityManager();

		try
		{
			Contact contact = new Contact();
			contact.setFirstname("Tristan");
			contact.setLastname("Sutton");
			contacts.addEntity(contact);
			contacts.commit();
			
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
			contacts.addEntity(result);
			contacts.commit();
			
			// Check the contact notes exists
			Assert.assertTrue(contactExists("Tristan", "Sutton"));
			foundContacts = Contact.findContactByName("Tristan", "Sutton");
			Assert.assertTrue(foundContacts.size() == 1);
			contact = foundContacts.get(0);
			Assert.assertEquals("Ryan", contact.getMiddleName());
			Assert.assertTrue(noteExists("Tristan", "Sutton", "My first note goes here"));
			Assert.assertTrue(noteExists("Tristan", "Sutton", "My second note goes here"));
			
			//check that we hae nto address or tag.
			Assert.assertTrue(contact.getAddress() == null);
			Assert.assertTrue(contact.getTags().size() == 0);

		}
		finally
		{
			em.close();
		}
	}

	private boolean tagExists(String firstname, String lastname, String tagName)
	{
		boolean exists = false;
		JPAContainer<Contact> contacts = JPAContainerFactory.make(Contact.class, "scouts");
		EntityProvider<Contact> ep = contacts.getEntityProvider();
		EntityManager em = ep.getEntityManager();

		try
		{
			List<Contact> foundContacts = Contact.findContactByName(firstname, lastname);
			Assert.assertTrue(foundContacts.size() == 1);
			Contact contact = foundContacts.get(0);
			Tag tag = contact.getTag(tagName);
			if (tag != null)
				exists = true;
		}
		finally
		{
			if (em != null)
				em.close();
		}

		return exists;
	}

	private boolean addressExists(String firstname, String lastname, String street, String suburb, String state, String postcode)
	{
		boolean exists = false;
		JPAContainer<Contact> contacts = JPAContainerFactory.make(Contact.class, "scouts");
		EntityProvider<Contact> ep = contacts.getEntityProvider();
		EntityManager em = ep.getEntityManager();

		try
		{
			List<Contact> foundContacts = Contact.findContactByName(firstname, lastname);
			Assert.assertTrue(foundContacts.size() == 1);
			Contact contact = foundContacts.get(0);
			Address address = contact.getAddress();
			if (address != null && address.getStreet().equals(street) && address.getCity().equals(suburb) 
					&& address.getState().equals(state) && address.getPostcode().equals(postcode))
			{
				exists = true;
			}
		}
		finally
		{
			if (em != null)
				em.close();
		}

		return exists;
	}

	private boolean noteExists(String firstname, String lastname,  String noteSubject)
	{
		boolean exists = false;
		JPAContainer<Contact> contacts = JPAContainerFactory.make(Contact.class, "scouts");
		EntityProvider<Contact> ep = contacts.getEntityProvider();
		EntityManager em = ep.getEntityManager();

		try
		{
			List<Contact> foundContacts = Contact.findContactByName(firstname, lastname);
			Assert.assertTrue(foundContacts.size() == 1);
			Contact contact = foundContacts.get(0);
			Note note = contact.getNote(noteSubject);
			if (note != null)
				exists = true;
		}
		finally
		{
			if (em != null)
				em.close();
		}

		return exists;
	}

	private boolean contactExists(String firstname, String lastname)
	{
		List<Contact> foundContacts = Contact.findContactByName(firstname, lastname);
		return foundContacts.size() == 1;
	}

	


}
