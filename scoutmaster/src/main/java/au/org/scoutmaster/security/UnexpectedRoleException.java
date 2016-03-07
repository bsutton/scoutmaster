package au.org.scoutmaster.security;

public class UnexpectedRoleException extends SecurityException
{
	private static final long serialVersionUID = 1L;

	public UnexpectedRoleException(String message)
	{
		super(message);
	}

}
