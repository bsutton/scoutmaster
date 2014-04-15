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

	private final String fieldName;
	private final FormField formField;
	private final Field field;

	private final String alternateDisplayName;

	public FormFieldImpl(final Field field, final String fieldName, final FormField formField)
	{
		this.fieldName = fieldName;
		this.formField = formField;
		this.field = field;
		this.alternateDisplayName = null;
	}

	public FormFieldImpl(final Field field, final String fieldName, final FormField formField,
			final String alternateDisplayName)
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
		{
			return this.formField.displayName();
		}
		else
		{
			return this.alternateDisplayName;
		}
	}

	public boolean visible()
	{
		return this.formField.visible();
	}

	@Override
	public String toString()
	{
		return displayName();
	}

	public void setValue(final Object entity, Object value) throws IllegalArgumentException, IllegalAccessException
	{
		if (value.getClass() != this.field.getType())
		{
			// we need to do some conversions by trying to instantiate an
			// instance of of the field with the value as
			// an argument
			Constructor<?> ctor;
			try
			{
				ctor = this.field.getType().getConstructor(value.getClass());
				if (ctor != null)
				{
					value = ctor.newInstance(value);
				}
			}
			catch (NoSuchMethodException | SecurityException | InstantiationException | InvocationTargetException e)
			{
				throw new IllegalArgumentException("Unable to convert value: " + value + " of type " + value.getClass()
						+ " to the field type of " + this.field.getType());
			}
		}
		this.field.set(entity, value);
	}
}