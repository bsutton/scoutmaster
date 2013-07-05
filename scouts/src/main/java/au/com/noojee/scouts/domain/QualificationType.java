package au.com.noojee.scouts.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/*
 * Used to define different types of qualifications required by leaders, parents and scouts.
 */
@Entity
public class QualificationType
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * A short descriptive name for the qualification Type.
	 */
	String name;
	
	/**
	 * A detailed description of the qualification potentially containing HTML formating.
	 */
	String description;
	
	/**
	 * If try then this type of qualification expires after a defined period of time.
	 */
	boolean expires;
	
	/**
	 * The default period of time that the qualifications is valid for.
	 */
	@Embedded
	Period validFor;
	

}
