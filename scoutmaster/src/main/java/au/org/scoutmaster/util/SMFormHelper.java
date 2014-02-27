package au.org.scoutmaster.util;

import java.io.Serializable;

import javax.persistence.metamodel.SetAttribute;

import org.vaadin.tokenfield.TokenField;

import au.com.vaadinutils.crud.FormHelper;
import au.com.vaadinutils.crud.ValidatingFieldGroup;
import au.org.scoutmaster.domain.BaseEntity;
import au.org.scoutmaster.domain.Tag_;
import au.org.scoutmaster.fields.SplitTagField;
import au.org.scoutmaster.views.Selected;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractLayout;

public class SMFormHelper<E> extends FormHelper<E> implements Serializable
{
	private static final long serialVersionUID = 1L;

	public SMFormHelper(AbstractLayout form, ValidatingFieldGroup<E> group)
	{
		super(form, group);
	}

	
	public <T extends BaseEntity> TokenField bindTokenField(Selected<T> selected, String fieldLabel, String fieldName,
			Class<? extends BaseEntity> clazz)
	{
		TokenField field = bindTagField(super.getForm(), super.getFieldGroup(), selected, fieldLabel, fieldName);
		this.getFieldList().add(field);
		return field;
	}
	
	public <T extends BaseEntity, L, F> TokenField bindTagField(AbstractLayout form, FieldGroup group, Selected<T> selected
			, String fieldLabel, SetAttribute<T, L> entityField)
	{
		TokenField field = bindTagField(form, group, selected, fieldLabel, entityField.getName());
		this.getFieldList().add(field);
		return field;
	}


	public <T extends BaseEntity, L> TokenField bindTagField(AbstractLayout form, FieldGroup group, Selected<T> selected, String fieldLabel, String fieldName)
	{
		TokenField field = new SplitTagField<T>(fieldLabel, false);
		field.setWidth("100%"); // width...
		field.setInputWidth("100%"); // and input width separately
		field.setFilteringMode(FilteringMode.CONTAINS); // suggest
		field.setTokenCaptionPropertyId(Tag_.name.getName());
		field.setInputPrompt("Enter one or more comma separated tags");
		field.setRememberNewTokens(false); // we can opt to do this ourselves
		field.setImmediate(true);

		if (group != null)
			group.bind(field, fieldName);
		form.addComponent(field);
		return field;
	}

}
