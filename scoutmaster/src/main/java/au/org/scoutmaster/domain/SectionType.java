package au.org.scoutmaster.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * A section type is used to identify basic attributes about a section type.
 * 
 * Typical section types are Joeys, Cubs, Scouts Venturers but each region may
 * have different names for these sections.
 * 
 * @author bsutton
 * 
 */
@Entity
@NamedQueries(

{ @NamedQuery(name = SectionType.FIND_ALL, query = "SELECT sectiontype FROM SectionType sectiontype  order by sectiontype.startingAge.years,  sectiontype.startingAge.months, sectiontype.startingAge.days"),
	@NamedQuery(name = SectionType.FIND_BY_NAME, query = "SELECT sectiontype FROM SectionType sectiontype WHERE sectiontype.name = :name") })

public class SectionType extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	public static final String FIND_ALL = "SectionType.FindAll";

	public static final String FIND_BY_NAME = "SectionType.FindByName";

	/**
	 * The name of the section type. e.g Cubs.
	 */
	private String name;

	/**
	 * A optional description of the section type
	 */
	private String description;

	@Embedded
	@AttributeOverrides(
	{ @AttributeOverride(name = "years", column = @Column(name = "startingAgeYears")),
			@AttributeOverride(name = "months", column = @Column(name = "startingAgeMonths")),
			@AttributeOverride(name = "days", column = @Column(name = "startingAgeDays")) })
	
	private Age startingAge;

	@Embedded
	@AttributeOverrides(
	{ @AttributeOverride(name = "years", column = @Column(name = "endingAgeYears")),
			@AttributeOverride(name = "months", column = @Column(name = "endingAgeMonths")),
			@AttributeOverride(name = "days", column = @Column(name = "endingAgeDays")) })
	private Age endingAge;

	/**
	 * details the qualifications required by the section leader.
	 */
	@ManyToMany
	List<QualificationType> leaderRequirements = new ArrayList<>();
	@ManyToMany
	List<QualificationType> assistentLeaderRequirements = new ArrayList<>();
	@ManyToMany
	List<QualificationType> parentHelperRequirements = new ArrayList<>();
	
	public String getName()
	{
		return this.name;
	}
	
	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Age getStartingAge()
	{
		return startingAge;
	}

	public void setStartingAge(Age startingAge)
	{
		this.startingAge = startingAge;
	}

	public Age getEndingAge()
	{
		return endingAge;
	}

	public void setEndingAge(Age endingAge)
	{
		this.endingAge = endingAge;
	}

	public List<QualificationType> getLeaderRequirements()
	{
		return leaderRequirements;
	}

	public void setLeaderRequirements(List<QualificationType> leaderRequirements)
	{
		this.leaderRequirements = leaderRequirements;
	}

	public List<QualificationType> getParentHelperRequirements()
	{
		return parentHelperRequirements;
	}

	public void setParentHelperRequirements(List<QualificationType> parentHelperRequirements)
	{
		this.parentHelperRequirements = parentHelperRequirements;
	}

	public String toString()
	{
		return name;
	}

}
