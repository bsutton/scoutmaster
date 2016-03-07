package au.org.scoutmaster.security;

public class MissingRoleException extends SecurityException
{
	private static final long serialVersionUID = 1L;

	public MissingRoleException(String message)
	{
		super(message);
	}

}
