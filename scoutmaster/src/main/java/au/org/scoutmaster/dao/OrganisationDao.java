package au.org.scoutmaster.dao;

import com.vaadin.addon.jpacontainer.JPAContainer;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.org.scoutmaster.domain.Organisation;

public class OrganisationDao extends JpaBaseDao<Organisation, Long> implements Dao<Organisation, Long>
{

	public OrganisationDao()
	{
		// inherit the default per request em.
	}

	@Override
	public JPAContainer<Organisation> createVaadinContainer()
	{

		final JPAContainer<Organisation> contactContainer = super.createVaadinContainer();
		contactContainer.addNestedContainerProperty("location.street");
		contactContainer.addNestedContainerProperty("location.city");
		contactContainer.addNestedContainerProperty("location.state");
		contactContainer.addNestedContainerProperty("location.postcode");

		contactContainer.addNestedContainerProperty("phone1.phoneNo");
		contactContainer.addNestedContainerProperty("phone1.primaryPhone");
		contactContainer.addNestedContainerProperty("phone1.phoneType");

		contactContainer.addNestedContainerProperty("phone2.phoneNo");
		contactContainer.addNestedContainerProperty("phone2.primaryPhone");
		contactContainer.addNestedContainerProperty("phone2.phoneType");

		contactContainer.addNestedContainerProperty("phone3.phoneNo");
		contactContainer.addNestedContainerProperty("phone3.primaryPhone");
		contactContainer.addNestedContainerProperty("phone3.phoneType");

		return contactContainer;
	}
}
