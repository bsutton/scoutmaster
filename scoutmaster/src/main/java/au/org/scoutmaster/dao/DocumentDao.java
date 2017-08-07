package au.org.scoutmaster.dao;

import com.vaadin.addon.jpacontainer.JPAContainer;

import au.com.vaadinutils.dao.JpaBaseDao;
import au.com.vaadinutils.dao.Path;
import au.org.scoutmaster.domain.Document;
import au.org.scoutmaster.domain.Document_;
import au.org.scoutmaster.domain.security.User_;

public class DocumentDao extends JpaBaseDao<Document, Long> implements Dao<Document, Long>
{

	public DocumentDao()
	{
		// inherit the default per request em.
	}



	@Override
	public JPAContainer<Document> createVaadinContainer()
	{
		final JPAContainer<Document> container = super.createVaadinContainer();
		container.addNestedContainerProperty(new Path(Document_.addedBy, User_.username).getName());

		return container;
	}
}
