package au.org.scoutmaster.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * A scouts section such as Joeys or Cubs
 * 
 * Larger groups may have multiple section of a given type (e.g. two Cub sections)
 * 
 * @author bsutton
 *
 */
@Entity(name="Section")
@Table(name="Section")
@Access(AccessType.FIELD)
public class Section extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * Name of the section.
	 */
	@Column(unique=true)
	String name;
	
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
	@OneToMany(cascade = CascadeType.ALL)
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
	@OneToMany(cascade = CascadeType.ALL)
	List<SectionTryout> trialMembers = new ArrayList<>();
	
	/**
	 * Describes the default times and locations that this section has its meetings.
	 */
	@ManyToOne
	SectionMeetingDefaults meetingDefaults;

	@Override
	public String getName()
	{
		return name;
	}
	
	
}
