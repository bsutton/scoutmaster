package au.org.scoutmaster.util;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.vaadin.tokenfield.TokenField;

import au.org.scoutmaster.domain.BaseEntity;
import au.org.scoutmaster.views.Selected;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class MultiColumnFormLayout<ENTITY> extends GridLayout
{
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(MultiColumnFormLayout.class);
	private static final long serialVersionUID = 1L;
	private final int columns;
	private int colspan = 1;

	private FieldGroup fieldGroup;
	private ArrayList<AbstractComponent> fieldList = new ArrayList<>();

	// public MultiColumnFormLayout(int columns)
	// {
	// super(columns * 2, 1);
	// this.columns = columns * 2;
	// init();
	// this.fieldGroup = newBeanGroupInstance();
	// //
	// // for (int i = 0; i < columns; i+=2)
	// // {
	// // this.setColumnExpandRatio(i + 1, 1 / columns);
	// // }
	// }

	public MultiColumnFormLayout(int columns, FieldGroup fieldGroup)
	{
		super(columns * 2, 1);
		this.columns = columns * 2;

		init();
		this.fieldGroup = fieldGroup;
	}

	private void init()
	{
		super.setSpacing(true);
		super.setMargin(true);
	}

	/**
	 * Add a component to the grid. If colspan has been set then it is honoured.
	 * If we are at the end of the row then automatically wrap this item to the
	 * end of the next row.
	 */
	@Override
	public void addComponent(Component component)
	{
		if (getCursorX() == columns)
		{
			newLine();
		}

		int fieldWidth = 1;
		if (component.getCaption() != null)
		{
			if (!(component instanceof Button))
			{
				Label label = new Label(component.getCaption());
				component.setCaption(null);
				super.addComponent(label);
				super.setComponentAlignment(label, Alignment.MIDDLE_RIGHT);
			}
			else
				setCursorX(getCursorX() + 1);
		}
		else
		{
			// Else if the label is null then we let the field take the full
			// width that the label normally occupies
			fieldWidth = 2;
		}

		if (colspan > 1)
			super.addComponent(component, getCursorX(), getCursorY(), getCursorX() + (fieldWidth - 1)
					+ ((colspan - 1) * 2), getCursorY());
		else
			super.addComponent(component, getCursorX(), getCursorY(), getCursorX() + (fieldWidth - 1), getCursorY());
		super.setComponentAlignment(component, Alignment.MIDDLE_LEFT);

		this.colspan = 1;
		if (getCursorY() == getRows())
		{
			insertRow(getRows());
			setCursorY(getCursorY() -1);
		}
	}

	/**
	 * Adds a new row to the grid and moves the cursor down one row.
	 */
	public void newLine()
	{
		super.insertRow(getRows());
		super.newLine();
	}

	/**
	 * Set the colspan for the next component that is inserted after which the
	 * colspan will be reset to 1.
	 * 
	 * @param colspan
	 */
	public void colspan(int colspan)
	{
		this.colspan = colspan;

	}

	public TextField bindTextField(String fieldLabel, String fieldName)
	{
		TextField field = FormHelper.bindTextField(this, fieldGroup, fieldLabel, fieldName);

		this.fieldList.add(field);
		return field;
	}

	/** 
	 * Adds a text field to the form without binding it to the FieldGroup
	 * @param caption
	 * @return
	 */
	public TextField addTextField(String caption)
	{
		TextField field = new TextField(caption);
		field.setWidth("100%");
		field.setImmediate(true);
		field.setNullRepresentation("");
		field.setNullSettingAllowed(false);
		this.addComponent(field);
		this.fieldList.add(field);
		return field;
	}


	public PasswordField bindPasswordField(String fieldLabel, String fieldName)
	{
		PasswordField field = FormHelper.bindPasswordField(this, fieldGroup, fieldLabel, fieldName);

		this.fieldList.add(field);
		return field;
	}
	
	/** 
	 * Adds a text field to the form without binding it to the FieldGroup
	 * @param caption
	 * @return
	 */
	public PasswordField addPasswordField(String caption)
	{
		PasswordField field = new PasswordField(caption);
		field.setWidth("100%");
		field.setImmediate(true);
		field.setNullRepresentation("");
		field.setNullSettingAllowed(false);
		this.addComponent(field);
		this.fieldList.add(field);
		return field;
	}


	public TextArea bindTextAreaField(String fieldLabel, String fieldName, int rows)
	{
		TextArea field = FormHelper.bindTextAreaField(this, fieldGroup, fieldLabel, fieldName, rows);
		this.fieldList.add(field);
		return field;
	}

	public DateField bindDateField(String fieldLabel, String fieldName)
	{
		DateField field = FormHelper.bindDateField(this, fieldGroup, fieldLabel, fieldName);
		this.fieldList.add(field);
		return field;
	}

	public Label bindLabel(String fieldLabel)
	{
		Label field = FormHelper.bindLabel(this, fieldGroup, fieldLabel);
		this.fieldList.add(field);
		return field;
	}

	public Label bindLabel(Label label)
	{
		Label field = FormHelper.bindLabel(this, fieldGroup, label);
		this.fieldList.add(field);
		return field;
	}

	public ComboBox bindEnumField(String fieldLabel, String fieldName, Class<?> clazz)
	{
		ComboBox field = FormHelper.bindEnumField(this, fieldGroup, fieldLabel, fieldName, clazz);
		this.fieldList.add(field);
		return field;
	}

	public CheckBox bindBooleanField(String fieldLabel, String fieldName)
	{
		CheckBox field = FormHelper.bindBooleanField(this, fieldGroup, fieldLabel, fieldName);
		this.fieldList.add(field);
		return field;
	}

	public ComboBox bindEntityField(String fieldLabel, String fieldName, String listFieldName,
			Class<? extends BaseEntity> listClazz)
	{
		ComboBox field = FormHelper.bindEntityField(this, fieldGroup, fieldLabel, fieldName, listFieldName, listClazz);
		this.fieldList.add(field);
		return field;
	}

	public <T extends BaseEntity> TokenField bindTokenField(Selected<T> selected, String fieldLabel, String fieldName,
			Class<? extends BaseEntity> clazz)
	{
		TokenField field = FormHelper.bindTokenField(this, fieldGroup, selected, fieldLabel, fieldName, clazz);
		this.fieldList.add(field);
		return field;
	}

	public ArrayList<AbstractComponent> getFieldList()
	{
		return this.fieldList;
	}


}
