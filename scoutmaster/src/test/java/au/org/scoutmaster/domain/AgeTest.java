package au.org.scoutmaster.domain;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

public class AgeTest
{

	@Test
	public void test()
	{
		Age tooYoungStart = new Age(0, 0, 0);
		Age tooYoungEnd = new Age(5, 11, 30);
		
		Assert.assertTrue(test(tooYoungStart, tooYoungEnd, new Age(0, 0, 0)));
		Assert.assertTrue(test(tooYoungStart, tooYoungEnd, new Age(2, 1, 1)));
		Assert.assertTrue(test(tooYoungStart, tooYoungEnd, new Age(5, 11, 30)));
		Assert.assertFalse(test(tooYoungStart, tooYoungEnd, new Age(6, 0, 0)));

		Age joeyStart = new Age(6, 0, 0);
		Age joeyEnd = new Age(7, 11, 30);

		Assert.assertFalse(test(joeyStart, joeyEnd, new Age(5, 11, 30)));
		Assert.assertTrue(test(joeyStart, joeyEnd, new Age(6, 0, 0)));
		Assert.assertTrue(test(joeyStart, joeyEnd, new Age(7, 1, 1)));
		Assert.assertTrue(test(joeyStart, joeyEnd, new Age(7, 11, 30)));
		Assert.assertFalse(test(joeyStart, joeyEnd, new Age(8, 0, 0)));

	}

	boolean test(Age start, Age end, Age person)
	{
		DateTime birthDate = person.getBirthDate();
		
		DateTime startingAge = start.getBirthDate();
		DateTime endingAge = end.getBirthDate();
		
		System.out.println("Start:" + start.toString() + " " + startingAge);
		System.out.println("End: " + end.toString() + " " + endingAge);
		System.out.println("Person:" + person.toString() + " " + person);

		boolean result = (birthDate.equals(startingAge) || birthDate.isBefore(startingAge))
				&& (birthDate.isAfter(endingAge) || birthDate.equals(endingAge));
		System.out.println("Result:" + result);
		
		return result;
	}

}
