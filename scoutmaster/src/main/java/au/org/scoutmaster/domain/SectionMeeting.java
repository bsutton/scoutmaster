package au.org.scoutmaster.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity(name="SectionMeeting")
@Table(name="SectionMeeting")
@Access(AccessType.FIELD)
public class SectionMeeting extends Event
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * The scout section holding the meeting.
	 */
	@ManyToOne
	SectionType section;
	
	
	

}
