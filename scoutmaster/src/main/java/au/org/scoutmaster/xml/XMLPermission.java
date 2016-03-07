package au.org.scoutmaster.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import au.org.scoutmaster.domain.access.Permission;

/**
 * Permissions are used to define what features a given Role has access to.
 *
 * This class is used to import GroupSetup from an xml file.
 *
 *
 *
 * @author bsutton
 *
 */
@Root(name = "Permission")
public class XMLPermission
{

	@Attribute(name = "name") // xml descriptor for GroupSetup resource file
	String name;

	public String getName()
	{
		return this.name;
	}

	public Permission createPermission()
	{
		return new Permission(this.name);
	}
}
