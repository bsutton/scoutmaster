package au.org.scoutmaster.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.constraints.NotBlank;

/**
 * The Group entity defines a Scout or Guides Group.
 *
 * The Group is also the 'Tenant' for the JPA multi-tenant management.
 *
 * Most other entities are 'owned' by a specific Group (tenant) and therefore
 * are only visible to users that belong to the same Group.
 *
 * @author bsutton
 *
 */

@Entity
@Table(name = "`Group`")
@NamedQueries(
{ @NamedQuery(name = Group.FIND_BY_NAME, query = "SELECT group FROM Group group WHERE group.name = :name"), })
public class Group extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	@Transient
	private static final Logger logger = LogManager.getLogger(Group.class);

	public static final String PRIMARY_PHONE = "primaryPhone";

	public static final String FIND_BY_NAME = "Group.FindByName";

	/**
	 * The name of the Scout/Guide group
	 */
	@NotBlank
	@Column(unique = true)
	private String name;

	/**
	 * The Group's Type (scouts or guides)
	 */
	@Enumerated(EnumType.STRING)
	private GroupType groupType;

	/**
	 * The location of the group. We can't use the 'Address' entity as its
	 * multi-tenant and Group isn't (and you can't combine the two.
	 */

	@Size(max = 255)
	private String street = "";

	@Size(max = 255)
	private String city = "";

	@Size(max = 255)
	private String postcode = "";

	@Size(max = 255)
	private String state = "";

	@Size(max = 255)
	private String country = "";

	@Transient
	private Phone primaryPhone;

	@Embedded
	@AttributeOverrides(
	{ @AttributeOverride(name = "phoneType", column = @Column(name = "phone1PhoneType") ),
			@AttributeOverride(name = "primaryPhone", column = @Column(name = "phone1PrimaryPhone") ),
			@AttributeOverride(name = "phoneNo", column = @Column(name = "phone1PhoneNo") )

	})
	private Phone phone1 = new Phone();

	@Embedded
	@AttributeOverrides(
	{ @AttributeOverride(name = "phoneType", column = @Column(name = "phone2PhoneType") ),
			@AttributeOverride(name = "primaryPhone", column = @Column(name = "phone2PrimaryPhone") ),
			@AttributeOverride(name = "phoneNo", column = @Column(name = "phone2PhoneNo") )

	})
	private Phone phone2 = new Phone();

	@Embedded
	@AttributeOverrides(
	{ @AttributeOverride(name = "phoneType", column = @Column(name = "phone3PhoneType") ),
			@AttributeOverride(name = "primaryPhone", column = @Column(name = "phone3PrimaryPhone") ),
			@AttributeOverride(name = "phoneNo", column = @Column(name = "phone3PhoneNo") )

	})
	private Phone phone3 = new Phone();

	@Override
	public String getName()
	{
		return this.name;
	}

	public void setName(final String name)
	{
		this.name = name;

	}

	public Phone getPhone1()
	{
		return this.phone1;
	}

	public void setPhone1(final Phone phone1)
	{
		this.phone1 = phone1;
	}

	public Phone getPhone2()
	{
		return this.phone2;
	}

	public void setPhone2(final Phone phone2)
	{
		this.phone2 = phone2;
	}

	public Phone getPhone3()
	{
		return this.phone3;
	}

	public void setPhone3(final Phone phone3)
	{
		this.phone3 = phone3;
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

	public GroupType getGroupType()
	{
		return this.groupType;
	}

	public void setGroupType(GroupType groupType)
	{
		this.groupType = groupType;
	}

	public Contact getPrimaryContact()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setStreet(String street)
	{
		this.street = street;

	}

	public void setCity(String city)
	{
		this.city = city;

	}

	public void setPostcode(String postcode)
	{
		this.postcode = postcode;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

}
