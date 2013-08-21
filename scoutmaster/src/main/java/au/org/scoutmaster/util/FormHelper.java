package au.org.scoutmaster.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.vaadin.tokenfield.TokenField;

import au.org.scoutmaster.domain.BaseEntity;
import au.org.scoutmaster.domain.SectionType;
import au.org.scoutmaster.fields.ContactTokenField;
import au.org.scoutmaster.filter.EntityManagerProvider;
import au.org.scoutmaster.views.Selected;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.data.Container;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class FormHelper implements Serializable
{
	private static final long serialVersionUID = 1L;

	ArrayList<AbstractComponent> fieldList = new ArrayList<>();
	private AbstractLayout form;
	private FieldGroup group;

	public FormHelper(AbstractLayout form, FieldGroup group)
	{
		this.form = form;
		this.group = group;
	}

	public TextField bindTextField(String fieldLabel, String fieldName)
	{
		TextField field = bindTextField(form, group, fieldLabel, fieldName);
		this.fieldList.add(field);
		return field;
	}
	
	static public TextField bindTextField(AbstractLayout form, FieldGroup group, String fieldLabel, String fieldName)
	{
		TextField field = new TextField(fieldLabel);
		field.setWidth("100%");
		field.setImmediate(true);
		field.setNullRepresentation("");
		field.setNullSettingAllowed(false);
		if (group != null)
			group.bind(field, fieldName);
		form.addComponent(field);
		return field;
	}

	
	public TextArea bindTextAreaField(String fieldLabel, String fieldName, int rows)
	{
		TextArea field = bindTextAreaField(form, group, fieldLabel, fieldName, rows);
		this.fieldList.add(field);
		return field;
	}

	static public TextArea bindTextAreaField(AbstractLayout form, FieldGroup group,  String fieldLabel, String fieldName, int rows)
	{
		TextArea field = new TextArea(fieldLabel);
		field.setRows(rows);
		field.setWidth("100%");
		field.setImmediate(true);
		field.setNullRepresentation("");
		if (group != null)
			group.bind(field, fieldName);
		form.addComponent(field);
		return field;
	}
	
	public DateField bindDateField(String fieldLabel, String fieldName)
	{
		DateField field = bindDateField(form, group, fieldLabel, fieldName);
		this.fieldList.add(field);
		return field;
	}


	static public DateField bindDateField(AbstractLayout form, FieldGroup group,  String fieldLabel, String fieldName)
	{
		DateField field = new DateField(fieldLabel);
		field.setDateFormat("yyyy-MM-dd");

		field.setImmediate(true);
		field.setWidth("100%");
		if (group != null)
			group.bind(field, fieldName);
		form.addComponent(field);
		return field;
	}

	public Label bindLabel(String fieldLabel)
	{
		Label field = bindLabel(form, group, fieldLabel);
		this.fieldList.add(field);
		return field;
	}

	static public Label bindLabel(AbstractLayout form, FieldGroup group,  String fieldLabel)
	{
		Label field = new Label();
		field.setCaption(fieldLabel);
		field.setWidth("100%");
		form.addComponent(field);
		return field;
	}
	
	static public Label bindLabel(AbstractLayout form, FieldGroup group,  Label field)
	{
		field.setWidth("100%");
		form.addComponent(field);
		return field;
	}

	
	public ComboBox bindEnumField(String fieldLabel, String fieldName, Class<?> clazz)
	{
		ComboBox field = bindEnumField(form, group, fieldLabel, fieldName, clazz);
		this.fieldList.add(field);
		return field;
	}


	static public ComboBox bindEnumField(AbstractLayout form, FieldGroup group, String fieldLabel, String fieldName, Class<?> clazz)
	{
		ComboBox field = new ComboBox(fieldLabel, createContainerFromEnumClass(fieldName, clazz));
		field.setNewItemsAllowed(false);
		field.setNullSelectionAllowed(false);
		field.setTextInputAllowed(false);
		field.setWidth("100%");
		field.setImmediate(true);
		if (group != null)
			group.bind(field, fieldName);
		form.addComponent(field);
		return field;
	}

	public CheckBox bindBooleanField(String fieldLabel, String fieldName)
	{
		CheckBox field = bindBooleanField(form, group, fieldLabel, fieldName);
		this.fieldList.add(field);
		return field;
	}

	static public CheckBox bindBooleanField(AbstractLayout form, FieldGroup group, String fieldLabel, String fieldName)
	{
		CheckBox field = new CheckBox(fieldLabel);
		field.setWidth("100%");
		field.setImmediate(true);
		
		if (group != null)
			group.bind(field, fieldName);
		form.addComponent(field);
		return field;

	}

	public ComboBox bindEntityField(String fieldLabel, String fieldName, String listFieldname, Class<? extends BaseEntity> listClazz)
	{
		ComboBox field = bindEntityField(form, group, fieldLabel, fieldName, listFieldname, listClazz);
		this.fieldList.add(field);
		return field;
	}

	static public ComboBox bindEntityField(AbstractLayout form, FieldGroup group, String fieldLabel, String fieldName, String listFieldName, Class<? extends BaseEntity> listClazz)
	{
		JPAContainer<?> container = JPAContainerFactory.make(listClazz, EntityManagerProvider.INSTANCE.getEntityManager());

		ComboBox field = new ComboBox(fieldLabel);

		field.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		field.setItemCaptionPropertyId(listFieldName);
		field.setContainerDataSource(container);
		SingleSelectConverter<SectionType> converter = new SingleSelectConverter<>(field);
		field.setConverter(converter);
		field.setNewItemsAllowed(false);
		field.setNullSelectionAllowed(false);
		field.setTextInputAllowed(false);
		field.setWidth("100%");
		field.setImmediate(true);
		if (group != null)
			group.bind(field, fieldName);
		form.addComponent(field);
		return field;
	}

	static Container createContainerFromEnumClass(String fieldName, Class<?> clazz)
	{
		LinkedHashMap<Enum<?>, String> enumMap = new LinkedHashMap<Enum<?>, String>();
		for (Object enumConstant : clazz.getEnumConstants())
		{
			enumMap.put((Enum<?>) enumConstant, enumConstant.toString());
		}

		return createContainerFromMap(fieldName, enumMap);
	}

	
	public <T extends BaseEntity> TokenField bindTokenField(Selected<T> selected, String fieldLabel, String fieldName,
			Class<? extends BaseEntity> clazz)
	{
		TokenField field = bindTokenField(form, group, selected, fieldLabel, fieldName, clazz);
		this.fieldList.add(field);
		return field;
	}

	static public <T extends BaseEntity> TokenField bindTokenField(AbstractLayout form, FieldGroup group, Selected<T> selected, String fieldLabel, String fieldName,
			Class<? extends BaseEntity> clazz)
	{
		// JPAContainer<? extends BaseEntity> container =
		// JPAContainerFactory.make(clazz,
		// EntityManagerProvider.INSTANCE.getEntityManager());

		VerticalLayout layout = new VerticalLayout();
		TokenField field = new ContactTokenField<T>(selected, fieldLabel, layout);
		field.setStyleName(TokenField.STYLE_TOKENFIELD); // remove fake
															// textfield look
		field.setWidth("100%"); // width...
		field.setInputWidth("100%"); // and input width separately
		field.setFilteringMode(FilteringMode.CONTAINS); // suggest
		field.setTokenCaptionPropertyId("name"); // use container item property
													// "name" in input
		field.setInputPrompt("Enter one or more comma separated tags");
		field.setRememberNewTokens(false); // we can opt to do this ourselves
		field.setImmediate(true);

		if (group != null)
			group.bind(field, fieldName);
		form.addComponent(field);
		return field;
	}

	public void bind(ContactTokenField<?> contactTokenField)
	{
		this.fieldList.add(contactTokenField);
		this.form.addComponent(contactTokenField);

	}
	

	@SuppressWarnings("unchecked")
	static public Container createContainerFromMap(String fieldName, Map<?, String> hashMap)
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

	public ArrayList<AbstractComponent> getFieldList()
	{
		return this.fieldList;
	}

	public static void showConstraintViolation(ConstraintViolationException e)
	{
		// build constraint error
		StringBuilder sb = new StringBuilder();
		for (ConstraintViolation<?> violation : e.getConstraintViolations())
		{
			sb.append("Error: " + violation.getPropertyPath() + " : " + violation.getMessage() + "\n");
		}
		Notification.show(sb.toString(), Type.ERROR_MESSAGE);
	}


}
