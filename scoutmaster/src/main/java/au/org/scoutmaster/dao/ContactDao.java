package au.org.scoutmaster.dao;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import com.vaadin.addon.jpacontainer.JPAContainer;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.com.vaadinutils.dao.Path;
import au.org.scoutmaster.domain.Age;
import au.org.scoutmaster.domain.CommunicationLog;
import au.org.scoutmaster.domain.Contact;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.Note;
import au.org.scoutmaster.domain.Relationship;
import au.org.scoutmaster.domain.SectionType;
import au.org.scoutmaster.domain.Section_;
import au.org.scoutmaster.domain.Tag;

public class ContactDao extends JpaBaseDao<Contact, Long> implements Dao<Contact, Long>
{
	static private Logger logger = LogManager.getLogger(ContactDao.class);

	public ContactDao()
	{
		// inherit the default per request em.
	}

	
	@SuppressWarnings("unchecked")
	public List<Contact> findByName(final String firstname, final String lastname)
	{
		final Query query = JpaBaseDao.getEntityManager().createNamedQuery(Contact.FIND_BY_NAME);
		query.setParameter("firstname", firstname);
		query.setParameter("lastname", lastname);
		final List<Contact> resultContacts = query.getResultList();
		return resultContacts;
	}

	/**
	 * Find all contacts that have an email address
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Contact> findByHasEmail()
	{
		final Query query = JpaBaseDao.getEntityManager().createNamedQuery(Contact.FIND_BY_HAS_EMAIL);
		final List<Contact> resultContacts = query.getResultList();
		return resultContacts;
	}

	public Long getAge(final Contact contact)
	{
		long age = 0;
		if (contact.getBirthDate() != null)
		{
			final Calendar cal1 = new GregorianCalendar();
			final Calendar cal2 = new GregorianCalendar();
			int factor = 0;
			final Date date1 = contact.getBirthDate();
			final Date date2 = new Date(new java.util.Date().getTime());
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

	public void addNote(final Contact contact, final String subject, final String body)
	{
		final Note note = new Note(subject, body);
		// note.setContact(this);
		contact.getNotes().add(note);
	}

	public void attachTag(final Contact contact, final Tag tag)
	{
		contact.getTags().add(tag);
		tag.getContacts().add(contact);
	}

	public void addRelationship(final Contact contact, final Relationship child)
	{
		contact.getLHSRelationships().add(child);

	}

	public void addCommunication(final Contact contact, final CommunicationLog communication)
	{
		contact.getCommunicationsLog().add(communication);

	}

	/**
	 * Detaches the tag from this contact. The tag entity is not actually
	 * deleted as it may be used by other entities.
	 *
	 * @param tagName
	 */
	public void detachTag(final Contact contact, final String tagName)
	{
		Tag tagToRemove = null;
		for (final Tag tag : contact.getTags())
		{
			if (tag.isTag(tagName))
			{
				tagToRemove = tag;
			}
		}
		if (tagToRemove != null)
		{
			detachTag(contact, tagToRemove);
		}
		else
		{
			ContactDao.logger.warn("Attempt to detach non-existant tag. tagName={}", tagName);
		}
	}

	public void detachTag(final Contact contact, final Tag tag)
	{
		if (tag != null)
		{
			contact.getTags().remove(tag);
		}
	}

	public Note getNote(final Contact contact, final String noteSubject)
	{
		Note found = null;
		for (final Note note : contact.getNotes())
		{
			if (note.getSubject().equals(noteSubject))
			{
				found = note;
				break;
			}
		}
		return found;

	}

	public boolean hasTag(final Contact contact, final Tag tag)
	{
		boolean hasTag = false;
		for (final Tag aTag : contact.getTags())
		{
			if (aTag.equals(tag))
			{
				hasTag = true;
				break;
			}

		}
		return hasTag;
	}

	public Age getAge(final java.util.Date birthDate)
	{
		final DateTime date = new DateTime(birthDate);
		return new Age(date);
	}

	public SectionType getSectionEligibilty(final java.util.Date birthDate)
	{
		final DateTime date = new DateTime(new DateMidnight(birthDate));

		final SectionTypeDao daoSectionType = new SectionTypeDao();
		return daoSectionType.getEligibleSection(date);
	}

	@Override
	public JPAContainer<Contact> createVaadinContainer()
	{
		final JPAContainer<Contact> contactContainer = super.createVaadinContainer();
		contactContainer.addNestedContainerProperty("phone1.phoneNo");
		contactContainer.addNestedContainerProperty("phone1.primaryPhone");
		contactContainer.addNestedContainerProperty("phone1.phoneType");

		contactContainer.addNestedContainerProperty("phone2.phoneNo");
		contactContainer.addNestedContainerProperty("phone2.primaryPhone");
		contactContainer.addNestedContainerProperty("phone2.phoneType");

		contactContainer.addNestedContainerProperty("phone3.phoneNo");
		contactContainer.addNestedContainerProperty("phone3.primaryPhone");
		contactContainer.addNestedContainerProperty("phone3.phoneType");

		contactContainer.addNestedContainerProperty("address.street");
		contactContainer.addNestedContainerProperty("address.city");
		contactContainer.addNestedContainerProperty("address.postcode");
		contactContainer.addNestedContainerProperty("address.state");

		contactContainer.addNestedContainerProperty("groupRole.name");
		contactContainer.addNestedContainerProperty(new Path(Contact_.section, Section_.name).getName());

		return contactContainer;
	}

}
