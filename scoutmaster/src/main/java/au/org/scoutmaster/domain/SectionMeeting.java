package au.org.scoutmaster.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

@Entity(name = "SectionMeeting")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "SectionMeeting")
@Access(AccessType.FIELD)
public class SectionMeeting extends Event
{
	private static final long serialVersionUID = 1L;

	/**
	 * The scout section holding the meeting.
	 */
	@ManyToOne(targetEntity = SectionType.class)
	SectionType section;

}
