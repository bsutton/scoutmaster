package au.org.scoutmaster.validator;

import java.util.Date;

import com.vaadin.data.Validator;
import com.vaadin.ui.DateField;

public class DateRangeValidator implements Validator
{
	private static final long serialVersionUID = 1L;
	private final DateField endDateField;
	private final String startDateCaption;

	public DateRangeValidator(final String label, final DateField endDateField)
	{
		this.startDateCaption = label;
		this.endDateField = endDateField;
	}

	@Override
	public void validate(final Object object) throws InvalidValueException
	{
		final Date value = (Date) object;

		if (value == null || this.endDateField.getValue() == null || value.after(this.endDateField.getValue()))
		{
			throw new InvalidValueException("The " + this.startDateCaption + " must be before the "
					+ this.endDateField.getCaption());
		}
	}

}
