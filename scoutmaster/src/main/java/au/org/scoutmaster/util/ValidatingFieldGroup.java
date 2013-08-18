package au.org.scoutmaster.util;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.ui.Field;

public class ValidatingFieldGroup<T> extends FieldGroup
{
	private static final long serialVersionUID = 1L;
	private final Class<T> entityClass;


	public ValidatingFieldGroup(Class<T> entityClass)
	{
		this.entityClass = entityClass;
	}

	
	public ValidatingFieldGroup(Item item, Class<T> entityClass)
	{
		super(item);
		this.entityClass = entityClass;
		
	}

	/*
	 * Override configureField to add a bean validator to each field.
	 */
	@Override
	protected void configureField(Field<?> field)
	{
		field.removeAllValidators();
		super.configureField(field);
		// Add Bean validators if there are annotations
		// Note that this requires a bean validation implementation to
		// be available.
		BeanValidator validator = new BeanValidator(entityClass, getPropertyId(field).toString());
		
		field.addValidator(validator);
		if (field.getLocale() != null)
		{
			validator.setLocale(field.getLocale());
		}
	}

}
