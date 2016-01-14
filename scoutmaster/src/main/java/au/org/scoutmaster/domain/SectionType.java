package au.org.scoutmaster.domain;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import org.hibernate.validator.constraints.NotBlank;

/**
 * A section type is used to identify basic attributes about a section type.
 *
 * Typical section types are Joeys, Cubs, Scouts Venturers but each region may
 * have different names for these sections.
 *
 * @author bsutton
 *
 */
@Entity(name = "SectionType")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "SectionType")
@Access(AccessType.FIELD)
@NamedQueries(
{ @NamedQuery(name = SectionType.FIND_BY_NAME, query = "SELECT sectiontype FROM SectionType sectiontype WHERE sectiontype.name = :name"),
		@NamedQuery(name = SectionType.FIND_ALL, query = "SELECT sectiontype FROM SectionType sectiontype order by sectiontype.startingAge") }

)
public class SectionType extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	public static final String FIND_BY_NAME = "SectionType.FindByName";
	public static final String FIND_ALL = "SectionType.FindAll";

	/**
	 * The name of the section type. e.g Cubs.
	 */
	@NotBlank
	@Column(unique = true)
	private String name;

	/**
	 * A optional description of the section type
	 */
	@NotBlank
	private String description;

	@Embedded
	@AttributeOverrides(
	{ @AttributeOverride(name = "years", column = @Column(name = "startingAgeYears") ),
			@AttributeOverride(name = "months", column = @Column(name = "startingAgeMonths") ),
			@AttributeOverride(name = "days", column = @Column(name = "startingAgeDays") ) })
	private Age startingAge;

	@Embedded
	@AttributeOverrides(
	{ @AttributeOverride(name = "years", column = @Column(name = "endingAgeYears") ),
			@AttributeOverride(name = "months", column = @Column(name = "endingAgeMonths") ),
			@AttributeOverride(name = "days", column = @Column(name = "endingAgeDays") ) })
	private Age endingAge;

	/**
	 * The default colour to use when representing this Section graphically.
	 */
	@Embedded
	private Color colour;

	/**
	 * details the qualifications required by the section leader.
	 */
	@ManyToMany(targetEntity = QualificationType.class)
	Set<QualificationType> leaderRequirements = new TreeSet<>();
	@ManyToMany(targetEntity = QualificationType.class)
	Set<QualificationType> assistentLeaderRequirements = new TreeSet<>();
	@ManyToMany(targetEntity = QualificationType.class)
	Set<QualificationType> parentHelperRequirements = new TreeSet<>();

	public SectionType()
	{

	}

	public SectionType(String name, String description, Age startingAge, Age endingAge, Color colour)
	{
		this.name = name;
		this.description = description;
		this.startingAge = startingAge;
		this.endingAge = endingAge;
		this.colour = colour;
	}

	@Override
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

}
