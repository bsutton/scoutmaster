package au.org.scoutmaster.domain;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Past;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;

import au.com.vaadinutils.crud.CrudEntity;
import au.org.scoutmaster.dao.ContactDao;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.domain.validation.MemberChecks;

@Entity(name = "Contact")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "Contact")
@Access(AccessType.FIELD)
@NamedQueries(
{ @NamedQuery(name = Contact.FIND_BY_NAME, query = "SELECT contact FROM Contact contact WHERE contact.lastname like :lastname and contact.firstname like :firstname"),
		@NamedQuery(name = Contact.FIND_BY_HAS_EMAIL, query = "SELECT contact FROM Contact contact WHERE contact.homeEmail is not null or contact.workEmail is not null") })
public class Contact extends BaseEntity implements Importable, CrudEntity
{
	static public final String FIND_BY_NAME = "Contact.findByName";
	static public final String FIND_BY_HAS_EMAIL = "Contact.findByHasEmail";

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	static private Logger logger = LogManager.getLogger(Contact.class);

	public static final String PRIMARY_PHONE = "primaryPhone";

	@FormField(displayName = "Active")
	private Boolean active = true;

	@FormField(displayName = "Prefix")
	private String prefix = "";

	@NotBlank
	@javax.validation.constraints.Size(min = 1, max = 255)
	@FormField(displayName = "Firstname")
	private String firstname = "";

	@FormField(displayName = "Middle Name")
	private String middlename = "";

	@NotBlank
	@javax.validation.constraints.Size(min = 1, max = 255)
	@FormField(displayName = "Lastname")
	private String lastname = "";

	/**
	 * This is an amalgum of the firstname and lastname i.e firstname + " " +
	 * lastname This is redundant but it makes it easier to create lists which
	 * are sorted by the full name as the metamodel doesn't expose transient
	 * fields so we can filter or sort on a transient field in many scenarios.
	 */
	@SuppressWarnings("unused")
	private String fullname = "";

	@FormField(displayName = "Birth Date")
	@Past
	private Date birthDate;

	@FormField(displayName = "Gender")
	private Gender gender = Gender.Male;

	@Transient
	private Age age;

	@Transient
	private Phone primaryPhone;

	/**
	 * Contact fields
	 */

	/**
	 * If true then the contact is not to be included in any bulk
	 * communications.
	 */
	private Boolean doNotSendBulkCommunications = false;

	@FormField(displayName = "Phone 1")
	@Embedded
	@AttributeOverrides(
	{ @AttributeOverride(name = "phoneType", column = @Column(name = "phone1PhoneType") ),
			@AttributeOverride(name = "primaryPhone", column = @Column(name = "phone1PrimaryPhone") ),
			@AttributeOverride(name = "phoneNo", column = @Column(name = "phone1PhoneNo") )

	})
	private Phone phone1 = new Phone();

	@FormField(displayName = "Phone 2")
	@Embedded
	@AttributeOverrides(
	{ @AttributeOverride(name = "phoneType", column = @Column(name = "phone2PhoneType") ),
			@AttributeOverride(name = "primaryPhone", column = @Column(name = "phone2PrimaryPhone") ),
			@AttributeOverride(name = "phoneNo", column = @Column(name = "phone2PhoneNo") )

	})
	private Phone phone2 = new Phone();

	@FormField(displayName = "Phone 3")
	@Embedded
	@AttributeOverrides(
	{ @AttributeOverride(name = "phoneType", column = @Column(name = "phone3PhoneType") ),
			@AttributeOverride(name = "primaryPhone", column = @Column(name = "phone3PrimaryPhone") ),
			@AttributeOverride(name = "phoneNo", column = @Column(name = "phone3PhoneNo") )

	})
	private Phone phone3 = new Phone();

	@FormField(displayName = "Home Email")
	@Email
	private String homeEmail = "";

	@FormField(displayName = "Work Email")
	@Email
	private String workEmail = "";

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
	@Transient
	private SectionType sectionEligibility;

	@OneToOne(fetch = FetchType.EAGER, orphanRemoval = true, targetEntity = Address.class, cascade =
	{ CascadeType.MERGE })
	@FormField(displayName = "Address")
	private Address address = new Address();

