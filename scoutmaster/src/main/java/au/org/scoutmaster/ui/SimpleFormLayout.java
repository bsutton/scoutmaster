package au.org.scoutmaster.ui;

import java.util.ArrayList;

import com.vaadin.data.Validator;
import com.vaadin.data.Validator.EmptyValueException;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class SimpleFormLayout extends VerticalLayout
{
	private static final long serialVersionUID = 1L;

	class FieldPair
	{
		public FieldPair(AbstractField<?> field, Label errorLabel)
		{
			this.field = field;
			this.errorLabel = errorLabel;
		}

		AbstractField<?> field;
		Label errorLabel;

		public boolean validate()
		{

			boolean valid = false;
			try
			{
				for (Validator validator : field.getValidators())
				{
					validator.validate(field.getValue());
				}

				if (field.isRequired() && field.isEmpty())
					throw new InvalidValueException("Field is required");
				valid = true;
			}
			catch (InvalidValueException e)
			{
				if (e instanceof EmptyValueException)
					errorLabel.setValue(red("You must provide a value."));
				else
					errorLabel.setValue(red(e.getMessage()));
			}
			if (valid == true)
				errorLabel.setValue("");

			return field.isValid();

		}
	}

	ArrayList<FieldPair> fields = new ArrayList<>();
	private int labelWidth = 150;
	private int fieldWidth = 250;

	public SimpleFormLayout()
	{
		this.setSpacing(true);
	}

	public void addComponent(AbstractField<?> field)
	{
		HorizontalLayout line = new HorizontalLayout();

		Label label = new Label(field.getCaption());
		label.setWidth("" + this.labelWidth);
		line.addComponent(label);

		field.setWidth("" + fieldWidth);
		field.setImmediate(true);
		line.addComponent(field);
		field.setCaption(null);

		Label errorLabel = new Label();
		errorLabel.setContentMode(ContentMode.HTML);
		line.addComponent(errorLabel);

		addDynamicValidation(field);

		fields.add(new FieldPair(field, errorLabel));

		// The first field added to the form is given focus.
		if (fields.size() == 1)
			field.focus();

		this.addComponent(line);
	}

	private void addDynamicValidation(AbstractField<?> field)
	{
		if (field instanceof AbstractTextField)
		{
			((AbstractTextField) field).addTextChangeListener(listener -> {
				validate(field);
			});

			((AbstractTextField) field).addBlurListener(listener -> {
				validate(field);
			});
		}
		else if (field instanceof ComboBox)
		{
			((ComboBox) field).addBlurListener(listener -> {
				validate(field);
			});
			field.addValueChangeListener(listener -> {
				validate(field);
			});
		}
		else
		{
			field.addValueChangeListener(listener -> {
				validate(field);
			});
		}

	}

	public boolean validate()
	{
		boolean valid = true;
		for (FieldPair fieldPair : fields)
		{
			valid &= fieldPair.validate();
		}

		return valid;
	}

	public void validate(AbstractField<?> field)
	{
		for (FieldPair fieldPair : fields)
		{
			if (fieldPair.field == field)
			{
				fieldPair.validate();
				break;
			}
		}

	}

	/**
	 * Explicitly sets the error message for the given field.
	 *
	 * @param groupName
	 * @param string
	 */
	public void setError(TextField field, String error)
	{

		for (FieldPair fieldPair : fields)
		{
			if (fieldPair.field == field)
			{
				fieldPair.errorLabel.setValue(red(error));
				break;
			}
		}
	}

	private String red(String error)
	{
		return "<font color='red'>" + error + "</font>";
	}

	public void setLabelWidth(int labelWidth)
	{
		this.labelWidth = labelWidth;

	}
}
