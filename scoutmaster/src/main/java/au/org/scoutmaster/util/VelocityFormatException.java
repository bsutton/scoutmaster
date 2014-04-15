package au.org.scoutmaster.util;

/**
 * Used to package up velocity errors into a catchable exception.
 * 
 * @author bsutton
 *
 */
public class VelocityFormatException extends Exception
{
	private static final long serialVersionUID = 1L;

	public VelocityFormatException(final Throwable e)
	{
		super(e);
	}

}
