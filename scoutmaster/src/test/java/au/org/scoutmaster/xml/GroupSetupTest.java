package au.org.scoutmaster.xml;

import java.io.IOException;

import org.junit.Test;
import org.xml.sax.SAXException;

import au.org.scoutmaster.domain.GroupType;

public class GroupSetupTest
{

	@Test
	public void test()
	{
		try
		{
			GroupSetup.load(GroupType.Scouts, "Australia");
		}
		catch (IOException | SAXException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
