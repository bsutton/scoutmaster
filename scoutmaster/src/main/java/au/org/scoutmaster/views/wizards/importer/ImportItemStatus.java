package au.org.scoutmaster.views.wizards.importer;

import javax.validation.ConstraintViolationException;

public class ImportItemStatus
{
	public Integer getRow()
	{
		return this.row;
	}

	public Exception getException()
	{
		return this.exception;
	}

	Integer row;
	Exception exception;

	public ImportItemStatus(final Integer row, final Exception e)
	{
		this.row = row;
		this.exception = new ImportRowExeption(e);
	}

	public ImportItemStatus(final int row, final ConstraintViolationException e)
	{
		this.row = row;
		this.exception = new ImportRowExeption(e);

	}

}
