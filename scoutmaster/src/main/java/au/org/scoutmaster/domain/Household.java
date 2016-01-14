package au.org.scoutmaster.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import org.hibernate.validator.constraints.NotBlank;

/**
 * A household is used to link a number of contacts which live at a single
 * locations
 *
 * If a member belongs to a split household then a Household should be created
 * for each family grouping and the contact should be added to both.
 *
 * @author bsutton
 *
 */
@Entity(name = "Household")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "Household")
@Access(AccessType.FIELD)
public class Household extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * The name of the household. This will normally be the families lastname.
	 * In cases where the group has more than one household with the same
	 * lastname then the name can be augmented by appending the street name
	 *
	 * e.g. Smith - Drive St.
	 */
	@NotBlank
	@Column(unique = true)
	private String name;

	/**
	 * The address of the household.
	 */
	@OneToOne(targetEntity = Address.class, cascade =
	{ CascadeType.MERGE })
	private Address location;

	@OneToOne(targetEntity = Contact.class)
	private Contact primaryContact;

	/**
	 * Members of the household and their relationship to the household.
	 */
	@OneToMany(targetEntity = Relationship.class)
	private List<Relationship> members = new ArrayList<>();

	@OneToMany(targetEntity = Note.class)
	private List<Note> notes = new ArrayList<>();

	@ManyToMany(targetEntity = Tag.class)
	private List<Tag> tags = new ArrayList<>();

	/**
	 * List of interactions with this contact.
	 */
	@OneToMany(targetEntity = CommunicationLog.class)
	private List<CommunicationLog> activites = new ArrayList<>();

	@Override
	public String getName()
	{
		return this.name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public Address getLocation()
	{
		return this.location;
	}

	public void setLocation(final Address location)
	{
		this.location = location;
	}

}
