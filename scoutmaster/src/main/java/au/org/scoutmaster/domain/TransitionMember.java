package au.org.scoutmaster.domain;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * A youth members that are currently transitioning from one section to another
 * e.g. moving from cubs to scouts.
 * 
 * @author bsutton
 * 
 */
@Entity(name="TransitionMember")
@Table(name="Activity")
public class TransitionMember extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * The youth member that is currently transitioning.
	 */
	@OneToOne
	Contact youthMember;

	/**
	 * The contact who's job it is to supervise the youth member during
	 * transition.
	 * 
	 * This may be null if no supervisor has been appointed.
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

	@Override
	public String getName()
	{
		return youthMember.getFullname();
	}

}
