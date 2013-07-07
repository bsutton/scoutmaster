package au.org.scoutmaster.domain;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import au.org.scoutmaster.FormField;
import au.org.scoutmaster.filter.EntityManagerProvider;

@Entity
@NamedQueries(
{
		@NamedQuery(name = "Contact.findAll", query = "SELECT contact FROM Contact contact"),
		@NamedQuery(name = "Contact.findByName", query = "SELECT contact FROM Contact contact WHERE contact.lastname like :lastname and contact.firstname like :firstname") })
public class Contact extends BaseEntity implements Importable
{
	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(Contact.class);

	public static final String FIRSTNAME = "firstname";
	public static final String LASTNAME = "lastname";
	public static final String BIRTH_DATE = "birthDate";
	public static final String ROLE = "role";
	public static final String SECTION = "section";

	@FormField(displayName = "Active")
	private final boolean active = true;

	@FormField(displayName = "Prefix")
	private final String prefix = "";

	@FormField(displayName = "Firstname")
	private String firstname = "";

	@FormField(displayName = "Middle Name")
	private String middlename = "";

	@FormField(displayName = "Lastname")
	private String lastname = "";

	@FormField(displayName = "Birth Date")
	private final Date birthDate;

	@FormField(displayName = "Gender")
	private final Gender gender = Gender.Male;

	@Transient
	private Age age;

	/**
	 * Adult fields
	 */

	@FormField(displayName = "Home Phone")
	private final String homePhone = "";

	@FormField(displayName = "Work Phone")
	private final String workPhone = "";

	@FormField(displayName = "Mobile")
	private final String mobile = "";

	@FormField(displayName = "Home Email")
	private final String homeEmail = "";

	@FormField(displayName = "Work Email")
	private final String workEmail = "";

	@FormField(displayName = "Preferred Phone")
	private final PreferredPhone preferredPhone = PreferredPhone.MOBILE;

	@FormField(displayName = "Preferred Email")
	private final PreferredEmail preferredEmail = PreferredEmail.HOME;

	@FormField(displayName = "Preferred Communications")
	private final PreferredCommunications preferredCommunications = PreferredCommunications.EMAIL_SMS;

	/**
	 * Youth fields
	 */
	@FormField(displayName = "Allergies")
	private final String allergies = "";

	@FormField(displayName = "Custody Order")
	private final Boolean custodyOrder = false;

	@FormField(displayName = "Custody Order Details")
	private final String custodyOrderDetails = "";

	@FormField(displayName = "School")
	private final String school = "";

	@FormField(displayName = "Section Eligibility")
	@ManyToOne
	private SectionType sectionEligibility;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@FormField(displayName = "Address")
	private Address address;

	/**
	 * Member fields
	 */
	@FormField(displayName = "Member")
	private final Boolean isMember = false; // this should be derived from the
											// member
	// records.

	@FormField(displayName = "Member No")
	private final String memberNo = "";

	@FormField(displayName = "Member Since")
	private final Date memberSince = new Date(new java.util.Date().getTime()); // this
																				// should
	// be
	// derived
	// from the
	// member
	// records.

	/** The actual section the Youth or Adult member is attached to. */
	@FormField(displayName = "Section")
	@ManyToOne
	private SectionType section;

	/**
	 * Affiliate - An Affiliate is any one that is actively associated with the
	 * group including Youth doing the three for free and the the parents of
	 * those Youth.
	 * 
	 * Prospects are not Affiliates.
	 */
	@FormField(displayName = "Hobbies")
	private final String hobbies = "";

	@FormField(displayName = "Affiliated Since")
	private final Date affiliatedSince = new Date(new java.util.Date().getTime());

	@FormField(displayName = "Group Role")
	private final GroupRole role = GroupRole.YouthMember;

	@FormField(displayName = "Medicare No")
	private final String medicareNo = "";

	@FormField(displayName = "Ambulance Subscriber")
	private final Boolean ambulanceSubscriber = false;

	@FormField(displayName = "Private Medical Insurance")
	private final Boolean privateMedicalInsurance = false;

	@FormField(displayName = "Private Medical Fund Name")
	private final String privateMedicalFundName = "";

	/**
	 * Affiliated Adults
	 */
	@FormField(displayName = "Current Employer")
	private final String currentEmployer = "";

