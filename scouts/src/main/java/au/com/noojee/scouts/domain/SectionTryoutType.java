package au.com.noojee.scouts.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.joda.money.Money;

/**
 * Used to store defaults associated with a youth member trying out for scouts.
 * 
 * @author bsutton
 *
 */
@Entity
public class SectionTryoutType
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * The sections this tryout type applies to.
	 */
	@OneToMany
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
