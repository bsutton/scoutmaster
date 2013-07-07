package au.org.scoutmaster.domain;

import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Used to hold the set of defaults for a given section.
 * 
 * @author bsutton
 *
 */
@Entity
public class SectionMeetingDefaults extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/** 
	 * The section these defaults apply to
	 */
	@ManyToOne
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
	String meetingSubject;
	
	/**
	 * A default detailed description of the meeting. 
	 */
	String meetingDetails;
	
	/** 
	 * The default location of the meeting.
	 */
	@ManyToOne
	Address location;

}
