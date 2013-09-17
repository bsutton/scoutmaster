package au.org.scoutmaster.domain;

import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-09-17T12:57:50.362+1000")
@StaticMetamodel(Contact.class)
public class Contact_ extends BaseEntity_ {
	public static volatile SingularAttribute<Contact, Boolean> active;
	public static volatile SingularAttribute<Contact, String> prefix;
	public static volatile SingularAttribute<Contact, String> firstname;
	public static volatile SingularAttribute<Contact, String> middlename;
	public static volatile SingularAttribute<Contact, String> lastname;
	public static volatile SingularAttribute<Contact, Date> birthDate;
	public static volatile SingularAttribute<Contact, Gender> gender;
	public static volatile SingularAttribute<Contact, Phone> phone1;
	public static volatile SingularAttribute<Contact, Phone> phone2;
	public static volatile SingularAttribute<Contact, Phone> phone3;
	public static volatile SingularAttribute<Contact, String> homeEmail;
	public static volatile SingularAttribute<Contact, String> workEmail;
	public static volatile SingularAttribute<Contact, PreferredEmail> preferredEmail;
	public static volatile SingularAttribute<Contact, PreferredCommunications> preferredCommunications;
	public static volatile SingularAttribute<Contact, String> allergies;
	public static volatile SingularAttribute<Contact, Boolean> custodyOrder;
	public static volatile SingularAttribute<Contact, String> custodyOrderDetails;
	public static volatile SingularAttribute<Contact, String> school;
	public static volatile SingularAttribute<Contact, SectionType> sectionEligibility;
	public static volatile SingularAttribute<Contact, Address> address;
	public static volatile SingularAttribute<Contact, Boolean> isMember;
	public static volatile SingularAttribute<Contact, String> memberNo;
	public static volatile SingularAttribute<Contact, Date> memberSince;
	public static volatile SingularAttribute<Contact, SectionType> section;
	public static volatile SingularAttribute<Contact, String> hobbies;
	public static volatile SingularAttribute<Contact, Date> affiliatedSince;
	public static volatile SingularAttribute<Contact, GroupRole> role;
	public static volatile SingularAttribute<Contact, String> medicareNo;
	public static volatile SingularAttribute<Contact, Boolean> ambulanceSubscriber;
	public static volatile SingularAttribute<Contact, Boolean> privateMedicalInsurance;
	public static volatile SingularAttribute<Contact, String> privateMedicalFundName;
	public static volatile SingularAttribute<Contact, String> medicalFundNo;
	public static volatile SingularAttribute<Contact, String> currentEmployer;
	public static volatile SingularAttribute<Contact, String> jobTitle;
	public static volatile SingularAttribute<Contact, Boolean> hasLicense;
	public static volatile SingularAttribute<Contact, Boolean> hasWWC;
	public static volatile SingularAttribute<Contact, Date> wwcExpiry;
	public static volatile SingularAttribute<Contact, String> wwcNo;
	public static volatile SingularAttribute<Contact, Boolean> hasPoliceCheck;
	public static volatile SingularAttribute<Contact, Date> policeCheckExpiry;
	public static volatile SingularAttribute<Contact, Boolean> hasFoodHandlingCertificate;
	public static volatile SingularAttribute<Contact, Boolean> hasFirstAidCertificate;
	public static volatile SetAttribute<Contact, Tag> tags;
	public static volatile ListAttribute<Contact, Note> notes;
	public static volatile ListAttribute<Contact, Activity> activites;
	public static volatile SingularAttribute<Contact, String> importId;
}
