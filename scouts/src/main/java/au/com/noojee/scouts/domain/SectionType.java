package au.com.noojee.scouts.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 * A section type is used to identify basic attributes about a section type.
 * 
 * Typical section types are Joeys, Cubs, Scouts Venturers but each region may have different names 
 * for these sections.
 * 
 * @author bsutton
 *
 */
@Entity
public class SectionType
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * The name of the section type. e.g Cubs.
	 */
	String name;
	
	/**
	 * A optional description of the section type
	 */
	String description;
	
	@Embedded
	@AttributeOverrides ({
		@AttributeOverride(name="years", column=@Column(name="startingAgeYears")),
		@AttributeOverride(name="months", column=@Column(name="startingAgeMonths")),
		@AttributeOverride(name="days", column=@Column(name="startingAgeDays"))
	})
	Period startingAge;

	@Embedded
	@AttributeOverrides ({
		@AttributeOverride(name="years", column=@Column(name="endingAgeYears")),
		@AttributeOverride(name="months", column=@Column(name="endingAgeMonths")),
		@AttributeOverride(name="days", column=@Column(name="endingAgeDays"))
	})
	Period endingAge;
	
	
	/**
	 * details the qualifications required by the section leader.
	 */
	@ManyToMany
	List<QualificationType> leaderRequirements = new ArrayList<>();
	@ManyToMany
	List<QualificationType> assistentLeaderRequirements = new ArrayList<>();
	@ManyToMany
	List<QualificationType> parentHelperRequirements = new ArrayList<>();
	
}
