package au.org.scoutmaster.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import org.hibernate.validator.constraints.NotBlank;

/**
 * An organisation (company, government body) that the scout group interacts
 * with in some way.
 *
 * There should also be one special organisation created for each system which
 * is the one that represents the Scout Group.
 *
 * e.g. supplier of uniforms.
 *
 * @author bsutton
 *
 */
@Entity(name = "Organisation")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "Organisation")
@Access(AccessType.FIELD)
@NamedQueries(
{ @NamedQuery(name = Organisation.FIND_OUR_SCOUT_GROUP, query = "SELECT organisation FROM Organisation organisation where organisation.isOurScoutGroup = true"), })
public class Organisation extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	public static final String FIND_OUR_SCOUT_GROUP = "Organisation.findOurScoutGroup";

	public static final String PRIMARY_PHONE = "primaryPhone";

	/**
	 * If true then this organisation represents our local scout group. This
	 * organisations should be created by the setup wizard.
	 *
	 */
	private Boolean isOurScoutGroup = false;

	/**
	 * The name of the organisation
	 */
	@NotBlank
	@Column(unique = true)
	private String name;

	/**
	 * The primary role an organisation takes in connection to the group.
	 */
	@ManyToOne(targetEntity = OrganisationType.class)
	private OrganisationType organisationType;

	/**
	 * A description of the organisation and how the group interacts with it.
	 */
	private String description;

	/**
	 * The list of contacts at the organsiation that the group associates with.
	 */
	@OneToMany(targetEntity = Contact.class)
	private List<Contact> contacts = new ArrayList<>();

	/**
	 * The location of the organisation.
	 */
	@OneToOne(targetEntity = Address.class, cascade =
	{ CascadeType.MERGE })
	private Address location = new Address();

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

	/**
	 * The list of tags used to describe the organisation.
	 */
	@ManyToMany(targetEntity = Tag.class)
	private List<Tag> tags = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, targetEntity = Note.class)
	private List<Note> notes = new ArrayList<>();

	/**
	 * List of interactions with this organisation.
	 */
	@OneToMany(targetEntity = CommunicationLog.class)
	private List<CommunicationLog> activites = new ArrayList<>();

	public Boolean isOurScoutGroup()
	{
		return this.isOurScoutGroup;
	}

	public void setOurScoutGroup(final Boolean isOurScoutGroup)
	{
		this.isOurScoutGroup = isOurScoutGroup;
	}

	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	public Address getLocation()
	{
		return this.location;
	}

	public void setName(final String name)
	{
		this.name = name;

	}

	public void setLocation(final Address location)
	{
		this.location = location;
	}

	public void addTag(final Tag tag)
	{
		this.tags.add(tag);
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

	/**
	 * Todo: implement UI to allow selection of the primary contact for an
	 * organisations. for the moment we are just choosing the first one.
	 *
	 * @return
	 */
	public Contact getPrimaryContact()
	{
		Contact contact = null;
		if (!this.contacts.isEmpty())
		{
			contact = this.contacts.get(0);
		}
		return contact;
	}

	public void setPrimaryPhone(final Phone phoneNo)
	{
		// No Op
	}

	public OrganisationType getType()
	{
		return this.organisationType;
	}

	public void setType(final OrganisationType type)
	{
		this.organisationType = type;
	}

}