	/**
	 * Member fields
	 */
	@FormField(displayName = "Member")
	@AssertTrue(groups = MemberChecks.class)
	private Boolean isMember = false; // this should be derived from the
	// member
	// records.

	@FormField(displayName = "Member No")
	@NotEmpty(groups = MemberChecks.class)
	private String memberNo = "";

	@FormField(displayName = "Member Since")
	@Past(groups = MemberChecks.class)
	private Date memberSince = new Date(new java.util.Date().getTime()); // this
	// should
	// be
	// derived
	// from the
	// member
	// records.

	/**
	 * The date the member was invested into the movement, null if they haven't
	 * been invested.
	 */
	private Date dateMemberInvested;

	/** The actual section the Youth or Adult member is attached to. */
	@FormField(displayName = "Section")
	@ManyToOne(optional = true, targetEntity = SectionType.class)
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
	@ManyToOne(targetEntity = GroupRole.class)
	private GroupRole groupRole;

	@FormField(displayName = "Medicare No")
	private String medicareNo = "";

	@FormField(displayName = "Ambulance Subscriber")
	private Boolean ambulanceSubscriber = false;

	@FormField(displayName = "Private Medical Insurance")
	private Boolean privateMedicalInsurance = false;

	@FormField(displayName = "Private Medical Fund Name")
	private String privateMedicalFundName = "";

	@FormField(displayName = "Medical Fund No.")
	private String medicalFundNo = "";

	/**
	 * Affiliated Adults
	 */
	@FormField(displayName = "Current Employer")
	private String currentEmployer = "";

	@FormField(displayName = "Job Title")
	private String jobTitle = "";

	@FormField(displayName = "Has License")
	private Boolean hasLicense = false;

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
	 * Contacts this contact is related to on the Left Hand Side (LHS) of the
	 * relationship type.
	 *
	 * e.g. Brett 'Parent Of' Tristan
	 *
	 * Brett is on the LHS of the relationship
	 */
	@OneToMany(mappedBy = "lhs", targetEntity = Relationship.class, orphanRemoval = true)
	private Set<Relationship> lhsrelationships = new HashSet<>();

	/**
	 * Contacts this contact is related to on the Right Hand Side (RHS) of the
	 * relationship type.
	 *
	 * e.g. Brett 'Parent Of' Tristan
	 *
	 * Tristan is on the RHS of the relationship
	 */
	@OneToMany(mappedBy = "rhs", targetEntity = Relationship.class, orphanRemoval = true)
	private Set<Relationship> rhsrelationships = new HashSet<>();

	/**
	 * List of tags used to describe this Contact.
	 */
	@ManyToMany(fetch = FetchType.EAGER, targetEntity = Tag.class)
	private Set<Tag> tags = new HashSet<>();

	@OneToMany(mappedBy = "attachedContact", orphanRemoval = true)
	@FormField(displayName = "")
	private List<Note> notes = new ArrayList<>();

	/**
	 * List of interactions with this contact.
	 */
	@OneToMany(mappedBy = "withContact", targetEntity = CommunicationLog.class, orphanRemoval = true)
	private List<CommunicationLog> activities = new ArrayList<>();

	/**
	 * Id imported along with the contact. Used to link the contact to an
	 * external data source generally the one it was imported from. This can be
	 * used during subsequent imports to link additional data entities to this
	 * contact.
	 */
	@FormField(displayName = "Import ID")
	private String importId;

	public Contact()
	{
		final Calendar TenYearsAgo = Calendar.getInstance();
		TenYearsAgo.add(Calendar.YEAR, -10);
		this.birthDate = new Date(TenYearsAgo.getTime().getTime());
		this.wwcExpiry = new Date(TenYearsAgo.getTime().getTime());
	}

	@Override
	public String toString()
	{
		return this.firstname + ", " + this.lastname;
	}

	public Tag getTag(final String tagName)
	{
		Tag found = null;
		for (final Tag tag : this.tags)
		{
			if (tag.isTag(tagName))
			{
				found = tag;
				break;
			}
		}
		return found;
	}

	public String getImportId()
	{
		return this.importId;
	}

