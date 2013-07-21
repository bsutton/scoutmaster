package au.org.scoutmaster.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.joda.money.Money;

/**
 * Used to store defaults associated with a youth member trying out for scouts.
 * 
 * @author bsutton
 * 
 */
@Entity
public class SectionTryoutType extends BaseEntity
{
	private static final long serialVersionUID = 1L;

	/**
	 * The sections this tryout type applies to.
	 */
	@OneToMany(cascade = CascadeType.ALL)
	List<Section> sections = new ArrayList<>();

	/**
	 * The name used to identify this type of tryout. e.g. Three for free
	 */
	String name;

	/**
	 * A description of the tryout and any special terms and conditions.
	 */
	String description;

	/**
	 * If true then entry to this tryout requires paperwork to be completed.
	 */
	boolean paperWorkRequired;

	/**
	 * The number of weeks the tryout normally goes for.
	 */
	int weeks;

	/**
	 * The cost, if any associated with this tryout.
	 */
	Money cost;

}