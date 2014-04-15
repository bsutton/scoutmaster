package au.org.scoutmaster.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

/**
 * A school in the Scout Groups catchment area.
 *
 * @author bsutton
 *
 */
@Entity(name = "School")
@Table(name = "School")
@Access(AccessType.FIELD)
public class School extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * The name of the school
	 */
	@NotBlank
	@Column(unique = true)
	String name;

	/**
	 * A detailed description of the school. May be HTML.
	 */
	String description;

	/**
	 * A list of the schools locations (if it has multiple campus'
	 */
	@OneToMany(cascade = CascadeType.ALL)
	List<Address> location = new ArrayList<>();

	/**
	 * The schools principle
	 */
	@OneToOne(targetEntity = Contact.class)
	Contact principle;

	/**
	 * Advertising contact
	 */
	@OneToOne(targetEntity = Contact.class)
	Contact advertisingContact;

	/**
	 * The list of youth affiliated with our group (this includes prospects)
	 * that attend the school.
	 */
	@OneToMany(cascade = CascadeType.ALL, targetEntity = Contact.class)
	List<Contact> youth = new ArrayList<>();

	@Override
	public String getName()
	{
		return this.name;
	}

}
