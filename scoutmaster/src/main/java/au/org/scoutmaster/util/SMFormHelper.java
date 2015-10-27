package au.org.scoutmaster.util;

import java.io.Serializable;

import javax.persistence.metamodel.SetAttribute;

import org.vaadin.tokenfield.TokenField;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractLayout;

import au.com.vaadinutils.crud.CrudEntity;
import au.com.vaadinutils.crud.FormHelper;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.org.scoutmaster.domain.BaseEntity;
import au.org.scoutmaster.domain.Tag_;
import au.org.scoutmaster.fields.SplitTagField;
import au.org.scoutmaster.views.Selected;

public class SMFormHelper<E extends CrudEntity> extends FormHelper<E> implements Serializable
{
	private static final long serialVersionUID = 1L;

	public SMFormHelper(final AbstractLayout form, final ValidatingFieldGroup<E> group)
	{
		super(form, group);
	}

	public <T extends BaseEntity> TokenField bindTokenField(final Selected<T> selected, final String fieldLabel,
			final String fieldName, final Class<? extends BaseEntity> clazz)
	{
		final TokenField field = bindTagField(super.getForm(), super.getFieldGroup(), selected, fieldLabel, fieldName);
		getFieldList().add(field);
		return field;
	}

	public <T extends BaseEntity, L, F> TokenField bindTagField(final AbstractLayout form, final FieldGroup group,
			final Selected<T> selected, final String fieldLabel, final SetAttribute<T, L> entityField)
	{
		final TokenField field = bindTagField(form, group, selected, fieldLabel, entityField.getName());
		getFieldList().add(field);
		return field;
	}

	public <T extends BaseEntity, L> TokenField bindTagField(final AbstractLayout form, final FieldGroup group,
			final Selected<T> selected, final String fieldLabel, final String fieldName)
	{
		final TokenField field = new SplitTagField<T>(fieldLabel, false);
		field.setWidth("100%"); // width...
		field.setInputWidth("100%"); // and input width separately
		field.setFilteringMode(FilteringMode.CONTAINS); // suggest
		field.setTokenCaptionPropertyId(Tag_.name.getName());
		field.setInputPrompt("Enter one or more comma separated tags");
		field.setRememberNewTokens(false); // we can opt to do this ourselves
		field.setImmediate(true);

		if (group != null)
		{
			group.bind(field, fieldName);
		}
		form.addComponent(field);
		return field;
	}

}
