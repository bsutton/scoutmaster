package au.com.noojee.scouts.domain;

import java.lang.reflect.Field;
import java.util.ArrayList;

import au.com.noojee.scouts.FormField;

public class EntityAdaptor<T>
{
	Class<?> entity;

	public EntityAdaptor(Class<?> entity)
	{
		this.entity = entity;
	}

	public ArrayList<FormFieldImpl> getFields()
	{
		ArrayList<FormFieldImpl> formFields = new ArrayList<FormFieldImpl>();
		Field[] fields = entity.getDeclaredFields();

		for (Field field : fields)
		{
			if (field.isAnnotationPresent(FormField.class))
			{
				FormField singleAnnotation = (FormField) field.getAnnotation(FormField.class);

				FormFieldImpl formField = new FormFieldImpl(field.getName(), ((FormField) singleAnnotation));
				formFields.add(formField);
			}
		}

		return formFields;
	}

	public ArrayList<String> getFieldDisplayNames()
	{
		ArrayList<String> fieldDisplayNames = new ArrayList<String>();

		for (FormFieldImpl field : getFields())
		{
			fieldDisplayNames.add(field.displayName());
		}
		return fieldDisplayNames;
	}
}
