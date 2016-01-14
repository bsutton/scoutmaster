package au.org.scoutmaster.domain;

import java.sql.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

/**
 * Used to define the set of qualifications the associated leader has.
 *
 * @author bsutton
 *
 */
@Entity(name = "Qualification")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "Qualification")
@Access(AccessType.FIELD)
public class Qualification extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * Describes the qualification type held by this leader.
	 */
	@ManyToOne(targetEntity = QualificationType.class)
	QualificationType type;

	/**
	 * The leader that has this qualifications
	 */
	@ManyToOne(targetEntity = Contact.class)
	Contact leader;

	/**
	 * The date the qualification was obtained
	 */
	Date obtained;

	/**
	 * The date the qualification expires or null if the QualificationType
	 * doesn't expire.
	 */
	Date expires;

	@Override
	public String getName()
	{
		return this.leader.getFullname() + " " + this.type.getName();
	}

}
