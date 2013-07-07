package au.org.scoutmaster.domain;

import au.org.scoutmaster.FormField;

public class FormFieldImpl
{
	String fieldName;
	FormField field;

	public FormFieldImpl(String fieldName, FormField field)
	{
		this.fieldName = fieldName;
		this.field = field;
	}
	
	public String getFieldName()
	{
		return this.fieldName;
	}

	public String displayName()
	{
		return field.displayName();
	}

	public boolean visible()
	{
		return field.visible();
	}
	
	public String toString()
	{
		return field.displayName();
	}
}