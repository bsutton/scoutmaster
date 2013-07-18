package au.org.scoutmaster.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.vaadin.tokenfield.TokenField;

import au.org.scoutmaster.domain.BaseEntity;
import au.org.scoutmaster.filter.EntityManagerProvider;
import au.org.scoutmaster.views.ContactTokenField;
import au.org.scoutmaster.views.Selected;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class FormHelper
{
	ArrayList<AbstractField<?>> fieldList = new ArrayList<AbstractField<?>>();
	private FormLayout form;
	private FieldGroup group;

	public FormHelper(FormLayout form, FieldGroup grouop)
	{
		this.form = form;
		this.group = grouop;
	}

	public TextField bindTextField(String fieldLabel,
			String fieldName)
	{
		TextField field = new TextField(fieldLabel);
		field.setWidth("100%");
		field.setImmediate(true);
		this.group.bind(field, fieldName);
		this.fieldList.add(field);
		this.form.addComponent(field);
		return field;
	}

	public TextArea bindTextAreaField(String fieldLabel,
			String fieldName, int rows)
	{
		TextArea field = new TextArea(fieldLabel);
		field.setRows(rows);
		field.setWidth("100%");
		field.setImmediate(true);
		this.group.bind(field, fieldName);
		this.fieldList.add(field);
		this.form.addComponent(field);
		return field;
	}

	public DateField bindDateField(String fieldLabel,
			String fieldName)
	{
		DateField field = new DateField(fieldLabel);
		field.setDateFormat("yyyy-MM-dd");

		field.setImmediate(true);
		field.setWidth("100%");
		this.group.bind(field, fieldName);
		this.fieldList.add(field);
		this.form.addComponent(field);
		return field;
	}

	public Label bindLabelField(String fieldLabel,
			String fieldName)
	{
		Label field = new Label(fieldLabel);
		field.setWidth("100%");
		this.form.addComponent(field);
		return field;
	}



	public ComboBox bindEnumField(String fieldLabel,
			String fieldName, Class<?> clazz)
	{
		ComboBox field = new ComboBox(fieldLabel, createContainerFromEnumClass(fieldName, clazz));
		field.setNewItemsAllowed(false);
		field.setNullSelectionAllowed(false);
		field.setTextInputAllowed(false);
		field.setWidth("100%");
		field.setImmediate(true);
		this.group.bind(field, fieldName);
		this.fieldList.add(field);
		this.form.addComponent(field);
		return field;
	}

	public CheckBox bindBooleanField(String fieldLabel,
			String fieldName)
	{
		CheckBox field = new CheckBox(fieldLabel);
		field.setWidth("100%");
		field.setImmediate(true);
		this.group.bind(field, fieldName);
		this.fieldList.add(field);
		this.form.addComponent(field);
		return field;

	}

	public ComboBox bindEntityField(String fieldLabel,
			String fieldName, Class<? extends BaseEntity> clazz)
	{
		JPAContainer<?> container = JPAContainerFactory.make(clazz, EntityManagerProvider.INSTANCE.getEntityManager());

		ComboBox field = new ComboBox(fieldLabel, container);
		field.setNewItemsAllowed(false);
		field.setNullSelectionAllowed(false);
		field.setTextInputAllowed(false);
		field.setWidth("100%");
		field.setImmediate(true);
		this.group.bind(field, fieldName);
		this.fieldList.add(field);
		this.form.addComponent(field);
		return field;
	}

	Container createContainerFromEnumClass(String fieldName, Class<?> clazz)
	{
		LinkedHashMap<Enum<?>, String> enumMap = new LinkedHashMap<Enum<?>, String>();
		for (Object enumConstant : clazz.getEnumConstants())
		{
			enumMap.put((Enum<?>) enumConstant, enumConstant.toString());
		}

		return createContainerFromMap(fieldName, enumMap);
	}

	@SuppressWarnings("unchecked")
	public Container createContainerFromMap(String fieldName, Map<?, String> hashMap)
	{
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty(fieldName, String.class, "");

		Iterator<?> iter = hashMap.keySet().iterator();
		while (iter.hasNext())
		{
			Object itemId = iter.next();
			container.addItem(itemId);
			container.getItem(itemId).getItemProperty(fieldName).setValue(hashMap.get(itemId));
		}

		return container;
	}

	public ArrayList<AbstractField<?>> getFieldList()
	{
		return this.fieldList;
	}

	public <T> TokenField bindTokenField(Selected<T> selected, String fieldLabel, String fieldName, Class<? extends BaseEntity> clazz)
	{
			JPAContainer<?> container = JPAContainerFactory.make(clazz, EntityManagerProvider.INSTANCE.getEntityManager());

			VerticalLayout layout = new VerticalLayout();
			TokenField field = new ContactTokenField(selected, fieldLabel, layout);
			field.setStyleName(TokenField.STYLE_TOKENFIELD); // remove fake textfield look
			field.setWidth("100%"); // width...
			field.setInputWidth("100%"); // and input width separately
			field.setContainerDataSource(container); // 'address book'
			field.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS); // suggest
			field.setTokenCaptionPropertyId("name"); // use container item property "name" in input
			field.setInputPrompt("Enter contact name or new email address");
			field.setRememberNewTokens(false); // we can opt to do this ourselves
			field.setImmediate(true);
			this.group.bind(field, fieldName);
			this.fieldList.add(field);
			this.form.addComponent(field);
			return field;
	}

	public void bind(ContactTokenField contactTokenField)
	{
		this.fieldList.add(contactTokenField);
		this.form.addComponent(contactTokenField);
		
	}

}
