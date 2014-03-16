package au.org.scoutmaster.dao;

import javax.persistence.EntityManager;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.com.vaadinutils.dao.Path;
import au.org.scoutmaster.domain.Document;
import au.org.scoutmaster.domain.Document_;
import au.org.scoutmaster.domain.access.User_;

import com.vaadin.addon.jpacontainer.JPAContainer;

public class DocumentDao extends JpaBaseDao<Document, Long> implements Dao<Document, Long>
{

	public DocumentDao()
	{
		// inherit the default per request em.
	}

	public DocumentDao(EntityManager em)
	{
		super(em);
	}


	@Override
	public JPAContainer<Document> createVaadinContainer()
	{
		JPAContainer<Document> container = super.createVaadinContainer();
		container.addNestedContainerProperty(new Path(Document_.addedBy, User_.username).getName());

		return container;
	}
}
