package au.com.noojee.scouts.domain;

/**
 * Used to represent the age of a person in Years, Months and days
 * @author bsutton
 *
 */
public class Age extends Period
{
	private Age(int years, int months, int days)
	{
		super(years, months, days);
	}
}
