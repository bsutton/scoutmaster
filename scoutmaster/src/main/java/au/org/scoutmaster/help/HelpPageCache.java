package au.org.scoutmaster.help;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
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

	static Logger logger = Logger.getLogger(HelpPageCache.class);

	static LoadingCache<HelpPageIdentifier, String> cache = CacheBuilder.newBuilder().maximumSize(10)
			.expireAfterWrite(1, TimeUnit.SECONDS)

			// TODO: put this back to 10 minutes.

			.build(new CacheLoader<HelpPageIdentifier, String>()
			{
				@Override
				public String load(final HelpPageIdentifier key) throws Exception
				{
					String page = "";
					BufferedReader in = null;
					final String url = "https://raw.github.com/wiki/bsutton/scoutmaster/Help-" + key + ".md";
					try
					{
						final URL oracle = new URL(url);
						final URLConnection yc = oracle.openConnection();
						in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
						final Markdown4jProcessor processor = new Markdown4jProcessor();
						page = processor.process(in);
						in.close();
					}
					catch (final Exception e)
					{
						HelpPageCache.logger.error(e, e);
					}
					return page;
				}
			});

	public String lookupHelpPage(final HelpPageIdentifier id) throws ExecutionException
	{
		return HelpPageCache.cache.get(id);

	}
}
