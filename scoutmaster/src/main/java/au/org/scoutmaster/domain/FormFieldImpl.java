package au.org.scoutmaster.domain;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

import au.org.scoutmaster.FormField;

public class FormFieldImpl
{
	private static Logger logger = Logger.getLogger(FormFieldImpl.class);
	
	String fieldName;
	FormField formField;
	private Field field;

	public FormFieldImpl(Field field, String fieldName, FormField formField)
	{
		this.fieldName = fieldName;
		this.formField = formField;
		this.field = field;
	}
	
	public String getFieldName()
	{
		return this.fieldName;
	}

	public String displayName()
	{
		return formField.displayName();
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
			logger.error(e,e);
		}
	}
}