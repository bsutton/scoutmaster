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
		ArrayList<FormFieldImpl> formFields = new ArrayList<>();
		Field[] fields = entity.getDeclaredFields();

		for (Field field : fields)
		{
			if (field.isAnnotationPresent(FormField.class))
			{
				FormField singleAnnotation = (FormField) field.getAnnotation(FormField.class);

				if (field.getName().equals(Contact_.address.getName()))
				{
					FormFieldImpl formField = new FormFieldImpl(field, Address_.street.getName(), singleAnnotation, "Address.Street");
					formFields.add(formField);
					formField = new FormFieldImpl(field, Address_.city.getName(), singleAnnotation, "Address.City");
					formFields.add(formField);
					formField = new FormFieldImpl(field, Address_.state.getName(), singleAnnotation, "Address.State");
					formFields.add(formField);
					formField = new FormFieldImpl(field, Address_.postcode.getName(), singleAnnotation, "Address.Postcode");
					formFields.add(formField);
				}
				else if (field.getName().equals(Contact_.phone1.getName()))
				{
					FormFieldImpl formField = new FormFieldImpl(field, Contact_.phone1.getName(), singleAnnotation,
							"Phone1.PhoneNo");
					formFields.add(formField);
				}
				else if (field.getName().equals(Contact_.phone2.getName()))
				{
					FormFieldImpl formField = new FormFieldImpl(field, Contact_.phone2.getName(), singleAnnotation,
							"Phone2.PhoneNo");
					formFields.add(formField);
				}
				else if (field.getName().equals(Contact_.phone3.getName()))
				{
					FormFieldImpl formField = new FormFieldImpl(field, Contact_.phone3.getName(), singleAnnotation,
							"Phone3.PhoneNo");
					formFields.add(formField);
				}
				else
				{
					FormFieldImpl formField = new FormFieldImpl(field, field.getName(), ((FormField) singleAnnotation));
					formFields.add(formField);
				}
			}
		}

		Collections.sort(formFields, new CustomComparator<FormFieldImpl>());

		return formFields;
	}

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

					if (formField.getFieldName().equals(Contact_.phone1.getName()))
						entity.setPhone1(fieldValue);
					else if (formField.getFieldName().equals(Contact_.phone2.getName()))
						entity.setPhone2(new Phone(fieldValue));
					else if (formField.getFieldName().equals(Contact_.phone3.getName()))
						entity.setPhone3(new Phone(fieldValue));
					else if (formField.getFieldName().equals(Contact_.gender.getName()))
						entity.setGender(Gender.valueOf(fieldValue));
					else if (formField.getFieldName().equals(Address_.street.getName()))
						entity.setStreet(fieldValue);
					else if (formField.getFieldName().equals(Address_.city.getName()))
						entity.setCity(fieldValue);
					else if (formField.getFieldName().equals(Address_.state.getName()))
						entity.setState(fieldValue);
					else if (formField.getFieldName().equals(Address_.postcode.getName()))
						entity.setPostcode(fieldValue);
					else if (formField.getFieldName().equals(Contact_.birthDate.getName()))
						entity.setBirthDate(fieldValue);
					else
						formField.setValue(entity, fieldValue);
				}
			}
			catch (Throwable e)
			{
				// For the moment just ignore non-parsable fields.
				logger.error(e, e);
			}
		}

	}

	// class HackedFormField implements FormField
	// {
	// private String displayName;
	//
	// HackedFormField(String displayName)
	// {
	// this.displayName = displayName;
	// }
	//
	// @Override
	// public Class<? extends Annotation> annotationType()
	// {
	// return null;
	// }
	//
	// @Override
	// public boolean visible()
	// {
	// return false;
	// }
	//
	// @Override
	// public String displayName()
	// {
	// return this.displayName;
	// }
	//
	// }

}
