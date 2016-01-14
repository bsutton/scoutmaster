package au.org.scoutmaster.xml;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Embedded;
import javax.persistence.ManyToMany;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import au.org.scoutmaster.domain.Age;
import au.org.scoutmaster.domain.Color;
import au.org.scoutmaster.domain.QualificationType;
import au.org.scoutmaster.domain.SectionType;

/**
 * A section type is used to identify basic attributes about a section type.
 *
 * This class is used to import GroupSetup from an xml file.
 *
 * Typical section types are Joeys, Cubs, Scouts Venturers but each region may
 * have different names for these sections.
 *
 * @author bsutton
 *
 */
@Root(name = "SectionType")
public class XMLSectionType
{

	/**
	 * The name of the section type. e.g Cubs.
	 */
	@Attribute(name = "name") // xml descriptor for GroupSetup resource file
	private String name;

	/**
	 * A optional description of the section type
	 */
	@Attribute(name = "description") // xml descriptor for GroupSetup resource
										// file
	private String description;

	@Element(name = "StartingAge") // xml descriptor for GroupSetup resource
	private Age startingAge;

	@Embedded
	@Element(name = "EndingAge") // xml descriptor
	private Age endingAge;

	/**
	 * The default colour to use when representing this Section graphically.
	 */
	@Element(name = "Colour") // xml descriptor for GroupSetup resource file
	private Color colour;

	/**
	 * details the qualifications required by the section leader.
	 */
	Set<QualificationType> leaderRequirements = new TreeSet<>();
	@ManyToMany(targetEntity = QualificationType.class)

	Set<QualificationType> assistentLeaderRequirements = new TreeSet<>();

	Set<QualificationType> parentHelperRequirements = new TreeSet<>();

	public String getName()
	{
		return this.name;
	}

	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(final String description)
	{
		this.description = description;
	}

	public Age getStartingAge()
	{
		return this.startingAge;
	}

	public void setStartingAge(final Age startingAge)
	{
		this.startingAge = startingAge;
	}

	public Age getEndingAge()
	{
		return this.endingAge;
	}

	public void setEndingAge(final Age endingAge)
	{
		this.endingAge = endingAge;
	}

	public Set<QualificationType> getLeaderRequirements()
	{
		return this.leaderRequirements;
	}

	public void setLeaderRequirements(final Set<QualificationType> leaderRequirements)
	{
		this.leaderRequirements = leaderRequirements;
	}

	public Set<QualificationType> getParentHelperRequirements()
	{
		return this.parentHelperRequirements;
	}

	public void setParentHelperRequirements(final Set<QualificationType> parentHelperRequirements)
	{
		this.parentHelperRequirements = parentHelperRequirements;
	}

	@Override
	public String toString()
	{
		return this.name;
	}

	public SectionType createSectionType()
	{
		return new SectionType(this.name, this.description, this.startingAge, this.endingAge, this.colour);
	}

}