	public void setImportId(final String importId)
	{
		this.importId = importId;
	}

	public List<Note> getNotes()
	{
		// If some wants the list lets force it to be read from the db.
		this.notes.isEmpty();
		return this.notes;
	}

	public Set<Tag> getTags()
	{
		// If someone wants the list lets force it to be read from the db.
		this.tags.isEmpty();
		return this.tags;
	}

	public Date getBirthDate()
	{
		return this.birthDate;
	}

	public String getMiddlename()
	{
		return this.middlename;
	}

	public void setMiddlename(final String middlename)
	{
		this.middlename = middlename;
	}

	@Access(value = AccessType.PROPERTY)
	public SectionType getSectionEligibility()
	{
		final ContactDao daoContact = new DaoFactory().getContactDao();
		final SectionType eligibility = daoContact.getSectionEligibilty(this.birthDate);
		this.sectionEligibility = eligibility;

		return this.sectionEligibility;
	}

	public void setSectionEligibility(final SectionType sectionType)
	{
		// do nothing as this is transient and readonly
	}

	public SectionType getSection()
	{
		return this.section;
	}

	public void setSection(final SectionType section)
	{
		this.section = section;
	}

	public Date getWwcExpiry()
	{
		return this.wwcExpiry;
	}

	public void setWwcExpiry(final Date wwcExpiry)
	{
		this.wwcExpiry = wwcExpiry;
	}

	public Date getPoliceCheckExpiry()
	{
		return this.policeCheckExpiry;
	}

	public void setPoliceCheckExpiry(final Date policeCheckExpiry)
	{
		this.policeCheckExpiry = policeCheckExpiry;
	}

	public Boolean getActive()
	{
		return this.active;
	}

	public String getPrefix()
	{
		return this.prefix;
	}

	public void setFirstname(final String firstname)
	{
		this.firstname = firstname;
		setFullname(this.firstname + " " + this.lastname);
	}

	public String getFirstname()
	{
		return this.firstname;
	}

	public String getLastname()
	{
		return this.lastname;
	}

	public Gender getGender()
	{
		return this.gender;
	}

	public void setPhone1(final String phoneNo)
	{
		this.phone1.setPhoneNo(phoneNo);
	}

	public void setPhone1(final Phone phone1)
	{
		this.phone1 = phone1;
	}

	public Phone getPhone1()
	{
		return this.phone1;
	}

	public Phone getPhone2()
	{
		return this.phone2;
	}

	public Phone getPhone3()
	{
		return this.phone3;
	}

	public String getHomeEmail()
	{
		return this.homeEmail;
	}

	public Age getAge()
	{
		return this.age;
	}

	public Phone getPrimaryPhone()
	{
		Phone primary = null;
		if (this.phone1.isPrimaryPhone())
		{
			primary = this.phone1;
		}
		else if (this.phone2.isPrimaryPhone())
		{
			primary = this.phone2;
		}
		else if (this.phone3.isPrimaryPhone())
		{
			primary = this.phone3;
		}
		return primary;

	}

	public void setPrimaryPhone(final Phone phoneNo)
	{
		// No Op
	}

	public void setAge(final Age age)
	{
		this.age = age;
	}

	public void setActive(final Boolean active)
	{
		this.active = active;
	}

	public void setPrefix(final String prefix)
	{
		this.prefix = prefix;
	}

	public void setBirthDate(final Date birthDate)
	{
		this.birthDate = birthDate;
	}

	public void setGender(final Gender gender)
	{
		this.gender = gender;
	}

	public void setPhone2(final Phone workPhone)
	{
		this.phone2 = workPhone;
	}

	public void setPhone3(final Phone mobile)
	{
		this.phone3 = mobile;
	}

	public void setHomeEmail(final String homeEmail)
	{
		this.homeEmail = homeEmail;
	}

	public void setWorkEmail(final String workEmail)
	{
		this.workEmail = workEmail;
	}

	public void setPreferredEmail(final PreferredEmail preferredEmail)
	{
		this.preferredEmail = preferredEmail;
	}

	public void setPreferredCommunications(final PreferredCommunications preferredCommunications)
	{
		this.preferredCommunications = preferredCommunications;
	}

