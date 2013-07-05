package au.com.noojee.scouts.domain;

import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * Used to hold the set of defaults for a given section.
 * 
 * @author bsutton
 *
 */
@Entity
public class SectionMeetingDefaults
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

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
