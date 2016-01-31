package au.org.scoutmaster.dao;

import com.vaadin.addon.jpacontainer.JPAContainer;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.com.vaadinutils.dao.Path;
import au.org.scoutmaster.domain.Address_;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.School;
import au.org.scoutmaster.domain.School_;

public class SchoolDao extends JpaBaseDao<School, Long> implements Dao<School, Long>
{

	public SchoolDao()
	{
		// inherit the default per request em.
	}

	@Override
	public School findById(final Long id)
	{
		final School school = JpaBaseDao.getEntityManager().find(this.entityClass, id);
		return school;
	}

	public School findByName(final String groupName)
	{
		return super.findSingleBySingleParameter(School.FIND_BY_NAME, "name", groupName);
	}

	@Override
	public JPAContainer<School> createVaadinContainer()
	{
		final JPAContainer<School> contactContainer = super.createVaadinContainer();
		contactContainer.addNestedContainerProperty(new Path(School_.principle, Contact_.fullname).getName());
		contactContainer.addNestedContainerProperty(new Path(School_.principle, Contact_.workEmail).getName());
		contactContainer.addNestedContainerProperty(new Path(School_.advertisingContact, Contact_.fullname).getName());
		contactContainer.addNestedContainerProperty(new Path(School_.advertisingContact, Contact_.workEmail).getName());

		contactContainer.addNestedContainerProperty(new Path(School_.primaryAddress, Address_.street).getName());
		contactContainer.addNestedContainerProperty(new Path(School_.primaryAddress, Address_.city).getName());
		contactContainer.addNestedContainerProperty(new Path(School_.primaryAddress, Address_.postcode).getName());
		contactContainer.addNestedContainerProperty(new Path(School_.primaryAddress, Address_.state).getName());

		return contactContainer;
	}

}
