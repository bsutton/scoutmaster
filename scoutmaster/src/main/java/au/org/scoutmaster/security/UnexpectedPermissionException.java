package au.org.scoutmaster.security;

public class UnexpectedPermissionException extends SecurityException
{
	private static final long serialVersionUID = 1L;

	public UnexpectedPermissionException(String message)
	{
		super(message);
	}

}
