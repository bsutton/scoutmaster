package au.org.scoutmaster.security;

import au.com.vaadinutils.crud.CrudSecurityManager;
import au.com.vaadinutils.crud.security.AllowAllSecurityManager;
import au.com.vaadinutils.crud.security.SecurityManagerFactory;
import au.org.scoutmaster.domain.access.User;

public class SecurityFactoryImpl implements SecurityManagerFactory
{
	// Logger logger = LogManager.getLogger();

	public static CrudSecurityManager defaultSecurityManager = new AllowAllSecurityManager();

	@Override
	public CrudSecurityManager buildSecurityManager(Class<?> baseCrudView)
	{
		SecurityModel model = SecurityModelFactory.createSecurityModel(baseCrudView);
		if (model == null)
		{
			return defaultSecurityManager;
		}
		return new SecurityManagerImpl(model);
	}

	/**
	 * for use outside of vaadin
	 *
	 * @param baseCrudView
	 * @param user
	 * @return
	 */
	static public CrudSecurityManager buildSecurityManager(Class<?> baseCrudView, User user)
	{
		SecurityModel model = SecurityModelFactory.createSecurityModel(baseCrudView);
		if (model == null)
		{
			return defaultSecurityManager;
		}
		return new SecurityManagerImpl(model, user);
	}
}
