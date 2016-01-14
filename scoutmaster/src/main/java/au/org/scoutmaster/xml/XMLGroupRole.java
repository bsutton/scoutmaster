package au.org.scoutmaster.xml;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import au.org.scoutmaster.domain.GroupRole;
import au.org.scoutmaster.domain.Tag;

/**
 * A Role within the Scout Group.
 *
 * This class is used to import GroupSetup from an xml file.
 *
 * Typical Roles are 'Scout Leader', "President', 'Youth Member'.
 *
 * The system has a number of built in roles which may not be modified or
 * deleted but an administrator can add additional roles.
 *
 * @author bsutton
 *
 */
@Root(name = "GroupRole")
public class XMLGroupRole
{
	/**
	 * Name of the section.
	 */
	@Attribute(name = "name") // xml descriptor for GroupSetup resource file
	private String name;

	/**
	 * If a group role is one of the built in roles then this field will provide
	 * a link between the role and the BuiltIn enum. Non-builtin roles will use
	 * the Builtin type of 'Custom'.
	 */
	@Attribute(name = "enumName") // xml descriptor for GroupSetup resource
									// file
	private String enumName;

	/**
	 * Name of the section.
	 */
	@Attribute(name = "description") // xml descriptor for GroupSetup resource
										// file
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
	@ElementList(entry = "RoleTag", required = false, inline = true)
	private Set<XMLRoleTag> xmlTags = new HashSet<XMLRoleTag>();

	public Set<XMLRoleTag> getRoleTags()
	{
		return this.xmlTags;
	}

	public void setTags(List<Tag> tags)
	{
	}

	public String getName()
	{
		return this.name;
	}

	public String getEnumName()
	{
		return this.enumName;
	}

	public String getDescription()
	{
		return this.description;
	}

	public GroupRole getGroupRole()
	{

		GroupRole role = new GroupRole(this.name, GroupRole.BuiltIn.valueOf(this.enumName), this.description,
				this.primaryRole);

		for (XMLRoleTag xmlTag : this.xmlTags)
		{
			role.addTag(xmlTag.getTag());
		}

		return role;
	}

	/**
	 * Attempts to resolve each XMLRoleTag to an actual Tag.
	 *
	 * @param tags
	 */
	public void resolveTags(List<Tag> tags)
	{
		for (XMLRoleTag xmlRoleTag : this.xmlTags)
		{
			boolean found = false;
			for (Tag tag : tags)
			{
				if (xmlRoleTag.getName().equals(tag.getName()))
				{
					xmlRoleTag.setTag(tag);
					found = true;
					break;
				}
			}
			if (found != true)
				throw new RuntimeException("An invalid Tag was found in GroupRole " + this.name
						+ " whilst importing an Group Setup xml file.");
		}

	}
}
