package au.com.noojee.scouts.domain;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Used to define the set of qualifications the associated leader has.
 * 
 * @author bsutton
 *
 */
@Entity
public class Qualification
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

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
