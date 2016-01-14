package au.org.scoutmaster.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import org.simpleframework.xml.Attribute;

/**
 * A Role within the Scout Group.
 *
 * Typical Roles are 'Scout Leader', "President', 'Youth Member'.
 *
 * The system has a number of built in roles which may not be modified or
 * deleted but an administrator can add additional roles.
 *
 * @author bsutton
 *
 */
@Entity(name = "GroupRole")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "GroupRole")
@Access(AccessType.FIELD)
@NamedQueries(
{ @NamedQuery(name = GroupRole.FIND_BY_NAME, query = "SELECT grouprole FROM GroupRole grouprole WHERE grouprole.name like :name") })
public class GroupRole extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	static public final String FIND_BY_NAME = "GroupRole.findByName";

	/**
	 * Note: this enum is bound to the field 'enumName' changing any of these
	 * names will break the database linkage.
	 *
	 * @author bsutton
	 *
	 */

	public enum BuiltIn
	{
		None, YouthMember, Parent, Guardian, SectionHelper, Volunteer, Leaders, AssistantLeader, President, Secretary, Treasurer, QuarterMaster, GroupLeader, CommitteeMember, CouncilMember, RecruitmentOfficer, Custom

	}

	GroupRole()
	{

	}

	public GroupRole(String name, BuiltIn enumName, String description, boolean primaryRole)
	{
		super();
		this.name = name;
		this.enumName = enumName;
		this.description = description;
		this.primaryRole = primaryRole;
	}

	/**
	 * Name of the section.
	 */
	@Column(unique = true)
	@Attribute(name = "name") // xml descriptor for TenantSetup resource file
	private String name;

	/**
	 * If a group role is one of the built in roles then this field will provide
	 * a link between the role and the BuiltIn enum. Non-builtin roles will use
	 * the Builtin type of 'Custom'.
	 */
	@Enumerated(EnumType.STRING)
	private BuiltIn enumName;

	/**
	 * Name of the section.
	 */
	@Column(unique = true)
	private String description;

	/**
	 * Indicates that the person with this role is the CEO of the group. Only
	 * one role may have this flag
	 */
	@Attribute(name = "primaryRole", required = false)
	private boolean primaryRole;

	/**
	 * The set of tags that should be added to a contact when this role is
	 * assigned to the contact.
	 */
	@ManyToMany(targetEntity = Tag.class, fetch = FetchType.EAGER)
	private Set<Tag> tags = new HashSet<Tag>();

	public Set<Tag> getTags()
	{
		return this.tags;
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	public BuiltIn getBuiltIn()
	{
		return this.enumName;
	}

	public String getDescription()
	{
		return this.description;
	}

	@Override
	public String toString()
	{
		// Change camel case used in enum name to separate words.
		return String.join(" ", this.name.split("(?<!^)(?=[A-Z])"));
	}

	public void addTag(Tag tag)
	{
		this.tags.add(tag);

	}

}
