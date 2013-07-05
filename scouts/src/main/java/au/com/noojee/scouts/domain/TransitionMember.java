package au.com.noojee.scouts.domain;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * A youth members that are currently transitioning from one section
 * to another e.g. moving from cubs to scouts.
 * 
 * @author bsutton
 * 
 */
@Entity
public class TransitionMember
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * The youth member that is currently transitioning.
	 */
	@OneToOne
	Contact youthMember;
	
	/**
	 * The contact who's job it is to supervise the youth member during transition.
	 * 
	 *  This may be null if no supervisor has been appointed.
	 */
	@ManyToOne
	Contact transitionSupervisor;
	
	/**
	 * The section they are transitioning from.
	 */
	@ManyToOne
	Section fromSection;
	
	
	/**
	 * The section they are transitioning too.
	 */
	@ManyToOne
	Section toSection;
	
	Date expectedStartDate;
	
	Date expextedCompletionDate;
	
}
