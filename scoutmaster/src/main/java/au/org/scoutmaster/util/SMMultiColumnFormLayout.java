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

	public SMMultiColumnFormLayout(final int columns, final ValidatingFieldGroup<E> fieldGroup)
	{
		super(columns, fieldGroup);
	}

	@Override
	protected FormHelper<E> getFormHelper(final MultiColumnFormLayout<E> layout,
			final ValidatingFieldGroup<E> fieldGroup)
	{
		return new SMFormHelper<>(layout, fieldGroup);
	}

	public <L> TokenField bindTagField(final Selected<E> selected, final String fieldLabel,
			final SetAttribute<E, L> entityField)
	{
		final TokenField field = ((SMFormHelper<E>) getFormHelper()).bindTagField(this, super.getFieldGroup(), selected,
				fieldLabel, entityField);
		getFieldList().add(field);
		return field;
	}

	public <L> TokenField bindTokenField(final Selected<E> selected, final String fieldLabel, final String fieldName)
	{
		final TokenField field = ((SMFormHelper<E>) getFormHelper()).bindTagField(this, super.getFieldGroup(), selected,
				fieldLabel, fieldName);
		getFieldList().add(field);
		return field;
	}
}