	@FormField(displayName = "Job Title")
	private final String jobTitle = "";

	@FormField(displayName = "Has WWC")
	private final Boolean hasWWC = false;

	@FormField(displayName = "WWC Expiry")
	private Date wwcExpiry;

	@FormField(displayName = "WWC No")
	private final String wwcNo = "";

	@FormField(displayName = "Has Police Check")
	private final Boolean hasPoliceCheck = false;

	@FormField(displayName = "Police Check Expiry")
	private Date policeCheckExpiry;

	@FormField(displayName = "Has Food Handling Certificate")
	private final Boolean hasFoodHandlingCertificate = false;

	@FormField(displayName = "Has First Aid Certificate")
	private final Boolean hasFirstAidCertificate = false;

	/**
	 * List of tags used to describe this Contact.
	 */
	//@ManyToMany(mappedBy = "contacts", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private final Set<Tag> tags = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL)
	// (mappedBy = "contact", cascade = CascadeType.ALL, fetch =
	// FetchType.EAGER, orphanRemoval = true)
	@FormField(displayName = "")
	private final List<Note> notes = new ArrayList<>();

	/**
	 * List of interactions with this contact.
	 */
	@OneToMany(cascade = CascadeType.ALL)
	private final List<Activity> activites = new ArrayList<>();

	public Contact()
	{
		Calendar TenYearsAgo = Calendar.getInstance();
		TenYearsAgo.add(Calendar.YEAR, -10);
		this.birthDate = new Date(TenYearsAgo.getTime().getTime());
	}

	@Transient
	public Long getAge()
	{
		long age = 0;
		if (this.birthDate != null)
		{
			Calendar cal1 = new GregorianCalendar();
			Calendar cal2 = new GregorianCalendar();
			int factor = 0;
			Date date1 = this.birthDate;
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

	@Override
	public String toString()
	{
		return this.firstname + ", " + this.lastname;
	}

	@Override
	public Long getId()
	{
		return this.id;
	}

	public void setFirstname(String firstname)
	{
		this.firstname = firstname;

	}

	public void setLastname(String lastname)
	{
		this.lastname = lastname;
	}

	public void setMiddleName(String middlename)
	{
		this.middlename = middlename;
	}

	public void addNote(String subject, String body)
	{
		Note note = new Note(subject, body);
		// note.setContact(this);
		this.notes.add(note);
	}

	public void setAddress(Address address)
	{
		this.address = address;

	}

	public void addTag(Tag tag)
	{
//		tag.addContact(this);
		this.tags.add(tag);
	}

	/**
	 * Detaches the tag from this contact. The tag entity is not actually deleted as
	 * it may be used by other entities.
	 *  
	 * @param tagName
	 */
	public void detachTag(String tagName)
	{
		Tag tagToRemove = null;
		for (Tag tag : this.tags)
		{
			if (tag.isTag(tagName))
			{
				tagToRemove = tag;
			}
		}
		if (tagToRemove != null)
			this.tags.remove(tagToRemove);
		else
			logger.warn("Attempt to delete non-existant tag. tagName=" + tagName);
	}

	@SuppressWarnings("unchecked")
	static List<Contact> findContactByName(String firstname, String lastname)
	{
		List<Contact> resultContacts = null;
		EntityManager em = EntityManagerProvider.INSTANCE.getEntityManager();

		Query query = em.createNamedQuery("Contact.findByName");
		query.setParameter("firstname", firstname);
		query.setParameter("lastname", lastname);
		resultContacts = query.getResultList();
		return resultContacts;
	}

	public Note getNote(String noteSubject)
	{
		Note found = null;
		for (Note note : this.notes)
		{
			if (note.getSubject().equals(noteSubject))
			{
				found = note;
				break;
			}
		}
		return found;

	}

	public Address getAddress()
	{
		return this.address;
	}

	public Tag getTag(String tagName)
	{
		Tag found = null;
		for (Tag tag : this.tags)
		{
			if (tag.isTag(tagName))
			{
				found = tag;
				break;
			}
		}
		return found;
	}

	public List<Note> getNotes()
	{
		return this.notes;
	}

	public Set<Tag> getTags()
	{
		return this.tags;
	}

	public String getMiddleName()
	{
		return this.middlename;
	}

}
