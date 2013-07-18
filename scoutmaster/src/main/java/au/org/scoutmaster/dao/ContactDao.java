package au.org.scoutmaster.dao;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Note;
import au.org.scoutmaster.domain.Tag;

public class ContactDao extends JpaBaseDao<Contact, Long> implements Dao<Contact, Long>
{
	static private Logger logger = Logger.getLogger(ContactDao.class);

	public ContactDao()
	{
		// inherit the default per request em. 
	}
	public ContactDao(EntityManager em)
	{
		super(em);
	}

	@Override
	public Contact findById(Long id)
	{
		Contact contact = entityManager.find(this.entityClass, id);
		return contact;
	}

	@Override
	public List<Contact> findAll()
	{
		Query query = entityManager.createNamedQuery(Contact.FIND_ALL);
		@SuppressWarnings("unchecked")
		List<Contact> list = query.getResultList();
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Contact> findContactByName(String firstname, String lastname)
	{
		Query query = entityManager.createNamedQuery(Contact.FIND_BY_NAME);
		query.setParameter("firstname", firstname);
		query.setParameter("lastname", lastname);
		List<Contact> resultContacts = query.getResultList();
		return resultContacts;
	}

	
	public Long getAge(Contact contact)
	{
		long age = 0;
		if (contact.getBirthDate() != null)
		{
			Calendar cal1 = new GregorianCalendar();
			Calendar cal2 = new GregorianCalendar();
			int factor = 0;
			Date date1 = contact.getBirthDate();
			Date date2 = new Date(new java.util.Date().getTime());
			cal1.setTime(date1);
			cal2.setTime(date2);
			if (cal2.get(Calendar.DAY_OF_YEAR) < cal1.get(Calendar.DAY_OF_YEAR))
			{
				factor = -1;
			}
			age = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR) + factor;
		}
		return age;
	}

	public void addNote(Contact contact, String subject, String body)
	{
		Note note = new Note(subject, body);
		// note.setContact(this);
		contact.getNotes().add(note);
	}

	public void addTag(Contact contact, Tag tag)
	{
//		tag.addContact(this);
		contact.getTags().add(tag);
	}

	/**
	 * Detaches the tag from this contact. The tag entity is not actually deleted as
	 * it may be used by other entities.
	 *  
	 * @param tagName
	 */
	public void detachTag(Contact contact, String tagName)
	{
		Tag tagToRemove = null;
		for (Tag tag : contact.getTags())
		{
			if (tag.isTag(tagName))
			{
				tagToRemove = tag;
			}
		}
		if (tagToRemove != null)
			contact.getTags().remove(tagToRemove);
		else
			logger.warn("Attempt to delete non-existant tag. tagName=" + tagName);
	}


	public Note getNote(Contact contact, String noteSubject)
	{
		Note found = null;
		for (Note note : contact.getNotes())
		{
			if (note.getSubject().equals(noteSubject))
			{
				found = note;
				break;
			}
		}
		return found;

	}
	public boolean hasTag(Contact contact, Tag tag)
	{
		boolean hasTag = false;
		for (Tag aTag : contact.getTags())
		{
			if (aTag.equals(tag))
			{
				hasTag = true;
				break;
			}
				
		}
		return hasTag;
	}



}
