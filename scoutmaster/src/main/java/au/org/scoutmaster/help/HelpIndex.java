package au.org.scoutmaster.help;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class HelpIndex
{

	static Logger logger = Logger.getLogger(HelpIndex.class);

	static LoadingCache<Long, Map<HelpPageIdentifier, String>> cache = CacheBuilder.newBuilder().maximumSize(2)
			.expireAfterWrite(10, TimeUnit.MINUTES)

			.build(new CacheLoader<Long, Map<HelpPageIdentifier, String>>()
			{
				public Map<HelpPageIdentifier, String> load(Long key) throws Exception
				{
					Map<HelpPageIdentifier, String> helpMap = new HashMap<HelpPageIdentifier, String>();

					BufferedReader in = null;
					try
					{
						int indexPageId = 4155;

						URL oracle = new URL("https://wiki.noojee.com.au/help.php?page_id=" + indexPageId);
						URLConnection yc = oracle.openConnection();
						in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
						String inputLine;
						while ((inputLine = in.readLine()) != null)
						{

							if (inputLine.contains("<p>") && inputLine.contains("="))
							{
								try
								{
									HelpPageIdentifier helpPageIdentifier = HelpPageIdentifier.valueOf(inputLine
											.substring(inputLine.indexOf("<p>") + 3, inputLine.indexOf("=")));
									String helpPageId = inputLine.substring(inputLine.indexOf("=") + 1,
											inputLine.indexOf("</p>"));

									helpMap.put(helpPageIdentifier, helpPageId);
								}
								catch (Exception e)
								{
									logger.warn("This help page is probably no-longer required");
									logger.warn(e);
								}
							}
						}
						in.close();
					}
					catch (Exception e)
					{
						logger.error(e, e);
					}
					return helpMap;
				}
			});

	public String lookupHelpIndex(HelpPageIdentifier id) throws ExecutionException
	{
		return cache.get(1l).get(id);

	}
}
