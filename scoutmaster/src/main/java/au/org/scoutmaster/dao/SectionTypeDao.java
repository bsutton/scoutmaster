package au.org.scoutmaster.dao;

import java.util.List;

import org.joda.time.DateTime;

import com.vaadin.addon.jpacontainer.JPAContainer;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.application.SMSession;
import au.org.scoutmaster.domain.SectionType;
import au.org.scoutmaster.domain.Tag;

public class SectionTypeDao extends JpaBaseDao<SectionType, Long> implements Dao<SectionType, Long>
{

	public SectionTypeDao()
	{
		// inherit the default per request em.
	}

	public SectionType findByName(final String name)
	{
		return super.findSingleBySingleParameter(SectionType.FIND_BY_NAME, "name", name);
	}

	@Override
	public List<SectionType> findAll()
	{
		return super.findList(SectionType.FIND_ALL);
	}

	/**
	 * Determines which section a youth member is elidgible for.
	 *
	 * @param birthDate
	 * @return
	 */
	public SectionType getEligibleSection(final DateTime birthDate)
	{
		SectionType eligible = null;

		// We need to guarantee a default section type. The first one should be
		// the youngest
		// Which allows for a birthDate of 0/0/0.
		List<SectionType> sectionTypes = SMSession.INSTANCE.getSectionTypeCache();
		eligible = sectionTypes.get(0);

		for (final SectionType sectionType : sectionTypes)
		{

			if (isEligible(sectionType, birthDate))
			{
				eligible = sectionType;
				break;
			}
		}
		assert eligible != null : "All dates should map to a valid SectionType";
		return eligible;
	}

	private boolean isEligible(final SectionType sectionType, final DateTime birthDate)
	{
		final DateTime startingAge = sectionType.getStartingAge().getBirthDate();
		final DateTime endingAge = sectionType.getEndingAge().getBirthDate();
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
	public Tag getTag(final SectionType sectionType)
	{
		Tag tag = null;

		// Section Tag
		if (sectionType != null)
		{
			final TagDao daoTag = new DaoFactory().getTagDao();
			tag = daoTag.findByName(sectionType.getName());
		}
		assert tag != null : "Database problem. Tag for SectionType " + sectionType.getName() + " is missing";

		return tag;
	}
}
