package au.org.scoutmaster.domain;

import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 * Used to represent the age of a person in Years, Months and days
 * @author bsutton
 *
 */
public class Age extends Period
{
	public Age(DateTime date)
	{
		super(new Interval(date, DateTime.now()));
	}
	
}
