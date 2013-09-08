package au.org.scoutmaster.views.wizards.importer;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

public class ImportRowExeption extends Exception
{
	private static final long serialVersionUID = 1L;
	private String message;
	
	public ImportRowExeption(Exception e)
	{
		this.message = e.getClass().getSimpleName() + " : " + e.getMessage();
	}
	
	
	public ImportRowExeption(ConstraintViolationException e)
	{
		
		// build constraint error
		StringBuilder sb = new StringBuilder();
		for (ConstraintViolation<?> violation : e.getConstraintViolations())
		{
			sb.append("Error: " + violation.getPropertyPath() + " : " + violation.getMessage() + "\n");
		}
		
		message = e.getClass().getSimpleName() + " : " + sb.toString();
	}
	
	public String toString()
	{
		return message;
	}
}
