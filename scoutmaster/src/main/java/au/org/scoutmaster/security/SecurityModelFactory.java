package au.org.scoutmaster.security;

import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import au.com.vaadinutils.menu.Menu;
import au.com.vaadinutils.menu.Menus;
import au.org.scoutmaster.security.annotations.iFeature;

public class SecurityModelFactory
{
	static private Logger logger = LogManager.getLogger();

	final static LoadingCache<Class<?>, Optional<SecurityModel>> cache = CacheBuilder.newBuilder().maximumSize(500)
			.expireAfterWrite(60, TimeUnit.MINUTES)

			.build(new CacheLoader<Class<?>, Optional<SecurityModel>>()
			{
				@Override
				public Optional<SecurityModel> load(Class<?> view) throws Exception
				{

					SecurityModel securityModel = readAnnotations(view);
					if (securityModel == null)
					{
						logger.warn("No annotations found on class " + view);
						// no annotations were found, so return absent
						return Optional.absent();
					}

					return Optional.of(securityModel);
				}

			});

	static public void flushCache()
	{
		cache.invalidateAll();
	}

	static public SecurityModel createSecurityModel(Class<?> baseCrudViewClass)
	{
		try
		{
			// If the cache is empty this will cause the above 'load' method to
			// be called
			// resulting in the SecurityModel being loaded.
			return cache.get(baseCrudViewClass).orNull();
		}
		catch (Exception e)
		{
			logger.error(e, e);
		}
		return null;

	}

	/**
	 * read the annotation attached to the given view?
	 *
	 * @param baseCrudView
	 * @param securityModel
	 * @return
	 * @throws SecurityException
	 */
	static private SecurityModel readAnnotations(Class<?> baseCrudView) throws SecurityException
	{
		SecurityModel securityModel = null;

		Menu menu = null;
		Menus menus = baseCrudView.getAnnotation(Menus.class);
		if (menus != null)
		{
			if (menus.menus().length > 0)
			{
				menu = menus.menus()[0];
			}
		}
		else
		{
			menu = baseCrudView.getAnnotation(Menu.class);
		}
		String menuName = "";
		if (menu != null)
		{
			menuName = menu.display();
		}

		for (Annotation annotation : baseCrudView.getAnnotations())
		{
			if (annotation instanceof iFeature)
			{
				iFeature feature = ((iFeature) annotation);
				securityModel = new SecurityModel(feature);
			}
		}
		return securityModel;
	}

	// /**
	// * convert class to an identifier, in this case SimpleName also ensure
	// that
	// * there aren't 2 classes that have the same SimpleName
	// *
	// * @param menuName
	// *
	// * @param clazz
	// * @return
	// */
	// @SuppressWarnings("rawtypes")
	// static private String classToFeatureName(String menuName, Class clazz)
	// {
	// Class known = knownClasses.get(clazz.getSimpleName());
	// if (known != null && known != clazz)
	// {
	// throw new RuntimeException("A non uniqueue Feature has be found." +
	// clazz.getCanonicalName() + " and "
	// + known + " have the same simple name");
	// }
	// String fullName = clazz.getSimpleName();
	// if (menuName.length() > 0)
	// {
	// fullName = menuName + " (" + fullName + ")";
	// }
	// return fullName;
	// }
}
