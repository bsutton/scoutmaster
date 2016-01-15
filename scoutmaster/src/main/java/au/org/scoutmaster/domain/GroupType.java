package au.org.scoutmaster.domain;

public enum GroupType
{
	Scouts("Scouts"), GirlGuides("Girl Guides");

	private String printableName;

	GroupType(String printableName)
	{
		this.printableName = printableName;
	}

	public String getPrintableName()
	{
		return this.printableName;
	}

	@Override
	public String toString()
	{
		return getPrintableName();
	}
}