	public void setAllergies(final String allergies)
	{
		this.allergies = allergies;
	}

	public void setCustodyOrder(final Boolean custodyOrder)
	{
		this.custodyOrder = custodyOrder;
	}

	public void setCustodyOrderDetails(final String custodyOrderDetails)
	{
		this.custodyOrderDetails = custodyOrderDetails;
	}

	public void setSchool(final String school)
	{
		this.school = school;
	}

	public void setIsMember(final Boolean isMember)
	{
		this.isMember = isMember;
	}

	public void setMemberNo(final String memberNo)
	{
		this.memberNo = memberNo;
	}

	public void setMemberSince(final Date memberSince)
	{
		this.memberSince = memberSince;
	}

	public void setHobbies(final String hobbies)
	{
		this.hobbies = hobbies;
	}

	public void setAffiliatedSince(final Date affiliatedSince)
	{
		this.affiliatedSince = affiliatedSince;
	}

	public void setRole(final GroupRole role)
	{
		this.groupRole = role;
	}

	public void setMedicareNo(final String medicareNo)
	{
		this.medicareNo = medicareNo;
	}

	public void setAmbulanceSubscriber(final Boolean ambulanceSubscriber)
	{
		this.ambulanceSubscriber = ambulanceSubscriber;
	}

	public void setPrivateMedicalInsurance(final Boolean privateMedicalInsurance)
	{
		this.privateMedicalInsurance = privateMedicalInsurance;
	}

	public void setPrivateMedicalFundName(final String privateMedicalFundName)
	{
		this.privateMedicalFundName = privateMedicalFundName;
	}

	public void setCurrentEmployer(final String currentEmployer)
	{
		this.currentEmployer = currentEmployer;
	}

	public void setJobTitle(final String jobTitle)
	{
		this.jobTitle = jobTitle;
	}

	public void setHasWWC(final Boolean hasWWC)
	{
		this.hasWWC = hasWWC;
	}

	public void setWwcNo(final String wwcNo)
	{
		this.wwcNo = wwcNo;
	}

	public void setHasPoliceCheck(final Boolean hasPoliceCheck)
	{
		this.hasPoliceCheck = hasPoliceCheck;
	}

	public void setHasFoodHandlingCertificate(final Boolean hasFoodHandlingCertificate)
	{
		this.hasFoodHandlingCertificate = hasFoodHandlingCertificate;
	}

	public void setHasFirstAidCertificate(final Boolean hasFirstAidCertificate)
	{
		this.hasFirstAidCertificate = hasFirstAidCertificate;
	}

	public void setTags(final Set<Tag> tags)
	{
		this.tags = tags;
	}

	@Override
	public void addTag(Tag tag)
	{
		this.tags.isEmpty();
		this.tags.add(tag);
	}

	public void setNotes(final List<Note> notes)
	{
		// If some wants the list lets force it to be read from the db.
		this.notes.isEmpty();
		this.notes = notes;
	}

	public void setActivites(final List<CommunicationLog> activites)
	{
		this.activities = activites;
	}

	public String getWorkEmail()
	{
		return this.workEmail;
	}

	public PreferredEmail getPreferredEmail()
	{
		return this.preferredEmail;
	}

	public PreferredCommunications getPreferredCommunications()
	{
		return this.preferredCommunications;
	}

	public String getAllergies()
	{
		return this.allergies;
	}

	public Boolean getCustodyOrder()
	{
		return this.custodyOrder;
	}

	public String getCustodyOrderDetails()
	{
		return this.custodyOrderDetails;
	}

	public String getSchool()
	{
		return this.school;
	}

	public Boolean getIsMember()
	{
		return this.isMember;
	}

	public String getMemberNo()
	{
		return this.memberNo;
	}

	public Date getMemberSince()
	{
		return this.memberSince;
	}

	public String getHobbies()
	{
		return this.hobbies;
	}

	public Date getAffiliatedSince()
	{
		return this.affiliatedSince;
	}

	public GroupRole getRole()
	{
		return this.groupRole;
	}

	public String getMedicareNo()
	{
		return this.medicareNo;
	}

	public Boolean getAmbulanceSubscriber()
	{
		return this.ambulanceSubscriber;
	}

