package au.org.scoutmaster.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;

/*
 * Used to define different types of qualifications required by leaders, parents and scouts.
 */
@Entity
public class QualificationType extends BaseEntity
{
	private static final long serialVersionUID = 1L;

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
	Boolean expires;
	
	/**
	 * The default period of time that the qualifications is valid for.
	 */
	@Embedded
	Period validFor;
	

}
