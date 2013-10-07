package au.org.scoutmaster.domain;



import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * A Role within the Scout Group.
 * 
 * Typical Roles are 'Scout Leader', "President', 'Youth Member'.
 * 
 * The system has a number of built in roles which may not be modified or deleted 
 * but an administrator can add additional roles.
 * 
 * @author bsutton
 *
 */
@Entity(name="GroupRole")
@Table(name="GroupRole")

@NamedQueries(
{
		@NamedQuery(name = GroupRole.FIND_BY_NAME, query = "SELECT grouprole FROM GroupRole grouprole WHERE grouprole.name like :name") 
})

public class GroupRole extends BaseEntity
{
	private static final long serialVersionUID = 1L;
	
	static public final String FIND_BY_NAME = "GroupRole.findByName";

	
	public enum BuiltIn
	{
		None, YouthMember, Parent, Gardian, SectionHelper, Volunteer, Leader, AssistantLeader, President, Secretary, Treasurer, QuarterMaster, GroupLeader, CommitteeMember, RecruitmentOfficer, Supplier, CommunitySupporter, Other
	
	}


	/**
	 * Name of the section.
	 */
	@Column(unique=true)
	private String name;
	
	
	/**
	 * If a group role is one of the built in roles then this field
	 * will provide a link between the role and the BuiltIn enum.
	 * Non-builtin roles will use the Builtin type of 'Other'.
	 */
	private BuiltIn builtIn;
	

	/**
	 * Name of the section.
	 */
	@Column(unique=true)
	private String description;
	
	
	/**
	 * The set of tags that should be added to a contact when this role is
	 * assigned to the contact.
	 */
	private Set<Tag> tags = new HashSet<Tag>();


	public Set<Tag> getTags()
	{
		return tags;
	}


	public String getName()
	{
		return name;
	}


	public BuiltIn getBuiltIn()
	{
		return builtIn;
	}


	public String getDescription()
	{
		return description;
	}
	
}
