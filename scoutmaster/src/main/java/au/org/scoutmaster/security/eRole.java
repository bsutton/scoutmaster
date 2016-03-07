package au.org.scoutmaster.security;

public enum eRole
{
	NONE("None"), VOLUNTEER("Volunteer"), LEADER("Leader"), GUARDIAN("Guardian"), GROUP_LEADER(
			"Group Leader"), PRESIDENT("President"), QUARTER_MASTER("Quarter Master"), COMMITTEE_MEMBER(
					"Committee Member"), SECRETARY("Secretary"), TREASURER("Treasurer"), MEMBER("Member"), SYS_ADMIN(
							"System Administrator");

	private String label;

	eRole(String label)
	{
		this.label = label;
	}

	public String getLabel()
	{
		return label;
	}

	@Override
	public String toString()
	{
		return label;
	}
}
