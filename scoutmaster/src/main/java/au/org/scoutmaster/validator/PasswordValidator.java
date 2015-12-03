package au.org.scoutmaster.validator;

import com.vaadin.data.Validator;
import com.vaadin.ui.PasswordField;

public class PasswordValidator implements Validator
{
	private static final long serialVersionUID = 1L;
	private final String label;
	private final PasswordField otherField;
	
	public static final String validationRule = "Password must be 8-30 characters, with at least 2 each of digits, letters and special chars i.e. !@#$%^&*()";

	public PasswordValidator(final String label)
	{
		this.label = label;
		this.otherField = null;
	}

	public PasswordValidator(final String label, final PasswordField otherField)
	{
		this.label = label;
		this.otherField = otherField;
	}

	@Override
	public void validate(final Object object) throws InvalidValueException
	{
		final String value = (String) object;

		if (value.length() < 8 || value.length() > 30)
		{
			throw new InvalidValueException("Your " + this.label + " must be between 8 and 30 characters long.");
		}

		int letters = 0, numbers = 0, specialChars = 0;
		for (final char c : value.toCharArray())
		{
			if (c == ' ')
			{
				throw new InvalidValueException("Your  " + this.label + " may not contain a space.");
			}
			else if (Character.isLetter(c))
			{
				++letters;
			}
			else if (Character.isDigit(c))
			{
				++numbers;
			}
			else
			{
				++specialChars;
			}
		}
		if (letters < 2)
		{
			throw new InvalidValueException("Your  " + this.label + " must have at least 2 letters.");
		}

		if (numbers < 2)
		{
			throw new InvalidValueException("Your  " + this.label + " must have at least 2 numbers.");
		}

		if (specialChars < 2)
		{
			throw new InvalidValueException("Your  " + this.label
					+ " must have at least 2 special chars e.g. !@#$%^&*().");
		}

		if (this.otherField != null)
		{
			if (!this.otherField.getValue().equals(value))
			{
				throw new InvalidValueException("The two password fields must be identical.");
			}
		}
	}

}
