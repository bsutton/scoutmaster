package au.org.scoutmaster.application;

import java.util.List;

import javax.persistence.EntityManager;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

import au.com.vaadinutils.dao.EntityManagerProvider;
import au.org.scoutmaster.dao.DaoFactory;
import au.org.scoutmaster.dao.SectionTypeDao;
import au.org.scoutmaster.domain.Group;
import au.org.scoutmaster.domain.SectionType;
import au.org.scoutmaster.domain.access.User;

public enum SMSession
{
	INSTANCE;

	static private final String USER = "user";

	/**
	 * Holds the Group (used by JPA for the Tenant) of the currently logged in
	 * user.
	 */
	private Group group;

	/**
	 * Cache of section types to get around some odd jpa bug.
	 */
	private List<SectionType> sectionTypes;

	public User getLoggedInUser()
	{
		return (User) UI.getCurrent().getSession().getAttribute(SMSession.USER);
	}

	public void setLoggedInUser(final User user)
	{
		final VaadinSession session = UI.getCurrent().getSession();

		if (user != null)
		{
			session.setAttribute(SMSession.USER, user);
			// push the user into the servlet session as well
			// so we can write session history when the session collapses.
			session.getSession().setAttribute(SMSession.USER, user);

			setGroup(user.getGroup());

			/**
			 * We are caching the section types to get around a bug in jpa that
			 * causes the system to lock up when trying to fetch section types
			 * from the Contact during startup. Remove the cache to see the
			 * problem :D
			 */
			final SectionTypeDao daoSectionType = new DaoFactory().getSectionTypeDao();
			sectionTypes = daoSectionType.findAll();
		}
		else
		{
			session.setAttribute(SMSession.USER, null);
			session.getSession().removeAttribute(SMSession.USER);
			this.group = null;
		}

	}

	public void setGroup(Group group)
	{
		// We now know who the tenant (group) is so we can make the transition
		// to
		// JPA based Tenant management.
		EntityManager em = EntityManagerProvider.getEntityManager();
		em.setProperty("eclipselink.tenant-id", "" + group.getId());

		this.group = group;
	}

	/**
	 *
	 * @return The Group (JPA tenant) of the currently logged in user or null if
	 *         there is no logged in user.
	 */
	public Group getGroup()
	{
		return group;
	}

	public List<SectionType> getSectionTypeCache()
	{
		return sectionTypes;

	}

}
