package au.org.scoutmaster.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

/**
 * A scouts section such as Joeys or Cubs
 *
 * Larger groups may have multiple section of a given type (e.g. two Cub
 * sections)
 *
 * @author bsutton
 *
 */
@Entity(name = "Section")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "Section")
@Access(AccessType.FIELD)
public class Section extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * Name of the section.
	 */
	@Column(unique = true)
	String name;

	@ManyToOne(targetEntity = SectionType.class)
	SectionType type;

	/**
	 * The list of leaders runnnig this Section.
	 */
	@ManyToMany(targetEntity = Contact.class)
	List<Contact> leaders = new ArrayList<>();

	/**
	 * The list of parent helpers attached to this section.
	 *
	 * A adult helper may be attached to multiple sections
	 */
	@ManyToMany(targetEntity = Contact.class)
	List<Contact> adultHelpers = new ArrayList<>();

	/**
	 * The list of youth members attached to this section.
	 *
	 */
	@OneToMany(targetEntity = Contact.class)
	List<Contact> youthMembers = new ArrayList<>();

	/**
	 * The list of youth members that are currently transitioning from one
	 * section to another e.g. moving from cubs to scouts.
	 */
	@ManyToMany(targetEntity = TransitionMember.class)
	List<TransitionMember> transitioningYouthMembers = new ArrayList<>();

	/**
	 * A list of youth members who are currently trying out to see if they would
	 * like to join this scout section.
	 */
	@OneToMany(targetEntity = SectionTryout.class)
	List<SectionTryout> trialMembers = new ArrayList<>();

	/**
	 * Describes the default times and locations that this section has its
	 * meetings.
	 */
	@ManyToOne(targetEntity = SectionMeetingDefaults.class)
	SectionMeetingDefaults meetingDefaults;

	@Override
	public String getName()
	{
		return this.name;
	}

}
