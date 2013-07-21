package au.org.scoutmaster.domain;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

@Entity
@NamedQueries(
{
	@NamedQuery(name = Contact.FIND_ALL, query = "SELECT contact FROM Contact contact"),
	@NamedQuery(name = Contact.FIND_BY_NAME, query = "SELECT contact FROM Contact contact WHERE contact.lastname like :lastname and contact.firstname like :firstname") 
})
public class Contact extends BaseEntity implements Importable
{
	static public final String FIND_ALL = "Contact.findAll";
	static public final String FIND_BY_NAME = "Contact.findByName";

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(Contact.class);

	public static final String FIRSTNAME = "firstname";
	public static final String LASTNAME = "lastname";
	public static final String BIRTH_DATE = "birthDate";
	public static final String ROLE = "role";
	public static final String SECTION = "section";

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
	private Phone homePhone;

	@FormField(displayName = "Work Phone")
	private Phone workPhone;

	@FormField(displayName = "Mobile")
	private Phone mobile;

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

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@FormField(displayName = "Address")
	private Address address;

	/**
	 * Member fields
	 */
	@FormField(displayName = "Member")
	private Boolean isMember = false; // this should be derived from the
										// member
	// records.

	@FormField(displayName = "Member No")
	private String memberNo = "";

	@FormField(displayName = "Member Since")
	private Date memberSince = new Date(new java.util.Date().getTime()); // this
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
	private String hobbies = "";

	@FormField(displayName = "Affiliated Since")
	private Date affiliatedSince = new Date(new java.util.Date().getTime());

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
	private Date wwcExpiry = new Date(new java.util.Date().getTime());

	@FormField(displayName = "WWC No")
	private String wwcNo = "";

	@FormField(displayName = "Has Police Check")
	private Boolean hasPoliceCheck = false;

	@FormField(displayName = "Police Check Expiry")
	private Date policeCheckExpiry = new Date(new java.util.Date().getTime());

	@FormField(displayName = "Has Food Handling Certificate")
	private Boolean hasFoodHandlingCertificate = false;

	@FormField(displayName = "Has First Aid Certificate")
	private Boolean hasFirstAidCertificate = false;

	/**
	 * List of tags used to describe this Contact.
	 */
	// @ManyToMany(mappedBy = "contacts", cascade = CascadeType.ALL, fetch =
	// FetchType.EAGER)
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Tag> tags = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL)
	// (mappedBy = "contact", cascade = CascadeType.ALL, fetch =
	// FetchType.EAGER, orphanRemoval = true)
	@FormField(displayName = "")
	private List<Note> notes = new ArrayList<>();

	/**
	 * List of interactions with this contact.
	 */
	@OneToMany(cascade = CascadeType.ALL)
	private List<Activity> activites = new ArrayList<>();

	public Contact()
	{
		Calendar TenYearsAgo = Calendar.getInstance();
		TenYearsAgo.add(Calendar.YEAR, -10);
		this.birthDate = new Date(TenYearsAgo.getTime().getTime());
		this.wwcExpiry = new Date(TenYearsAgo.getTime().getTime());
	}

	@Override
	public String toString()
	{
		return this.firstname + ", " + this.lastname;
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

	public Date getBirthDate()
	{
		return this.birthDate;
	}

	public String getMiddlename()
	{
		return middlename;
	}

	public void setMiddlename(String middlename)
	{
		this.middlename = middlename;
	}

	public SectionType getSectionEligibility()
	{
		return sectionEligibility;
	}

	public void setSectionEligibility(SectionType sectionEligibility)
	{
		this.sectionEligibility = sectionEligibility;
	}

	public SectionType getSection()
	{
		return section;
	}

	public void setSection(SectionType section)
	{
		this.section = section;
	}

	public Date getWwcExpiry()
	{
		return wwcExpiry;
	}

	public void setWwcExpiry(Date wwcExpiry)
	{
		this.wwcExpiry = wwcExpiry;
	}

	public Date getPoliceCheckExpiry()
	{
		return policeCheckExpiry;
	}

	public void setPoliceCheckExpiry(Date policeCheckExpiry)
	{
		this.policeCheckExpiry = policeCheckExpiry;
	}

	public boolean getActive()
	{
		return active;
	}

	public String getPrefix()
	{
		return prefix;
	}

	public void setFirstname(String firstname)
	{
		this.firstname = firstname;
	}

	public String getFirstname()
	{
		return firstname;
	}

	public String getLastname()
	{
		return lastname;
	}

	public Gender getGender()
	{
		return gender;
	}

	public void setHomePhone(Phone homePhone)
	{
		this.homePhone = homePhone;
	}

	public Phone getHomePhone()
	{
		return homePhone;
	}

	public Phone getWorkPhone()
	{
		return workPhone;
	}

	public Phone getMobile()
	{
		return mobile;
	}

	public String getHomeEmail()
	{
		return homeEmail;
	}

	public String getWorkEmail()
	{
		return workEmail;
	}

	public PreferredPhone getPreferredPhone()
	{
		return preferredPhone;
	}

	public PreferredEmail getPreferredEmail()
	{
		return preferredEmail;
	}

	public PreferredCommunications getPreferredCommunications()
	{
		return preferredCommunications;
	}

	public String getAllergies()
	{
		return allergies;
	}

	public Boolean getCustodyOrder()
	{
		return custodyOrder;
	}

	public String getCustodyOrderDetails()
	{
		return custodyOrderDetails;
	}

	public String getSchool()
	{
		return school;
	}

	public Boolean getIsMember()
	{
		return isMember;
	}

	public String getMemberNo()
	{
		return memberNo;
	}

	public Date getMemberSince()
	{
		return memberSince;
	}

	public String getHobbies()
	{
		return hobbies;
	}

	public Date getAffiliatedSince()
	{
		return affiliatedSince;
	}

	public GroupRole getRole()
	{
		return role;
	}

	public String getMedicareNo()
	{
		return medicareNo;
	}

	public Boolean getAmbulanceSubscriber()
	{
		return ambulanceSubscriber;
	}

	public Boolean getPrivateMedicalInsurance()
	{
		return privateMedicalInsurance;
	}

	public String getPrivateMedicalFundName()
	{
		return privateMedicalFundName;
	}

	public String getCurrentEmployer()
	{
		return currentEmployer;
	}

	public String getJobTitle()
	{
		return jobTitle;
	}

	public Boolean getHasWWC()
	{
		return hasWWC;
	}

	public String getWwcNo()
	{
		return wwcNo;
	}

	public Boolean getHasPoliceCheck()
	{
		return hasPoliceCheck;
	}

	public Boolean getHasFoodHandlingCertificate()
	{
		return hasFoodHandlingCertificate;
	}

	public Boolean getHasFirstAidCertificate()
	{
		return hasFirstAidCertificate;
	}

	public List<Activity> getActivites()
	{
		return activites;
	}

	public void setAddress(Address address)
	{
		this.address = address;
	}

	public Address getAddress()
	{
		return address;
	}

	public void setLastname(String lastname)
	{
		this.lastname = lastname;
	}

}
