package au.org.scoutmaster.domain;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

public class FormFieldImpl
{
	private static Logger logger = Logger.getLogger(FormFieldImpl.class);

	private String fieldName;
	private FormField formField;
	private Field field;

	private String alternateDisplayName;

	public FormFieldImpl(Field field, String fieldName, FormField formField)
	{
		this.fieldName = fieldName;
		this.formField = formField;
		this.field = field;
		this.alternateDisplayName = null;
	}

	public FormFieldImpl(Field field, String fieldName, FormField formField, String alternateDisplayName)
	{
		this.fieldName = fieldName;
		this.formField = formField;
		this.field = field;
		this.alternateDisplayName = alternateDisplayName;

	}

	public String getFieldName()
	{
		return this.fieldName;
	}

	public String displayName()
	{
		if (this.alternateDisplayName == null)
			return formField.displayName();
		else
			return this.alternateDisplayName;
	}

	public boolean visible()
	{
		return formField.visible();
	}

	public String toString()
	{
		return formField.displayName();
	}

	public void setValue(Object entity, Object value)
	{
		try
		{
			field.set(entity, value);
		}
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			logger.error(e, e);
		}
	}
}