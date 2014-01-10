package au.org.scoutmaster.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.joda.time.DateTime;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.SectionType;
import au.org.scoutmaster.domain.Tag;

import com.vaadin.addon.jpacontainer.JPAContainer;

public class SectionTypeDao extends JpaBaseDao<SectionType, Long> implements Dao<SectionType, Long>
{

	static private List<SectionType> sectionTypes;

	public SectionTypeDao()
	{
		// inherit the default per request em.
	}

	public SectionTypeDao(EntityManager em)
	{
		super(em);
	}

	public SectionType findByName(String name)
	{
		return super.findSingleBySingleParameter(SectionType.FIND_BY_NAME, "name", name);
	}

	/**
	 * Determines which section a youth member is elidgible for.
	 * 
	 * @param birthDate
	 * @return
	 */
	public SectionType getEligibleSection(DateTime birthDate)
	{
		SectionType eligible = null;

		cacheSectionTypes();
		for (SectionType sectionType : sectionTypes)
		{

			if (this.isEligible(sectionType, birthDate))
			{
				eligible = sectionType;
				break;
			}
		}
		assert eligible != null : "All dates should map to a valid SectionType";
		return eligible;
	}

	public  void cacheSectionTypes()
	{
		/** 
		 * We are caching the section types to get around a bug in jpa that causes 
		 * the system to lock up when trying to fetch section types from the Contact during startup.
		 * Remove the cache to see the problem :D
		 */
		if (sectionTypes == null)
		{
			sectionTypes = findAll();
		}
	}

	private boolean isEligible(SectionType sectionType, DateTime birthDate)
	{
		DateTime startingAge = sectionType.getStartingAge().getBirthDate();
		DateTime endingAge = sectionType.getEndingAge().getBirthDate();
		return (birthDate.equals(startingAge) || birthDate.isBefore(startingAge))
				&& (birthDate.isAfter(endingAge) || birthDate.equals(endingAge));
	}

	@Override
	public JPAContainer<SectionType> createVaadinContainer()
	{
		return super.createVaadinContainer();
	}

	/** 
	 * Finds and returns the tag with the same name as this section types name.
	 * 
	 * @param sectionType
	 * @return
	 */
	public Tag getTag(SectionType sectionType)
	{
		Tag tag = null;

		// Section Tag
		if (sectionType != null)
		{
			TagDao daoTag = new DaoFactory().getTagDao();
			tag = daoTag.findByName(sectionType.getName());
		}
		assert tag != null : "Database problem. Tag for SectionType " + sectionType.getName() + " is missing";
		
		return tag;
	}
}
