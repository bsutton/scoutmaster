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
import au.org.scoutmaster.dao.ContactDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.NoteDao;
import au.org.scoutmaster.dao.PhoneDao;
import au.org.scoutmaster.dao.TagDao;
import au.org.scoutmaster.dao.Transaction;

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
	public void testInsert()
	{
		try (Transaction t = new Transaction(this.em))
		{
			final ContactDao daoContact = new DaoFactory(this.em).getContactDao();
			final Contact contact = new Contact();
			contact.setFirstname(ContactTest.BRETT);
			contact.setLastname(ContactTest.SUTTON);
			contact.setPhone1(new Phone("83208100"));
			daoContact.addNote(contact, ContactTest.MY_FIRST_NOTE_GOES_HERE, ContactTest.THE_NOTE_BODY_1_IS_HERE);
			daoContact.addNote(contact, ContactTest.MY_SECOND_NOTE_GOES_HERE, ContactTest.THE_NOTE_BODY_2_IS_HERE);
			contact.setAddress(new Address("10 smith drv", "Sometown", "Victoria", "3000"));
			daoContact.attachTag(contact, new Tag(ContactTest.TAG1, "The Tag1"));
			daoContact.attachTag(contact, new Tag(ContactTest.TAG2, "The Tag2"));

			daoContact.persist(contact);
			t.commit();
		}
		finally
		{
		}

		try (Transaction t = new Transaction(this.em))
		{
			final ContactDao daoContact = new DaoFactory(this.em).getContactDao();

			// Check the contact notes and Tags exists
			Assert.assertTrue(contactExists(ContactTest.BRETT, ContactTest.SUTTON));
			Assert.assertTrue(noteExists(ContactTest.BRETT, ContactTest.SUTTON, ContactTest.MY_FIRST_NOTE_GOES_HERE));
			Assert.assertTrue(noteExists(ContactTest.BRETT, ContactTest.SUTTON, ContactTest.MY_SECOND_NOTE_GOES_HERE));
			Assert.assertTrue(addressExists(ContactTest.BRETT, ContactTest.SUTTON, "10 smith drv", "Sometown",
					"Victoria", "3000"));
			Assert.assertTrue(tagExists(ContactTest.BRETT, ContactTest.SUTTON, ContactTest.TAG1));
			Assert.assertTrue(tagExists(ContactTest.BRETT, ContactTest.SUTTON, ContactTest.TAG2));

			final List<Contact> contacts = daoContact.findByName(ContactTest.BRETT, ContactTest.SUTTON);
			Assert.assertTrue(contacts.size() == 1);
			// Delete the resulting contact
			daoContact.remove(contacts.get(0));
			t.commit();
		}
		finally
		{
		}

		try (Transaction t = new Transaction(this.em))
		{
			final TagDao daoTag = new DaoFactory(this.em).getTagDao();
			final NoteDao daoNote = new DaoFactory(this.em).getNoteDao();
			Assert.assertFalse(contactExists(ContactTest.BRETT, ContactTest.SUTTON));
			Assert.assertTrue(daoTag.findByName(ContactTest.TAG2) == null);
			Assert.assertTrue(daoTag.findByName(ContactTest.TAG1) == null);
			Assert.assertTrue(daoNote.findNoteBySubject(ContactTest.MY_FIRST_NOTE_GOES_HERE).size() == 0);
			Assert.assertTrue(daoNote.findNoteBySubject(ContactTest.MY_SECOND_NOTE_GOES_HERE).size() == 0);
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
		try (Transaction t = new Transaction(this.em))
		{
			final ContactDao daoContact = new DaoFactory(this.em).getContactDao();

			// create a contact
			final Contact contact = new Contact();
			contact.setFirstname(ContactTest.BRETT);
			contact.setLastname(ContactTest.SUTTON);
			daoContact.addNote(contact, ContactTest.MY_FIRST_NOTE_GOES_HERE, ContactTest.THE_NOTE_BODY_1_IS_HERE);
			daoContact.addNote(contact, ContactTest.MY_SECOND_NOTE_GOES_HERE, ContactTest.THE_NOTE_BODY_2_IS_HERE);
			contact.setAddress(new Address("10 smith drv", "Sometown", "Victoria", "3000"));
			daoContact.attachTag(contact, new Tag(ContactTest.TAG1, "The Tag1"));
			daoContact.attachTag(contact, new Tag(ContactTest.TAG2, "The Tag2"));
			daoContact.persist(contact);
			t.commit();
		}
		finally
		{
		}

		try (Transaction t = new Transaction(this.em))
		{
			final ContactDao daoContact = new DaoFactory(this.em).getContactDao();

			// Check the contact notes and Tags exists
			Assert.assertTrue(contactExists(ContactTest.BRETT, ContactTest.SUTTON));
			Assert.assertTrue(phoneExists("83208100"));
			Assert.assertTrue(noteExists(ContactTest.BRETT, ContactTest.SUTTON, ContactTest.MY_FIRST_NOTE_GOES_HERE));
			Assert.assertTrue(noteExists(ContactTest.BRETT, ContactTest.SUTTON, ContactTest.MY_SECOND_NOTE_GOES_HERE));
			Assert.assertTrue(addressExists(ContactTest.BRETT, ContactTest.SUTTON, "10 smith drv", "Sometown",
					"Victoria", "3000"));
			Assert.assertTrue(tagExists(ContactTest.BRETT, ContactTest.SUTTON, ContactTest.TAG1));
			Assert.assertTrue(tagExists(ContactTest.BRETT, ContactTest.SUTTON, ContactTest.TAG2));

			// update the contact.
			final List<Contact> list = daoContact.findAll();
			if (list.size() == 1)
			{
				final Contact contact = list.get(0);
				contact.setFirstname(ContactTest.STEPHEN);
				contact.setMiddlename("Bret");
				contact.getPhone1().setPhoneNo("83208111");
				daoContact.addNote(contact, "Note 3", "Note 3 body");
				daoContact.detachTag(contact, ContactTest.TAG2);
				daoContact.attachTag(contact, new Tag("Tag3", "The Tag3"));
				contact.setAddress(new Address("20 replacement drv", "Othertown", "Victoria", "3000"));
			}
			t.commit();
		}
		finally
		{
		}

		try (Transaction t = new Transaction(this.em))
		{

			// Check the contact notes and Tags exists
			Assert.assertTrue(contactExists(ContactTest.STEPHEN, ContactTest.SUTTON));
			Assert.assertTrue(noteExists(ContactTest.STEPHEN, ContactTest.SUTTON, ContactTest.MY_FIRST_NOTE_GOES_HERE));
			Assert.assertTrue(noteExists(ContactTest.STEPHEN, ContactTest.SUTTON, ContactTest.MY_SECOND_NOTE_GOES_HERE));
			Assert.assertTrue(phoneExists("83208111"));
			Assert.assertTrue(addressExists(ContactTest.STEPHEN, ContactTest.SUTTON, "20 replacement drv", "Othertown",
					"Victoria", "3000"));
			Assert.assertTrue(tagExists(ContactTest.STEPHEN, ContactTest.SUTTON, ContactTest.TAG1));
			Assert.assertFalse(tagExists(ContactTest.STEPHEN, ContactTest.SUTTON, ContactTest.TAG2));
			Assert.assertTrue(tagExists(ContactTest.STEPHEN, ContactTest.SUTTON, "Tag3"));
			t.commit();
		}
		finally
		{
		}

		try (Transaction t = new Transaction(this.em))
		{
			final ContactDao daoContact = new DaoFactory(this.em).getContactDao();
			final List<Contact> contacts = daoContact.findByName(ContactTest.STEPHEN, ContactTest.SUTTON);

			Assert.assertTrue(contacts.size() == 1);
			final Contact contact = contacts.get(0);
			daoContact.detachTag(contact, ContactTest.TAG1);
			daoContact.detachTag(contact, ContactTest.TAG2);

			// Now cleanup
			daoContact.remove(contact);
			t.commit();

		}
		finally
		{
		}

		try (Transaction t = new Transaction(this.em))
		{

			final TagDao daoTag = new DaoFactory(this.em).getTagDao();
			final NoteDao daoNote = new DaoFactory(this.em).getNoteDao();

			Assert.assertTrue(!contactExists(ContactTest.STEPHEN, ContactTest.SUTTON));
			Assert.assertTrue(daoNote.findNoteBySubject(ContactTest.MY_FIRST_NOTE_GOES_HERE).size() == 0);
			Assert.assertTrue(daoNote.findNoteBySubject(ContactTest.MY_SECOND_NOTE_GOES_HERE).size() == 0);
			Assert.assertTrue(Address.findAddress("20 replacement drv", "Othertown", "Victoria", "3000").size() == 0);
			// tags exists even after they are detached from the contact.
			Assert.assertTrue(daoTag.findByName(ContactTest.TAG1) != null);
			Assert.assertTrue(daoTag.findByName(ContactTest.TAG2) != null);
			Assert.assertTrue(daoTag.findByName(ContactTest.TAG2) != null);
			t.commit();

			final Organisation org = new Organisation();
			org.setName("Heidelberg Scouts");
			org.setLocation(new Address("31 Outhwaite Rd", "Heidelberg Heights", "Victoria", "3081"));
			final Tag tag4 = TagDao.addTag("Tag4", "Yet another tag");
			org.addTag(tag4);
			final Tag tag2 = daoTag.findByName(ContactTest.TAG2);
			org.addTag(tag2);

		}
		finally
		{
		}
	}

	@Test
	public void testJPAInsertAddress()
	{
		try (Transaction t = new Transaction(this.em))
		{
			final ContactDao daoContact = new DaoFactory(this.em).getContactDao();
			final Contact contact = new Contact();
			contact.setFirstname("Rhiannon");
			contact.setLastname(ContactTest.SUTTON);
			daoContact.persist(contact);
			t.commit();
		}
		finally
		{
		}

		try (Transaction t = new Transaction(this.em))
		{

			final ContactDao daoContact = new DaoFactory(this.em).getContactDao();

			Contact contact = new Contact();

			// Check the contact notes and Tags exists
			Assert.assertTrue(contactExists("Rhiannon", ContactTest.SUTTON));
			final List<Contact> foundContacts = daoContact.findByName("Rhiannon", ContactTest.SUTTON);
			Assert.assertTrue(foundContacts.size() == 1);
			contact = foundContacts.get(0);
			Assert.assertTrue(contact.getNotes().size() == 0);
			Assert.assertTrue(contact.getTags().size() == 0);

			contact.setMiddlename("Paige");
			contact.setAddress(new Address("10 Mossman Drv", "Eaglemont", "Victoria", "3084"));
			daoContact.persist(contact);
			t.commit();
		}
		finally
		{
		}

		try (Transaction t = new Transaction(this.em))
		{

			final ContactDao daoContact = new DaoFactory(this.em).getContactDao();

			Contact contact = null;

			// Check the contact notes and Tags exists
			Assert.assertTrue(contactExists("Rhiannon", ContactTest.SUTTON));
			final List<Contact> foundContacts = daoContact.findByName("Rhiannon", ContactTest.SUTTON);
			Assert.assertTrue(foundContacts.size() == 1);
			contact = foundContacts.get(0);

			Assert.assertEquals("Paige", contact.getMiddlename());
			Assert.assertTrue(addressExists("Rhiannon", ContactTest.SUTTON, "10 Mossman Drv", "Eaglemont", "Victoria",
					"3084"));
			Assert.assertTrue(contact.getNotes().size() == 0);
			Assert.assertTrue(contact.getTags().size() == 0);

			// Now cleanup
			daoContact.remove(contact);
			t.commit();
		}
		finally
		{
		}

		try (Transaction t = new Transaction(this.em))
		{
			final TagDao daoTag = new DaoFactory(this.em).getTagDao();
			final NoteDao daoNote = new DaoFactory(this.em).getNoteDao();
			Assert.assertFalse(contactExists("Rhiannon", ContactTest.SUTTON));
			Assert.assertTrue(daoTag.findByName(ContactTest.TAG2) == null);
			Assert.assertTrue(daoTag.findByName(ContactTest.TAG1) == null);
			Assert.assertTrue(daoNote.findNoteBySubject(ContactTest.MY_FIRST_NOTE_GOES_HERE).size() == 0);
			Assert.assertTrue(daoNote.findNoteBySubject(ContactTest.MY_SECOND_NOTE_GOES_HERE).size() == 0);
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
		try (Transaction t = new Transaction(this.em))
		{
			final ContactDao daoContact = new DaoFactory(this.em).getContactDao();
			Contact contact = new Contact();
			contact.setFirstname(ContactTest.TRISTAN);
			contact.setLastname(ContactTest.SUTTON);
			daoContact.persist(contact);

			// Check the contact notes and Tags exists
			Assert.assertTrue(contactExists(ContactTest.TRISTAN, ContactTest.SUTTON));
			List<Contact> foundContacts = daoContact.findByName(ContactTest.TRISTAN, ContactTest.SUTTON);
			Assert.assertTrue(contact.getNotes().size() == 0);
			// Assert.assertTrue(contact.getAddress() == null);
			Assert.assertTrue(contact.getTags().size() == 0);

			final List<Contact> result = daoContact.findByName(ContactTest.TRISTAN, ContactTest.SUTTON);
			Assert.assertTrue(result.size() == 1);
			contact = result.get(0);
			contact.setMiddlename(ContactTest.RYAN);
			daoContact.addNote(contact, ContactTest.MY_FIRST_NOTE_GOES_HERE, ContactTest.THE_NOTE_BODY_1_IS_HERE);
			daoContact.addNote(contact, ContactTest.MY_SECOND_NOTE_GOES_HERE, ContactTest.THE_NOTE_BODY_2_IS_HERE);
			daoContact.merge(contact);

			// Check the contact notes exists
			Assert.assertTrue(contactExists(ContactTest.TRISTAN, ContactTest.SUTTON));

			foundContacts = daoContact.findByName(ContactTest.TRISTAN, ContactTest.SUTTON);
			Assert.assertTrue(foundContacts.size() == 1);
			contact = foundContacts.get(0);
			Assert.assertEquals(ContactTest.RYAN, contact.getMiddlename());
			Assert.assertTrue(noteExists(ContactTest.TRISTAN, ContactTest.SUTTON, ContactTest.MY_FIRST_NOTE_GOES_HERE));
			Assert.assertTrue(noteExists(ContactTest.TRISTAN, ContactTest.SUTTON, ContactTest.MY_SECOND_NOTE_GOES_HERE));

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

	private boolean tagExists(final String firstname, final String lastname, final String tagName)
	{
		boolean exists = false;

		final ContactDao daoContact = new DaoFactory(this.em).getContactDao();

		final List<Contact> foundContacts = daoContact.findByName(firstname, lastname);
		Assert.assertTrue(foundContacts.size() == 1);
		final Contact contact = foundContacts.get(0);
		final Tag tag = contact.getTag(tagName);
		if (tag != null)
		{
			exists = true;
		}

		return exists;
	}

	private boolean addressExists(final String firstname, final String lastname, final String street,
			final String suburb, final String state, final String postcode)
	{
		boolean exists = false;

		final ContactDao daoContact = new DaoFactory(this.em).getContactDao();

		final List<Contact> foundContacts = daoContact.findByName(firstname, lastname);
		Assert.assertTrue(foundContacts.size() == 1);
		final Contact contact = foundContacts.get(0);
		final Address address = contact.getAddress();
		if (address != null && address.getStreet().equals(street) && address.getCity().equals(suburb)
				&& address.getState().equals(state) && address.getPostcode().equals(postcode))
		{
			exists = true;
		}

		return exists;
	}

	private boolean phoneExists(final String phoneNo)
	{
		boolean exists = false;

		final PhoneDao daoPhone = new DaoFactory(this.em).getPhoneDao();

		final List<Phone> foundPhone = daoPhone.findByNo(phoneNo);
		Assert.assertTrue(foundPhone.size() == 1);
		final Phone phone = foundPhone.get(0);
		if (phone.getPhoneNo().equals(phoneNo))
		{
			exists = true;
		}

		return exists;
	}

	private boolean noteExists(final String firstname, final String lastname, final String noteSubject)
	{
		boolean exists = false;

		final ContactDao daoContact = new DaoFactory(this.em).getContactDao();

		final List<Contact> foundContacts = daoContact.findByName(firstname, lastname);
		Assert.assertTrue(foundContacts.size() == 1);
		final Contact contact = foundContacts.get(0);
		final Note note = daoContact.getNote(contact, noteSubject);
		if (note != null)
		{
			exists = true;
		}

		return exists;
	}

	private boolean contactExists(final String firstname, final String lastname)
	{
		final ContactDao daoContact = new DaoFactory(this.em).getContactDao();

		final List<Contact> foundContacts = daoContact.findByName(firstname, lastname);
		return foundContacts.size() == 1;
	}

}
