package au.org.scoutmaster.xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import au.org.scoutmaster.domain.Tag;

/**
 * Tags are used to group and identify certain attributes of a contact,
 * organisation, household or school
 *
 * This class is used to import GroupSetup from an xml file.
 *
 *
 *
 * @author bsutton
 *
 */
@Root(name = "Tag")
public class XMLTag
{

	@Attribute(name = "name") // xml descriptor for GroupSetup resource file
	String name;

	@Attribute(name = "description") // xml descriptor for GroupSetup resource
										// file
	String description;

	/**
	 * Indicates that this is a builtin tag and therefore it may not be deleted
	 */
	Boolean builtin = new Boolean(false);

	/*
	 * Non detachable tags are a special class of tags which are automatically
	 * assigned to an entity based on some other entities property. For instance
	 * a Contact which is a Youth Member would also be automatically assigned to
	 * the tag Youth Member. This is some what redundant but the idea is to make
	 * searching for entities by a tag all encompassing. i.e. all major
	 * attributes of an entity are cross referenced by a tag for the purposes of
	 * searching.
	 *
	 * Only builtin tags may be NON-detachable.
	 */
	Boolean detachable = new Boolean(true);

	public String getName()
	{
		return this.name;
	}

	public String getDescription()
	{
		return this.description;
	}

	public Boolean getBuiltin()
	{
		return this.builtin;
	}

	public Boolean getDetachable()
	{
		return this.detachable;
	}

	public Tag createTag()
	{
		return new Tag(this.name, this.description, this.builtin);
	}
}
