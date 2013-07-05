package au.com.noojee.scouts.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * A scouts section such as Joeys or Cubs
 * 
 * Larger groups may have multiple section of a given type (e.g. two Cub sections)
 * 
 * @author bsutton
 *
 */
@Entity
public class Section
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	SectionType type;

	/**
	 * The list of leaders runnnig this Section.
	 */
	@ManyToMany
	List<Contact> leaders = new ArrayList<>();
	
	/**
	 * The list of parent helpers attached to this section.
	 * 
	 * A adult helper may be attached to multiple sections
	 */
	@ManyToMany
	List<Contact> adultHelpers = new ArrayList<>();
	
	/**
	 * The list of youth members attached to this section.
	 * 
	 */
	@OneToMany
	List<Contact>	youthMembers = new ArrayList<>();
	
	/**
	 * The list of youth members that are currently transitioning from one section to another
	 * e.g. moving from cubs to scouts.
	 */
	@ManyToMany
	List<TransitionMember> transitioningYouthMembers = new ArrayList<>();
	
	/** 
	 * A list of youth members who are currently trying out to see if they would
	 * like to join this scout section.
	 */
	@OneToMany
	List<SectionTryout> trialMembers = new ArrayList<>();
	
	/**
	 * Describes the default times and locations that this section has its meetings.
	 */
	@ManyToOne
	SectionMeetingDefaults meetingDefaults;
	
	
}
