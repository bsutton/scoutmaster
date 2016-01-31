package au.org.scoutmaster.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import org.hibernate.validator.constraints.NotBlank;

/**
 * A school in the Scout Groups catchment area.
 *
 * @author bsutton
 *
 */
@Entity(name = "School")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "School")
@Access(AccessType.FIELD)

@NamedQueries(
{ @NamedQuery(name = School.FIND_BY_NAME, query = "SELECT school FROM School school WHERE school.name = :name"), })

public class School extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	public static final String FIND_BY_NAME = "School.FindByName";

	/**
	 * The name of the school
	 */
	@NotBlank
	@Column(unique = true)
	String name;

	/**
	 * A detailed description of the school. May be HTML.
	 */
	String description;

	/**
	 * The schools primary address
	 */
	@OneToOne(cascade =
	{ CascadeType.MERGE })
	Address primaryAddress = new Address();

	/**
	 * A list of the schools locations (if it has multiple campus'
	 */
	@OneToMany()
	List<Address> locations = new ArrayList<>();

	/**
	 * The schools principle
	 */
	@OneToOne(targetEntity = Contact.class)
	Contact principle = new Contact();

	/**
	 * Advertising contact
	 */
	@OneToOne(targetEntity = Contact.class)
	Contact advertisingContact = new Contact();

	/**
	 * The list of youth affiliated with our group (this includes prospects)
	 * that attend the school.
	 */
	@OneToMany(targetEntity = Contact.class)
	List<Contact> youth = new ArrayList<>();

	/**
	 * The schools general email address
	 */
	String generalEmailAddress;

	/**
	 * The schools main phone no.
	 */
	String mainPhoneNo;

	/**
	 * School's web address.
	 */
	String webAddress;

	/**
	 * Schools Gender
	 */
	SchoolGender schoolGender = SchoolGender.COED;

	private SchoolType schoolType = SchoolType.PRIMARY;

	@Override
	public String getName()
	{
		return this.name;
	}

	public Contact getPrinciple()
	{
		return principle;
	}

	public Contact getAdvertisingContact()
	{
		return advertisingContact;
	}

	public String getPrincipleEmail()
	{
		return (principle != null ? principle.getEmail() : null);
	}

	public String getAdvertisingEmail()
	{
		return (advertisingContact != null ? advertisingContact.getEmail() : null);
	}

}