	public Boolean getPrivateMedicalInsurance()
	{
		return this.privateMedicalInsurance;
	}

	public String getPrivateMedicalFundName()
	{
		return this.privateMedicalFundName;
	}

	public String getCurrentEmployer()
	{
		return this.currentEmployer;
	}

	public String getJobTitle()
	{
		return this.jobTitle;
	}

	public Boolean getHasWWC()
	{
		return this.hasWWC;
	}

	public String getWwcNo()
	{
		return this.wwcNo;
	}

	public Boolean getHasPoliceCheck()
	{
		return this.hasPoliceCheck;
	}

	public Boolean getHasFoodHandlingCertificate()
	{
		return this.hasFoodHandlingCertificate;
	}

	public Boolean getHasFirstAidCertificate()
	{
		return this.hasFirstAidCertificate;
	}

	public List<CommunicationLog> getCommunicationsLog()
	{
		// If some wants the list lets force it to be read from the db.
		this.activities.isEmpty();
		return this.activities;
	}

	public void setAddress(final Address address)
	{
		this.address = address;
	}

	public Address getAddress()
	{
		return this.address;
	}

	public void setLastname(final String lastname)
	{
		this.lastname = lastname;
		setFullname(this.firstname + " " + this.lastname);
	}

	public void setStreet(final String street)
	{
		this.address.setStreet(street);

	}

	public void setCity(final String city)
	{
		this.address.setCity(city);

	}

	public void setState(final String state)
	{
		this.address.setState(state);

	}

	public void setPostcode(final String postcode)
	{
		this.address.setPostcode(postcode);
	}

	public void setBirthDate(final String fieldValue)
	{
		final DateTimeParser[] parsers =
		{ DateTimeFormat.forPattern("yyyy-MM-dd").getParser(), DateTimeFormat.forPattern("yyyy/MM/dd").getParser() };
		final DateTimeFormatter formatter = new DateTimeFormatterBuilder().append(null, parsers).toFormatter();

		if (fieldValue != null && fieldValue.length() > 0)
		{
			final DateTime date1 = formatter.parseDateTime(fieldValue);
			setBirthDate(new java.sql.Date(date1.toDate().getTime()));
		}
	}

	public String getFullname()
	{
		return this.firstname + " " + this.lastname;
	}

	public void setFullname(final String fullname)
	{
		// we ignore this argument as fullname is always an amalgam of the
		// firstname and lastname;
		this.fullname = this.firstname + " " + this.lastname;
	}

	@PreRemove
	private void preRemove()
	{
		this.tags.clear();
		// activites.isEmpty();
		// activites.clear();
		this.notes.clear();
	}

	@Override
	public String getName()
	{
		return getFullname();
	}

	public void addNote(final Note child)
	{
		this.notes.add(child);

	}

	public Set<Relationship> getLHSRelationships()
	{
		return this.lhsrelationships;
	}

	public Boolean getDoNotSendBulkCommunications()
	{
		return this.doNotSendBulkCommunications;
	}

	public void setDoNotSendBulkCommunications(final Boolean doNotSendBulkCommunications)
	{
		this.doNotSendBulkCommunications = doNotSendBulkCommunications;
	}

	public Date getDateMemberInvested()
	{
		return this.dateMemberInvested;
	}

	public void setDateMemberInvested(final Date dateMemberInvested)
	{
		this.dateMemberInvested = dateMemberInvested;
	}

	/**
	 * Returns the users preferred email or if no preferred email then their
	 * home email. If there is no home email then returns their work email.
	 *
	 * @return
	 */
	public String getEmail()
	{
		String email;

		/**
		 * TODO: UI not yet create to allow the setting of preferred email.
		 *
		 * switch (this.preferredEmail) { case HOME: email = this.homeEmail;
		 * break; case WORK: email = this.workEmail; break; default: if
		 * (this.homeEmail == null || this.homeEmail.length() == 0) email =
		 * this.workEmail; else email = this.homeEmail; break;
		 *
		 * }
		 */

		if (this.homeEmail == null || this.homeEmail.length() == 0)
			email = this.workEmail;
		else
			email = this.homeEmail;
		return email;
	}
}
