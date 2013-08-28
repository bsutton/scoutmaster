package au.org.scoutmaster.domain;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

public class ContactEntityAdaptor extends EntityAdaptor<Contact>
{
	private static Logger logger = Logger.getLogger(ContactEntityAdaptor.class);

	public ContactEntityAdaptor(Class<?> entity)
	{
		super(entity);
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

				if (field.getName() == "address")
				{
					FormFieldImpl formField = new FormFieldImpl(field, "street", singleAnnotation, "Address.Street");
					formFields.add(formField);
					formField = new FormFieldImpl(field, "city", singleAnnotation, "Address.City");
					formFields.add(formField);
					formField = new FormFieldImpl(field, "state", singleAnnotation, "Address.State");
					formFields.add(formField);
					formField = new FormFieldImpl(field, "postcode", singleAnnotation, "Address.Postcode");
					formFields.add(formField);
				}
				else
				{
					FormFieldImpl formField = new FormFieldImpl(field, field.getName(), ((FormField) singleAnnotation));
					formFields.add(formField);
				}
			}
		}

		Collections.sort(formFields,  new CustomComparator<FormFieldImpl>()  );

		return formFields;
	}
	
//	class HackedFormField implements FormField
//	{
//		private String displayName;
//
//		HackedFormField(String displayName)
//		{
//			this.displayName = displayName;
//		}
//		
//		@Override
//		public Class<? extends Annotation> annotationType()
//		{
//			return null;
//		}
//
//		@Override
//		public boolean visible()
//		{
//			// TODO Auto-generated method stub
//			return false;
//		}
//
//		@Override
//		public String displayName()
//		{
//			return this.displayName;
//		}
//		
//	}

	public void save(EntityManager em, Contact entity, String[] csvHeaders, String[] fieldValues,
			Hashtable<String, FormFieldImpl> fieldMap)
	{

		for (int i = 0; i < fieldValues.length; i++)
		{
			try
			{
			String csvHeaderName = csvHeaders[i];
			FormFieldImpl formField = fieldMap.get(csvHeaderName);
			if (formField != null)
			{
				String fieldValue = fieldValues[i];

				if (formField.getFieldName() == "homePhone")
					entity.setPhone1(fieldValue);
				else if (formField.getFieldName() == "workPhone")
					entity.setPhone2(new Phone(fieldValue));
				else if (formField.getFieldName() == "mobile")
					entity.setPhone3(new Phone(fieldValue));
				else if (formField.getFieldName() == "street")
					entity.setStreet(fieldValue);
				else if (formField.getFieldName() == "city")
					entity.setCity(fieldValue);
				else if (formField.getFieldName() == "state")
					entity.setState(fieldValue);
				else if (formField.getFieldName() == "postcode")
					entity.setPostcode(fieldValue);
				else if (formField.getFieldName() == "birthDate")
					entity.setBirthDate(fieldValue);
				else
					formField.setValue(entity, fieldValue);
			}
			}
			catch (Throwable e)
			{
				// For the moment just ignore non-parsable fields.
				logger.error(e,e);
			}
		}

	}
}
