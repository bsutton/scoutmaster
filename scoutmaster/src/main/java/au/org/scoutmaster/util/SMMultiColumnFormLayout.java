package au.org.scoutmaster.util;

import org.vaadin.tokenfield.TokenField;

import au.com.vaadinutils.crud.MultiColumnFormLayout;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.org.scoutmaster.domain.BaseEntity;
import au.org.scoutmaster.views.Selected;


public class SMMultiColumnFormLayout<E> extends MultiColumnFormLayout<E>
{
	private static final long serialVersionUID = 1L;

	public SMMultiColumnFormLayout(int columns, ValidatingFieldGroup<E> fieldGroup, int labelWidth)
	{
		super(columns, fieldGroup, labelWidth);
	}

	public <T extends BaseEntity> TokenField bindTokenField(Selected<T> selected, String fieldLabel, String fieldName,
			Class<? extends BaseEntity> clazz)
	{
		TokenField field = SMFormHelper.bindTokenField(this, super.getFieldGroup(), selected, fieldLabel, fieldName, clazz);
		this.getFieldList().add(field);
		return field;
	}

}
