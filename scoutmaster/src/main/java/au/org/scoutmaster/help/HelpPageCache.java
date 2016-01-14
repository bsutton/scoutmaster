package au.org.scoutmaster.help;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.markdown4j.Markdown4jProcessor;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Caches a help page after retrieving it from the github wiki and converting it
 * from mark up to html.
 *
 * @author bsutton
 *
 */
public class HelpPageCache
{

	static Logger logger = LogManager.getLogger();

	static LoadingCache<Enum, String> cache = CacheBuilder.newBuilder().maximumSize(10)
			.expireAfterWrite(1, TimeUnit.SECONDS)

			// TODO: put this back to 10 minutes.

			.build(new CacheLoader<Enum, String>()
			{
				@Override
				public String load(final Enum helpId) throws Exception
				{
					String page = "";
					BufferedReader in = null;
					final String url = getHelpPageURL(helpId);
					try
					{
						final URL oracle = new URL(url);
						final URLConnection yc = oracle.openConnection();
						in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
						final Markdown4jProcessor processor = new Markdown4jProcessor();
						page = processor.process(in);
						in.close();
					}
					catch (FileNotFoundException e)
					{
						logger.warn("The Help page: " + helpId + " was not found. Please create it at: " + url);
					}
					catch (final Exception e)
					{
						HelpPageCache.logger.error(e, e);
					}
					return page;
				}
			});

	/**
	 * Retrieves the help text for the given Id.
	 *
	 * @param helpId
	 * @return
	 * @throws ExecutionException
	 */
	static public String lookupHelpPage(final Enum helpId) throws ExecutionException
	{
		return HelpPageCache.cache.get(helpId);

	}

	static public String getHelpPageURL(final Enum helpId)
	{
		return "https://raw.githubusercontent.com/wiki/bsutton/scoutmaster/Help-" + helpId + ".md";
	}
}
