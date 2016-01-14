package au.org.scoutmaster.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import au.org.scoutmaster.domain.Tag;

@Root(name = "RoleTag")
public class XMLRoleTag
{

	@Attribute(name = "name") // xml descriptor for GroupSetup resource file
	private String name;

	// Thea actual tag this RoleTag resolves to (based on the name matching).
	private Tag tag;

	public void setTag(Tag tag)
	{
		this.tag = tag;
	}

	public Tag getTag()
	{
		return tag;
	}

	public String getName()
	{
		return name;
	}
}
