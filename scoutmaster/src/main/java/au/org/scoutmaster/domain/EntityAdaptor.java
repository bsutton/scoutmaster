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

	public EntityAdaptor(final Class<?> entity)
	{
		this.entity = entity;
	}

	public ArrayList<FormFieldImpl> getFields()
	{
		final ArrayList<FormFieldImpl> formFields = new ArrayList<FormFieldImpl>();
		final Field[] fields = this.entity.getDeclaredFields();

		for (final Field field : fields)
		{
			if (field.isAnnotationPresent(FormField.class))
			{
				final FormField singleAnnotation = field.getAnnotation(FormField.class);

				final FormFieldImpl formField = new FormFieldImpl(field, field.getName(), singleAnnotation);
				formFields.add(formField);
			}
		}
		Collections.sort(formFields, new CustomComparator<FormFieldImpl>());

		return formFields;
	}

	public ArrayList<String> getFieldDisplayNames()
	{
		final ArrayList<String> fieldDisplayNames = new ArrayList<String>();

		for (final FormFieldImpl field : getFields())
		{
			fieldDisplayNames.add(field.displayName());
		}
		return fieldDisplayNames;
	}

	public void save(final EntityManager em, final T entity, final String[] csvHeaders, final String[] fieldValues,
			final Hashtable<String, FormFieldImpl> fieldMap)
	{
		throw new NotImplementedException("Derived class must implatement this");
	}

	@SuppressWarnings("unchecked")
	static public <T extends EntityAdaptor<?>> T create(final Class<?> entityClass)
	{
		T adaptor = null;
		if (entityClass.equals(Contact.class))
		{
			adaptor = (T) new ContactEntityAdaptor(entityClass);
		}
		return adaptor;
	}

	public class CustomComparator<Fs> implements Comparator<FormFieldImpl>
	{
		@Override
		public int compare(final FormFieldImpl o1, final FormFieldImpl o2)
		{
			return o1.displayName().compareTo(o2.displayName());
		}
	}
}
