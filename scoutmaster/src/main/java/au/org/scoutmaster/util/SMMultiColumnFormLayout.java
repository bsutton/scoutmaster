package au.org.scoutmaster.util;

import javax.persistence.metamodel.SetAttribute;

import org.vaadin.tokenfield.TokenField;

import au.com.vaadinutils.crud.FormHelper;
import au.com.vaadinutils.crud.MultiColumnFormLayout;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.org.scoutmaster.domain.BaseEntity;
import au.org.scoutmaster.views.Selected;


public class SMMultiColumnFormLayout<E extends BaseEntity> extends MultiColumnFormLayout<E>
{
	private static final long serialVersionUID = 1L;

	public SMMultiColumnFormLayout(int columns, ValidatingFieldGroup<E> fieldGroup)
	{
		super(columns, fieldGroup);
	}
	
	protected FormHelper<E> getFormHelper(MultiColumnFormLayout<E> layout, ValidatingFieldGroup<E> fieldGroup)
	{
		return new SMFormHelper<>(layout, fieldGroup);
	}


	public <L> TokenField bindTagField(Selected<E> selected, String fieldLabel, SetAttribute<E, L> entityField)
	{
		TokenField field = ((SMFormHelper<E>)getFormHelper()).bindTagField(this, super.getFieldGroup(), selected, fieldLabel, entityField);
		this.getFieldList().add(field);
		return field;
	}

	public <L> TokenField bindTokenField(Selected<E> selected, String fieldLabel, String fieldName)
	{
		TokenField field = ((SMFormHelper<E>)getFormHelper()).bindTagField(this, super.getFieldGroup(), selected, fieldLabel, fieldName);
		this.getFieldList().add(field);
		return field;
	}
}
