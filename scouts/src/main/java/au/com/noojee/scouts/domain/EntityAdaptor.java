package au.com.noojee.scouts.domain;

import java.lang.reflect.Field;
import java.util.ArrayList;

import au.com.noojee.scouts.FormField;

public class EntityAdaptor<T extends Class<?>>
{
	T entity;

	public EntityAdaptor(T entity)
	{
		this.entity = entity;
	}

	public ArrayList<FormField> getFields()
	{
		ArrayList<FormField> formFields = new ArrayList<FormField>();
		Field[] fields = entity.getDeclaredFields();
		
		
		for (Field field : fields)
		{
			Class<?> fieldClass = field.getType();
			
			if(field.isAnnotationPresent(FormField.class)) {
				FormField singleAnnotation = 
	            		(FormField) field.getAnnotation(FormField.class);
	            formFields.add( (FormField) singleAnnotation);
			}
		}
		
		return formFields;
	}

	public ArrayList<String> getFieldNames()
	{
		ArrayList<String> fieldNames = new ArrayList<String>();
		
		for (FormField field : getFields())
		{
			fieldNames.add(field.name());
		}
		return fieldNames;
	}
}
