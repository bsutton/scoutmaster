package au.org.scoutmaster.validator;

import java.util.Date;

import com.vaadin.data.Validator;
import com.vaadin.ui.DateField;

public class DateRangeValidator implements Validator
{
	private static final long serialVersionUID = 1L;
	private DateField endDateField;
	private String startDateCaption;

	public DateRangeValidator(String label, DateField endDateField)
	{
		this.startDateCaption = label;
		this.endDateField = endDateField;
	}

	@Override
	public void validate(Object object) throws InvalidValueException
	{
		Date value = (Date) object;

		if (value == null  || this.endDateField.getValue() == null || value.after(this.endDateField.getValue()))
		{
			throw new InvalidValueException("The " + startDateCaption + " must be before the " + endDateField.getCaption() );
		}
	}

}
