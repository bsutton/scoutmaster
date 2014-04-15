package org.joda.time.contrib.eclipselink;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.mappings.converters.Converter;
import org.eclipse.persistence.sessions.Session;
import org.joda.time.LocalDate;

/**
 * Persist LocalDate via EclipseLink
 *
 * @author georgi.knox
 *
 */
public class LocalDateConverter implements Converter
{
	private static final long serialVersionUID = 1L;

	@Override
	public Object convertDataValueToObjectValue(final Object dataValue, final Session session)
	{
		if (dataValue instanceof Date)
		{
			return new LocalDate(dataValue);
		}
		throw new IllegalStateException("Converstion exception, value is not of LocalDate type.");
	}

	@Override
	public Object convertObjectValueToDataValue(final Object objectValue, final Session arg1)
	{

		if (objectValue instanceof LocalDate)
		{
			final LocalDate localDate = (LocalDate) objectValue;
			final String dateString = localDate.toString();
			final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			try
			{
				return df.parse(dateString);
			}
			catch (final ParseException e)
			{
				throw new IllegalStateException("Converstion exception, value is not of java.util.Date type.");
			}
		}
		throw new IllegalStateException("Converstion exception, value is not of java.util.Date type.");
	}

	@Override
	public void initialize(final DatabaseMapping arg0, final Session arg1)
	{

	}

	@Override
	public boolean isMutable()
	{
		return false;
	}

}
