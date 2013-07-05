package au.com.noojee.scouts.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import au.com.noojee.scouts.FormField;

import com.vaadin.addon.jpacontainer.EntityProvider;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;

@Entity
@NamedQueries(
{
		@NamedQuery(name = "Contact.findAll", query = "SELECT contact FROM Contact contact"),
		@NamedQuery(name = "Contact.findByName", query = "SELECT contact FROM Contact contact WHERE contact.lastname like :lastname and contact.firstname like :firstname") 
})
public class Contact implements Importable
{
	static public Logger logger = Logger.getLogger(Contact.class);
	
	public static final String FIRSTNAME = "firstname";
	public static final String LASTNAME = "lastname";
	public static final String BIRTH_DATE = "birthDate";
	public static final String ROLE = "role";
	public static final String SECTION = "section";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/** Common contact fields **/
	@FormField(displayName = "Created", visible = false)
	private Date created = new Date();

	@FormField(displayName = "Active")
	private boolean active = true;

	@FormField(displayName = "Prefix")
	private String prefix = "";

	@FormField(displayName = "Firstname")
	private String firstname = "";

	@FormField(displayName = "Middle Name")
	private String middlename = "";

	@FormField(displayName = "Lastname")
	private String lastname = "";

	@FormField(displayName = "Birth Date")
	private Date birthDate;

	@FormField(displayName = "Gender")
	private Gender gender = Gender.Male;

	@Transient
	private Age age;

	/**
	 * Adult fields
	 */

	@FormField(displayName = "Home Phone")
	private String homePhone = "";

	@FormField(displayName = "Work Phone")
	private String workPhone = "";

	@FormField(displayName = "Mobile")
	private String mobile = "";

	@FormField(displayName = "Home Email")
	private String homeEmail = "";

	@FormField(displayName = "Work Email")
	private String workEmail = "";

	@FormField(displayName = "Preferred Phone")
	private PreferredPhone preferredPhone = PreferredPhone.MOBILE;

	@FormField(displayName = "Preferred Email")
	private PreferredEmail preferredEmail = PreferredEmail.HOME;

	@FormField(displayName = "Preferred Communications")
	private PreferredCommunications preferredCommunications = PreferredCommunications.EMAIL_SMS;

	/**
	 * Youth fields
	 */
	@FormField(displayName = "Allergies")
	private String allergies = "";

	@FormField(displayName = "Custody Order")
	private Boolean custodyOrder = false;

	@FormField(displayName = "Custody Order Details")
	private String custodyOrderDetails = "";

	@FormField(displayName = "School")
	private String school = "";

	@FormField(displayName = "Section Eligibility")
	@ManyToOne
	private SectionType sectionEligibility;

	@OneToOne(mappedBy = "occupant", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@FormField(displayName = "Address")
	private Address address;
	

	/**
	 * Member fields
	 */
	@FormField(displayName = "Member")
	private Boolean isMember = false; // this should be derived from the member
										// records.

	@FormField(displayName = "Member No")
	private String memberNo = "";

	@FormField(displayName = "Member Since")
	private Date memberSince = Calendar.getInstance().getTime(); // this should
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
	private String hobbies = "";

	@FormField(displayName = "Affiliated Since")
	private Date affiliatedSince = Calendar.getInstance().getTime();

	@FormField(displayName = "Group Role")
	private GroupRole role = GroupRole.YouthMember;

	@FormField(displayName = "Medicare No")
	private String medicareNo = "";

	@FormField(displayName = "Ambulance Subscriber")
	private Boolean ambulanceSubscriber = false;

	@FormField(displayName = "Private Medical Insurance")
	private Boolean privateMedicalInsurance = false;

	@FormField(displayName = "Private Medical Fund Name")
	private String privateMedicalFundName = "";

	/**
	 * Affiliated Adults
	 */
	@FormField(displayName = "Current Employer")
	private String currentEmployer = "";

	@FormField(displayName = "Job Title")
	private String jobTitle = "";

	@FormField(displayName = "Has WWC")
	private Boolean hasWWC = false;

	@FormField(displayName = "WWC Expiry")
	private Date wwcExpiry;

	@FormField(displayName = "WWC No")
	private String wwcNo = "";

	@FormField(displayName = "Has Police Check")
	private Boolean hasPoliceCheck = false;

	@FormField(displayName = "Police Check Expiry")
	private Date policeCheckExpiry;

	@FormField(displayName = "Has Food Handling Certificate")
	private Boolean hasFoodHandlingCertificate = false;

	@FormField(displayName = "Has First Aid Certificate")
	private Boolean hasFirstAidCertificate = false;

	/**
	 * List of tags used to describe this Contact.
	 */
	@ManyToMany(mappedBy = "contacts", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Tag> tags = new HashSet<>();

	@OneToMany
	//(mappedBy = "contact", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@FormField(displayName = "")
	private List<Note> notes = new ArrayList<>();
	
	/**
	 * List of interactions with this contact.
	 */
	@OneToMany
	private List<Activity> activites = new ArrayList<>();

	public Contact()
	{
		Calendar TenYearsAgo = Calendar.getInstance();
		TenYearsAgo.add(Calendar.YEAR, -10);
		this.birthDate = TenYearsAgo.getTime();
	}

	@Transient
	public Long getAge()
	{
		long age = 0;
		if (birthDate != null)
		{
			Calendar cal1 = new GregorianCalendar();
			Calendar cal2 = new GregorianCalendar();
			int factor = 0;
			Date date1 = birthDate;
			Date date2 = Calendar.getInstance().getTime();
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
		return firstname + ", " + lastname;
	}

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
		//note.setContact(this);
		this.notes.add(note);
	}

	public void setAddress(Address address)
	{
		address.setOccupant(this);
		this.address = address;

	}

	public void addTag(Tag tag)
	{
		tag.addContact(this);
		this.tags.add(tag);
	}

	public void deleteTag(String tagName)
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
		logger.warn("Attempt to delete non-existant tag. tagName=" + tagName);
	}

	@SuppressWarnings("unchecked")
	static List<Contact> findContactByName(String firstname, String lastname)
	{
		List<Contact> resultContacts = null;
		JPAContainer<Contact> contacts = JPAContainerFactory.make(Contact.class, "scouts");
		EntityProvider<Contact> ep = contacts.getEntityProvider();
		EntityManager em = ep.getEntityManager();

		try
		{
			Query query = em.createNamedQuery("Contact.findByName");
			query.setParameter("firstname", firstname);
			query.setParameter("lastname", lastname);
			resultContacts =  query.getResultList();
		}
		finally
		{
			if (em != null)
				em.close();
		}
		return resultContacts;
	}

	public Note getNote(String noteSubject)
	{
		Note found = null;
		for (Note note : notes)
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
		for (Tag tag : tags)
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
		return notes;
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
