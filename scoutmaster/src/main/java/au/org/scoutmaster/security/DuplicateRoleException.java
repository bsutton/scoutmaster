package au.org.scoutmaster.security;

public class DuplicateRoleException extends SecurityException
{
	private static final long serialVersionUID = 1L;

	public DuplicateRoleException(String message)
	{
		super(message);
	}

}
