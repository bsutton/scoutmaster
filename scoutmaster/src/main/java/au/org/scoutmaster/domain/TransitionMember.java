package au.org.scoutmaster.domain;

import java.sql.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

/**
 * A youth members that are currently transitioning from one section to another
 * e.g. moving from cubs to scouts.
 *
 * @author bsutton
 *
 */
@Entity(name = "TransitionMember")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "TransitionMember")
@Access(AccessType.FIELD)
public class TransitionMember extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * The youth member that is currently transitioning.
	 */
	@OneToOne(targetEntity = Contact.class)
	Contact youthMember;

	/**
	 * The contact who's job it is to supervise the youth member during
	 * transition.
	 *
	 * This may be null if no supervisor has been appointed.
	 */
	@ManyToOne(targetEntity = Contact.class)
	Contact transitionSupervisor;

	/**
	 * The section they are transitioning from.
	 */
	@ManyToOne(targetEntity = Section.class)
	Section fromSection;

	/**
	 * The section they are transitioning too.
	 */
	@ManyToOne(targetEntity = Section.class)
	Section toSection;

	Date expectedStartDate;

	Date expextedCompletionDate;

	@Override
	public String getName()
	{
		return this.youthMember.getFullname();
	}

}
