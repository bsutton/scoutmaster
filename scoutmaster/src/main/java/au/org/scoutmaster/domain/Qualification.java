package au.org.scoutmaster.domain;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Used to define the set of qualifications the associated leader has.
 * 
 * @author bsutton
 *
 */
@Entity(name="Qualification")
@Table(name="Qualification")
public class Qualification extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * Describes the qualification type held by this leader.
	 */
	@ManyToOne
	QualificationType type;
	
	/**
	 * The leader that has this qualifications
	 */
	@ManyToOne
	Contact leader;
	
	
	/**
	 * The date the qualification was obtained
	 */
	Date obtained;
	
	/**
	 * The date the qualification expires or null if the QualificationType doesn't expire.
	 */
	Date expires;

}
