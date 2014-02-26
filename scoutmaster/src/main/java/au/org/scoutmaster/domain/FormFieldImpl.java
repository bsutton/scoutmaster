package au.org.scoutmaster.domain;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FormFieldImpl
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(FormFieldImpl.class);

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
		return displayName();
	}

	public void setValue(Object entity, Object value) throws IllegalArgumentException, IllegalAccessException
	{
		if (value.getClass() != field.getType())
		{
			// we need to do some conversions by trying to instantiate an instance of of the field with the value as
			// an argument
			Constructor<?> ctor;
			try
			{
				ctor = field.getType().getConstructor(value.getClass());
				if (ctor != null)
					value  = ctor.newInstance(value); 
			}
			catch (NoSuchMethodException | SecurityException | InstantiationException | InvocationTargetException e)
			{
				throw new IllegalArgumentException("Unable to convert value: " + value + " of type " + value.getClass() 
						+ " to the field type of " + field.getType());
			}
		}
		field.set(entity, value);
	}
}