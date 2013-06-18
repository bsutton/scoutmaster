package au.com.noojee.scouts.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import au.com.noojee.scouts.FormField;

@Entity
public class Contact implements Importable
{

	public static final String FIRSTNAME = "firstname";
	public static final String LASTNAME = "lastname";
	public static final String BIRTH_DATE = "birthDate";
	public static final String ROLE = "role";
	public static final String SECTION = "section";

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/** Common contact fields **/
	@FormField(name="Created", visible=false)
	private Date created = Calendar.getInstance().getTime();

	@FormField(name="Active")
	private boolean active = true;

	@FormField(name="Prefix")
	private String prefix = "";
	
	@FormField(name="Firstname")
	private String firstname = "";
	
	@FormField(name="Middle Name")
	private String middlename = "";

	@FormField(name="Lastname")
	private String lastname = "";

	@FormField(name="Birth Date")
	private Date birthDate;
	
	@FormField(name="Gender")
	private Gender gender = Gender.Male;

	@Transient
	private Long age;
	
	/**
	 * Adult fields
	 */
	
	
	@FormField(name="Home Phone")
	private String homePhone = "";
	
	@FormField(name="Work Phone")
	private String workPhone = "";
	
	@FormField(name="Mobile")
	private String mobile = "";
	
	@FormField(name="Home Email")
	private String homeEmail = "";
	
	@FormField(name="Work Email")
	private String workEmail = "";
	
	@FormField(name="Preferred Phone")
	private PreferredPhone preferredPhone = PreferredPhone.MOBILE;
	
	@FormField(name="Preferred Email")
	private PreferredEmail preferredEmail = PreferredEmail.HOME;
	
	@FormField(name="Preferred Communications")
	private PreferredCommunications preferredCommunications = PreferredCommunications.EMAIL_SMS;
	
	/**
	 * Youth fields
	 */
	@FormField(name="Allergies")
	private String allergies = "";
	
	@FormField(name="Custody Order")
	private Boolean custodyOrder = false;
	
	@FormField(name="Custody Order Details")
	private String custodyOrderDetails = "";
	
	@FormField(name="School")
	private String school = "";
	
	@FormField(name="Section Eligibility")
	private Section sectionEligibility;
	
	//private Address address;
	
	
	/**
	 * Member fields
	 */
	@FormField(name="Member")
	private Boolean isMember = false; // this should be derived from the member records.
	
	@FormField(name="Member No")
	private String memberNo = "";
	
	@FormField(name="Member Since")
	private Date memberSince = Calendar.getInstance().getTime(); // this should be derived from the member records.
	
	/** The actual section the Youth or Adult member is attached to. */
	@FormField(name="Section")
	private Section section;
	
	
	/**
	 * Affiliate - An Affiliate as any one that is actively associated with the group
	 *  including Youth doing the three for free and the the parents of those Youth.
	 *  
	 *  Prospects are not Affiliates.
	 */
	@FormField(name="Hobbies")
	private String hobbies = "";
	
	@FormField(name="Affiliated Since")
	private Date affiliatedSince = Calendar.getInstance().getTime();
	
	@FormField(name="Group Role")
	private GroupRole role = GroupRole.YouthMember;
	
	@FormField(name="Medicare No")
	private String medicareNo = "";
	
	@FormField(name="Ambulance Subscriber")
	private Boolean ambulanceSubscriber = false;
	
	@FormField(name="Private Medical Insurance")
	private Boolean privateMedicalInsurance = false;
	
	@FormField(name="Private Medical Fund Name")
	private String privateMedicalFundName = "";
	
	/**
	 * Affiliated Adults
	 */
	@FormField(name="Current Employer")
	private String currentEmployer = "";
	
	@FormField(name="Job Title")
	private String jobTitle = "";
	
	@FormField(name="Has WWC")
	private Boolean hasWWC = false;
	
	@FormField(name="WWC Expiry")
	private Date wwcExpiry;
	
	@FormField(name="WWC No")
	private String wwcNo = "";
	
	@FormField(name="Has Police Check")
	private Boolean hasPoliceCheck = false;
	
	@FormField(name="Police Check Expiry")
	private Date policeCheckExpiry;
	
	@FormField(name="Has Food Handling Certificate")
	private Boolean hasFoodHandlingCertificate = false;
	
	@FormField(name="Has First Aid Certificate")
	private Boolean hasFirstAidCertificate = false;

	
	//@OneToMany(mappedBy = "contact")
	//private Set<Tag> tags;
	

	@OneToMany(mappedBy = "contact")
	@FormField(name="")
	private Set<Note> notes;

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

}
