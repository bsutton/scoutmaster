package au.com.noojee.scouts.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;


@Entity
public class SectionMeeting extends Event
{
	private static final long serialVersionUID = 1L;
	/**
	 * The scout section holding the meeting.
	 */
	@ManyToOne
	SectionType section;
	
	
	

}
