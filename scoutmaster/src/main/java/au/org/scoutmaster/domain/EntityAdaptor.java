package au.org.scoutmaster.domain;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;

import javax.persistence.EntityManager;

import org.apache.commons.lang.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityAdaptor<T>
{
	@SuppressWarnings("unused")
	private static Logger logger = LogManager.getLogger(EntityAdaptor.class);

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

				FormFieldImpl formField = new FormFieldImpl(field, field.getName(), ((FormField) singleAnnotation));
				formFields.add(formField);
			}
		}
		Collections.sort(formFields,  new CustomComparator<FormFieldImpl>()  );

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

	public void save(EntityManager em, T entity, String[] csvHeaders, String[] fieldValues,
			Hashtable<String, FormFieldImpl> fieldMap)
	{
		throw new NotImplementedException("Derived class must implatement this");
	}

	@SuppressWarnings("unchecked")
	static public <T extends EntityAdaptor<?>> T create(Class<?> entityClass)
	{
		T adaptor = null;
		if (entityClass.equals(Contact.class))
			adaptor = (T) new ContactEntityAdaptor(entityClass);
		return adaptor;
	}

	public class CustomComparator<Fs> implements Comparator<FormFieldImpl>
	{
		@Override
		public int compare(FormFieldImpl o1, FormFieldImpl o2)
		{
			return o1.displayName().compareTo(o2.displayName());
		}
	}
}
