package au.org.scoutmaster.domain;

public enum ScoutGroupType
{
	Scouts("Scouts"), GirlGuides("Girl Guides");

	private String printableName;

	ScoutGroupType(String printableName)
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
