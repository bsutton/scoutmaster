package au.org.scoutmaster.dao;

import javax.persistence.EntityManager;

import au.org.scoutmaster.domain.Activity;
import au.org.scoutmaster.domain.ActivityType_;
import au.org.scoutmaster.domain.Activity_;
import au.org.scoutmaster.domain.Contact_;
import au.org.scoutmaster.domain.access.User_;

import com.vaadin.addon.jpacontainer.JPAContainer;

public class ActivityDao extends JpaBaseDao<Activity, Long> implements Dao<Activity, Long>
{

	public ActivityDao()
	{
		// inherit the default per request em.
	}

	public ActivityDao(EntityManager em)
	{
		super(em);
	}


	@Override
	public JPAContainer<Activity> makeJPAContainer()
	{
		JPAContainer<Activity> container = super.makeJPAContainer(Activity.class);
		container.addNestedContainerProperty(new Path(Activity_.withContact, Contact_.lastname).getName());
		container.addNestedContainerProperty(new Path(Activity_.withContact, Contact_.firstname).getName());
		container.addNestedContainerProperty(new Path(Activity_.addedBy, User_.username).getName());
		container.addNestedContainerProperty(new Path(Activity_.type, ActivityType_.name).getName());

		return container;
	}
}
