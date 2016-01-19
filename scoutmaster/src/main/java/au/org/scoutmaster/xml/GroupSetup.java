package au.org.scoutmaster.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.xml.sax.SAXException;

import au.org.scoutmaster.domain.GroupRole;
import au.org.scoutmaster.domain.GroupType;
import au.org.scoutmaster.domain.SectionType;
import au.org.scoutmaster.domain.Tag;
import au.org.scoutmaster.servlets.VaadinServlet;

@Root(name = "Group")
public class GroupSetup
{
	// We just parse this once but if the country changes we need to re-do it.
	private static GroupSetup self = null;
	private static String country = null;
	private static GroupType groupType = null;

	@ElementList(name = "SectionType", inline = true, type = XMLSectionType.class)
	private List<XMLSectionType> xmlSectionTypes;

	@ElementList(name = "Tag", inline = true, type = XMLTag.class)
	private List<XMLTag> xmlTags;

	@ElementList(name = "GroupRoles", inline = true, type = XMLGroupRole.class)
	private List<XMLGroupRole> xmlGroupRoles;
	private List<Tag> tags;
	private List<GroupRole> groupRoles;
	private ArrayList<SectionType> sectionTypes;

	public List<SectionType> getSectionTypes()
	{
		if (sectionTypes == null)
		{
			sectionTypes = new ArrayList<>();

			for (XMLSectionType xmlSectionType : this.xmlSectionTypes)
			{
				sectionTypes.add(xmlSectionType.createSectionType());
			}
		}
		return sectionTypes;
	}

	public List<Tag> getTags()
	{
		if (tags == null)
		{
			tags = new ArrayList<>();

			for (XMLTag xmlTag : this.xmlTags)
			{
				tags.add(xmlTag.createTag());
			}
		}
		return tags;
	}

	public List<GroupRole> getGroupRoles()
	{
		if (groupRoles == null)
		{
			groupRoles = new ArrayList<>();

			for (XMLGroupRole xmlRole : this.xmlGroupRoles)
			{
				groupRoles.add(xmlRole.getGroupRole());
			}
		}
		return groupRoles;
	}

	static GroupSetup parse(File xml) throws SAXException
	{
		Serializer serializer = new Persister();

		GroupSetup setup;
		try
		{
			setup = serializer.read(GroupSetup.class, xml);

			// Resolve tag references in the Group Roles
			for (XMLGroupRole xmlRole : setup.getXMLGroupRoles())
			{
				xmlRole.resolveTags(setup.getTags());
			}

			return setup;
		}
		catch (Exception e)
		{

			// A little ugly - but don't want callers to have to deal with yet
			// another exception type
			throw new SAXException(e);
		}
	}

	private List<XMLGroupRole> getXMLGroupRoles()
	{
		return this.xmlGroupRoles;
	}

	public static GroupSetup load(GroupType groupType, String country) throws IOException, SAXException
	{
		if (self == null || country.equals(GroupSetup.country) || groupType != GroupSetup.groupType)
		{
			self = null;
			GroupSetup.country = country;

			File xmlFile = null;

			xmlFile = getXMLFile(groupType, country);
			if (!xmlFile.exists())
			{
				// No country specific file so lets get the default
				xmlFile = getXMLFile(groupType, "Default");
			}

			if (xmlFile != null)
				self = parse(xmlFile);
		}

		return self;

	}

	private static File getXMLFile(GroupType groupType, String country)
	{
		final String xmlPath = VaadinServlet.getCurrent().getServletContext()
				.getRealPath(new File("/WEB-INF/classes/GroupSetup", country).getPath());

		// Use this version for unit testing.
		// String xmlPath = "src/main/resources/GroupSetup/" + country;
		return new File(xmlPath, groupType.name() + ".xml");
	}

}
