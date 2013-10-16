package au.org.scoutmaster.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import au.com.vaadinutils.dao.EntityManagerProvider;
import au.org.scoutmaster.application.Transaction;
import au.org.scoutmaster.dao.ContactDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.NoteDao;
import au.org.scoutmaster.dao.TagDao;

public class ContactTest
{
	private static final String RYAN = "Ryan";
	private static final String THE_NOTE_BODY_2_IS_HERE = "The note body 2 is here";
	private static final String THE_NOTE_BODY_1_IS_HERE = "The note body 1 is here";
	private static final String MY_SECOND_NOTE_GOES_HERE = "My second note goes here";
	private static final String MY_FIRST_NOTE_GOES_HERE = "My first note goes here";
	private static final String TRISTAN = "Tristan";
	private static final String STEPHEN = "Stephen";
	private static final String TAG1 = "Tag1";
	private static final String TAG2 = "Tag2";
	private static final String SUTTON = "Sutton";
	private static final String BRETT = "Brett";
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
	public void testInsert()
	{
		try (Transaction t = new Transaction(em))
		{
			ContactDao daoContact = new DaoFactory(em).getContactDao();
			Contact contact = new Contact();
			contact.setFirstname(BRETT);
			contact.setLastname(SUTTON);
			daoContact.addNote(contact, MY_FIRST_NOTE_GOES_HERE, THE_NOTE_BODY_1_IS_HERE);
			daoContact.addNote(contact, MY_SECOND_NOTE_GOES_HERE, THE_NOTE_BODY_2_IS_HERE);
			contact.setAddress(new Address("10 smith drv", "Sometown", "Victoria", "3000"));
			daoContact.attachTag(contact, new Tag(TAG1, "The Tag1"));
			daoContact.attachTag(contact, new Tag(TAG2, "The Tag2"));

			daoContact.persist(contact);
			t.commit();
		}
		finally
		{
		}

		try (Transaction t = new Transaction(em))
		{
			ContactDao daoContact = new DaoFactory(em).getContactDao();

			// Check the contact notes and Tags exists
			Assert.assertTrue(contactExists(BRETT, SUTTON));
			Assert.assertTrue(noteExists(BRETT, SUTTON, MY_FIRST_NOTE_GOES_HERE));
			Assert.assertTrue(noteExists(BRETT, SUTTON, MY_SECOND_NOTE_GOES_HERE));
			Assert.assertTrue(addressExists(BRETT, SUTTON, "10 smith drv", "Sometown", "Victoria", "3000"));
			Assert.assertTrue(tagExists(BRETT, SUTTON, TAG1));
			Assert.assertTrue(tagExists(BRETT, SUTTON, TAG2));

			List<Contact> contacts = daoContact.findByName(BRETT, SUTTON);
			Assert.assertTrue(contacts.size() == 1);
			// Delete the resulting contact
			daoContact.remove(contacts.get(0));
			t.commit();
		}
		finally
		{
		}

		try (Transaction t = new Transaction(em))
		{
			TagDao daoTag = new DaoFactory(em).getTagDao();
			NoteDao daoNote = new DaoFactory(em).getNoteDao();
			Assert.assertFalse(contactExists(BRETT, SUTTON));
			Assert.assertTrue(daoTag.findByName(TAG2) == null);
			Assert.assertTrue(daoTag.findByName(TAG1) == null);
			Assert.assertTrue(daoNote.findNoteBySubject(MY_FIRST_NOTE_GOES_HERE).size() == 0);
			Assert.assertTrue(daoNote.findNoteBySubject(MY_SECOND_NOTE_GOES_HERE).size() == 0);
			Assert.assertTrue(Address.findAddress("10 smith drv", "Sometown", "Victoria", "3000").size() == 0);
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
			ContactDao daoContact = new DaoFactory(em).getContactDao();

			// create a contact
			Contact contact = new Contact();
			contact.setFirstname(BRETT);
			contact.setLastname(SUTTON);
			daoContact.addNote(contact, MY_FIRST_NOTE_GOES_HERE, THE_NOTE_BODY_1_IS_HERE);
			daoContact.addNote(contact, MY_SECOND_NOTE_GOES_HERE, THE_NOTE_BODY_2_IS_HERE);
			contact.setAddress(new Address("10 smith drv", "Sometown", "Victoria", "3000"));
			daoContact.attachTag(contact, new Tag(TAG1, "The Tag1"));
			daoContact.attachTag(contact, new Tag(TAG2, "The Tag2"));
			daoContact.merge(contact);
			t.commit();
		}
		finally
		{
		}

		try (Transaction t = new Transaction(em))
		{
			ContactDao daoContact = new DaoFactory(em).getContactDao();

			// Check the contact notes and Tags exists
			Assert.assertTrue(contactExists(BRETT, SUTTON));
			Assert.assertTrue(noteExists(BRETT, SUTTON, MY_FIRST_NOTE_GOES_HERE));
			Assert.assertTrue(noteExists(BRETT, SUTTON, MY_SECOND_NOTE_GOES_HERE));
			Assert.assertTrue(addressExists(BRETT, SUTTON, "10 smith drv", "Sometown", "Victoria", "3000"));
			Assert.assertTrue(tagExists(BRETT, SUTTON, TAG1));
			Assert.assertTrue(tagExists(BRETT, SUTTON, TAG2));

			// update the contact.
			List<Contact> list = daoContact.findAll();
			if (list.size() == 1)
			{
				Contact contact = list.get(0);
				contact.setFirstname(STEPHEN);
				contact.setMiddlename("Bret");
				daoContact.addNote(contact, "Note 3", "Note 3 body");
				daoContact.detachTag(contact, TAG2);
				daoContact.attachTag(contact, new Tag("Tag3", "The Tag3"));
				contact.setAddress(new Address("20 replacement drv", "Othertown", "Victoria", "3000"));
				daoContact.merge(contact);
			}
			t.commit();
		}
		finally
		{
		}

		try (Transaction t = new Transaction(em))
		{

			// Check the contact notes and Tags exists
			Assert.assertTrue(contactExists(STEPHEN, SUTTON));
			Assert.assertTrue(noteExists(STEPHEN, SUTTON, MY_FIRST_NOTE_GOES_HERE));
			Assert.assertTrue(noteExists(STEPHEN, SUTTON, MY_SECOND_NOTE_GOES_HERE));
			Assert.assertTrue(addressExists(STEPHEN, SUTTON, "20 replacement drv", "Othertown", "Victoria", "3000"));
			Assert.assertTrue(tagExists(STEPHEN, SUTTON, TAG1));
			Assert.assertFalse(tagExists(STEPHEN, SUTTON, TAG2));
			Assert.assertTrue(tagExists(STEPHEN, SUTTON, "Tag3"));
			t.commit();
		}
		finally
		{
		}

		try (Transaction t = new Transaction(em))
		{
			ContactDao daoContact = new DaoFactory(em).getContactDao();
			List<Contact> contacts = daoContact.findByName(STEPHEN, SUTTON);

			Assert.assertTrue(contacts.size() == 1);
			Contact contact = contacts.get(0);
			daoContact.detachTag(contact, TAG1);
			daoContact.detachTag(contact, TAG2);

			// Now cleanup
			daoContact.remove(contact);
			t.commit();

		}
		finally
		{
		}

		try (Transaction t = new Transaction(em))
		{

			TagDao daoTag = new DaoFactory(em).getTagDao();
			NoteDao daoNote = new DaoFactory(em).getNoteDao();

			Assert.assertTrue(!contactExists(STEPHEN, SUTTON));
			Assert.assertTrue(daoNote.findNoteBySubject(MY_FIRST_NOTE_GOES_HERE).size() == 0);
			Assert.assertTrue(daoNote.findNoteBySubject(MY_SECOND_NOTE_GOES_HERE).size() == 0);
			Assert.assertTrue(Address.findAddress("20 replacement drv", "Othertown", "Victoria", "3000").size() == 0);
			// tags exists even after they are detached from the contact.
			Assert.assertTrue(daoTag.findByName(TAG1) != null);
			Assert.assertTrue(daoTag.findByName(TAG2) != null);
			Assert.assertTrue(daoTag.findByName(TAG2) != null);
			t.commit();

			Organisation org = new Organisation();
			org.setName("Heidelberg Scouts");
			org.setLocation(new Address("31 Outhwaite Rd", "Heidelberg Heights", "Victoria", "3081"));
			Tag tag4 = TagDao.addTag("Tag4", "Yet another tag");
			org.addTag(tag4);
			Tag tag2 = daoTag.findByName(TAG2);
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
			ContactDao daoContact = new DaoFactory(em).getContactDao();
			Contact contact = new Contact();
			contact.setFirstname("Rhiannon");
			contact.setLastname(SUTTON);
			daoContact.persist(contact);
			t.commit();
		}
		finally
		{
		}

		try (Transaction t = new Transaction(em))
		{

			ContactDao daoContact = new DaoFactory(em).getContactDao();

			Contact contact = new Contact();

			// Check the contact notes and Tags exists
			Assert.assertTrue(contactExists("Rhiannon", SUTTON));
			List<Contact> foundContacts = daoContact.findByName("Rhiannon", SUTTON);
			Assert.assertTrue(foundContacts.size() == 1);
			contact = foundContacts.get(0);
			Assert.assertTrue(contact.getNotes().size() == 0);
			Assert.assertTrue(contact.getAddress() == null);
			Assert.assertTrue(contact.getTags().size() == 0);

			contact.setMiddlename("Paige");
			contact.setAddress(new Address("10 Mossman Drv", "Eaglemont", "Victoria", "3084"));
			daoContact.persist(contact);
			t.commit();
		}
		finally
		{
		}

		try (Transaction t = new Transaction(em))
		{

			ContactDao daoContact = new DaoFactory(em).getContactDao();

			Contact contact = null;

			// Check the contact notes and Tags exists
			Assert.assertTrue(contactExists("Rhiannon", SUTTON));
			List<Contact> foundContacts = daoContact.findByName("Rhiannon", SUTTON);
			Assert.assertTrue(foundContacts.size() == 1);
			contact = foundContacts.get(0);

			Assert.assertEquals("Paige", contact.getMiddlename());
			Assert.assertTrue(addressExists("Rhiannon", SUTTON, "10 Mossman Drv", "Eaglemont", "Victoria", "3084"));
			Assert.assertTrue(contact.getNotes().size() == 0);
			Assert.assertTrue(contact.getTags().size() == 0);

			// Now cleanup
			daoContact.remove(contact);
			t.commit();
		}
		finally
		{
		}

		try (Transaction t = new Transaction(em))
		{
			TagDao daoTag = new DaoFactory(em).getTagDao();
			NoteDao daoNote = new DaoFactory(em).getNoteDao();
			Assert.assertFalse(contactExists("Rhiannon", SUTTON));
			Assert.assertTrue(daoTag.findByName(TAG2) == null);
			Assert.assertTrue(daoTag.findByName(TAG1) == null);
			Assert.assertTrue(daoNote.findNoteBySubject(MY_FIRST_NOTE_GOES_HERE).size() == 0);
			Assert.assertTrue(daoNote.findNoteBySubject(MY_SECOND_NOTE_GOES_HERE).size() == 0);
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
			ContactDao daoContact = new DaoFactory(em).getContactDao();
			Contact contact = new Contact();
			contact.setFirstname(TRISTAN);
			contact.setLastname(SUTTON);
			daoContact.persist(contact);

			// Check the contact notes and Tags exists
			Assert.assertTrue(contactExists(TRISTAN, SUTTON));
			List<Contact> foundContacts = daoContact.findByName(TRISTAN, SUTTON);
			Assert.assertTrue(contact.getNotes().size() == 0);
			Assert.assertTrue(contact.getAddress() == null);
			Assert.assertTrue(contact.getTags().size() == 0);

			List<Contact> result = daoContact.findByName(TRISTAN, SUTTON);
			Assert.assertTrue(result.size() == 1);
			contact = result.get(0);
			contact.setMiddlename(RYAN);
			daoContact.addNote(contact, MY_FIRST_NOTE_GOES_HERE, THE_NOTE_BODY_1_IS_HERE);
			daoContact.addNote(contact, MY_SECOND_NOTE_GOES_HERE, THE_NOTE_BODY_2_IS_HERE);
			daoContact.merge(contact);

			// Check the contact notes exists
			Assert.assertTrue(contactExists(TRISTAN, SUTTON));

			foundContacts = daoContact.findByName(TRISTAN, SUTTON);
			Assert.assertTrue(foundContacts.size() == 1);
			contact = foundContacts.get(0);
			Assert.assertEquals(RYAN, contact.getMiddlename());
			Assert.assertTrue(noteExists(TRISTAN, SUTTON, MY_FIRST_NOTE_GOES_HERE));
			Assert.assertTrue(noteExists(TRISTAN, SUTTON, MY_SECOND_NOTE_GOES_HERE));

			// check that we have no address or tag.
			Assert.assertTrue(contact.getAddress() == null);
			Assert.assertTrue(contact.getTags().size() == 0);

			daoContact.remove(contact);
			t.commit();

		}
		finally
		{
		}
	}

	private boolean tagExists(String firstname, String lastname, String tagName)
	{
		boolean exists = false;

		ContactDao daoContact = new DaoFactory(em).getContactDao();

		List<Contact> foundContacts = daoContact.findByName(firstname, lastname);
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

		ContactDao daoContact = new DaoFactory(em).getContactDao();

		List<Contact> foundContacts = daoContact.findByName(firstname, lastname);
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

		ContactDao daoContact = new DaoFactory(em).getContactDao();

		List<Contact> foundContacts = daoContact.findByName(firstname, lastname);
		Assert.assertTrue(foundContacts.size() == 1);
		Contact contact = foundContacts.get(0);
		Note note = daoContact.getNote(contact, noteSubject);
		if (note != null)
			exists = true;

		return exists;
	}

	private boolean contactExists(String firstname, String lastname)
	{
		ContactDao daoContact = new DaoFactory(em).getContactDao();

		List<Contact> foundContacts = daoContact.findByName(firstname, lastname);
		return foundContacts.size() == 1;
	}

}
