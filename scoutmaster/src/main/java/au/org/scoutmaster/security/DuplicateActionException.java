package au.org.scoutmaster.security;

public class DuplicateActionException extends SecurityException
{
	private static final long serialVersionUID = 1L;

	public DuplicateActionException(String message)
	{
		super(message);
	}

}
