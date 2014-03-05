package au.org.scoutmaster.application;

import java.util.Map;

/**
 * Views which implement this interface will be passed any parameters
 * that were on the URI when the view was called.
 * 
 * To pass parameters add them after the view name
 * 
 * e.g. 
 * scoutmaster/#!Raffle/ID=10
 * 
 * This could be used to pass the ID (10) of the raffle.
 * If you want to pass multiple parameters add additional
 * slashes. 
 * e.g.
 * scoutmaster/#!Raffle/ID=10/Name=One
 * 
 * 
 * @author bsutton
 *
 */
public interface URIParameterListener
{

	/**
	 * passes a map with each parameter contained as a key value pair.
	 * @param parameters
	 */
	void setParameters(Map<String, String> parameters);

}
