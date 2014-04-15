package au.org.scoutmaster.views.wizards.importer;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

public class ImportRowExeption extends Exception
{
	private static final long serialVersionUID = 1L;
	private final String message;

	public ImportRowExeption(final Exception e)
	{
		this.message = e.getClass().getSimpleName() + " : " + e.getMessage();
	}

	public ImportRowExeption(final ConstraintViolationException e)
	{

		// build constraint error
		final StringBuilder sb = new StringBuilder();
		for (final ConstraintViolation<?> violation : e.getConstraintViolations())
		{
			sb.append("Error: " + violation.getPropertyPath() + " : " + violation.getMessage() + "\n");
		}

		this.message = e.getClass().getSimpleName() + " : " + sb.toString();
	}

	@Override
	public String toString()
	{
		return this.message;
	}
}
