package au.org.scoutmaster.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.money.Money;

/**
 * Used to store defaults associated with a youth member trying out for scouts.
 *
 * @author bsutton
 *
 */
@Entity(name = "SectionTryoutType")
@Multitenant
@TenantDiscriminatorColumn(name = "Group_ID")
@Table(name = "SectionTryoutType")
@Access(AccessType.FIELD)
public class SectionTryoutType extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * The sections this tryout type applies to.
	 */
	@OneToMany(targetEntity = Section.class)
	List<Section> sections = new ArrayList<>();

	/**
	 * The name used to identify this type of tryout. e.g. Three for free
	 */
	@NotBlank
	@Column(unique = true)
	String name;

	/**
	 * A description of the tryout and any special terms and conditions.
	 */
	@NotBlank
	String description;

	/**
	 * If true then entry to this tryout requires paperwork to be completed.
	 */
	Boolean paperWorkRequired;

	/**
	 * The number of weeks the tryout normally goes for.
	 */
	@Min(value = 1)
	Integer weeks;

	/**
	 * The cost, if any associated with this tryout.
	 */
	Money cost;

	@Override
	public String getName()
	{
		return this.name;
	}

}
