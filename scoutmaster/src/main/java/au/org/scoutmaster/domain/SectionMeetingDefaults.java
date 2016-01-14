package au.org.scoutmaster.domain;

import java.sql.Time;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Used to hold the set of defaults for a given section.
 *
 * @author bsutton
 *
 */

@Entity(name = "SectionMeetingDefaults")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "SectionMeetingDefaults")
@Access(AccessType.FIELD)
public class SectionMeetingDefaults extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * The section these defaults apply to
	 */
	@ManyToOne(targetEntity = Section.class)
	Section section;

	/**
	 * The night this section usually meets on.
	 */
	Night meetingNight;

	/**
	 * The normal meeting start time
	 */
	Time startTime;

	/**
	 * The normal meeting end time
	 */
	Time EndTime;

	/**
	 * A short default description used when summarizing the meeting.
	 */
	@NotBlank
	String meetingSubject;

	/**
	 * A default detailed description of the meeting.
	 */
	String meetingDetails;

	/**
	 * The default location of the meeting.
	 */
	@ManyToOne(targetEntity = Address.class)
	Address location;

	@Override
	@edu.umd.cs.findbugs.annotations.SuppressWarnings(value =
	{ "NP_UNWRITTEN_FIELD", "UWF_UNWRITTEN_FIELD" }, justification = "JPA injection")
	public String getName()
	{
		return this.section.getName() + " " + this.meetingNight.toString() + " " + this.meetingSubject;
	}

}
