package au.org.scoutmaster.validator;

import com.vaadin.data.Validator;

public class PasswordValidator implements Validator
{
	private static final long serialVersionUID = 1L;
	private String label;


	public PasswordValidator(String label)
	{
		this.label = label;
	}

	@Override
	public void validate(Object object) throws InvalidValueException
	{
		String value = (String) object;
		
		if (value.length() < 8 || value.length() > 30)
			throw new InvalidValueException("Your " + label + " must be between 8 and 30 characters long.");
		
		int letters = 0, numbers = 0, specialChars = 0;
		for (char c : value.toCharArray())
		{
			if (c == ' ')
				throw new InvalidValueException("Your  " + label + " may not contain a space.");
			else if (Character.isLetter(c))
				++letters;
			else if (Character.isDigit(c))
				++numbers;
			else
				++specialChars;
		}
		if (letters < 2)
			throw new InvalidValueException("Your  " + label + " must have at least 2 letters.");

		if (numbers < 2)
			throw new InvalidValueException("Your  " + label + " must have at least 2 numbers.");

		if (specialChars < 2)
			throw new InvalidValueException("Your  " + label + " must have at least 2 special chars e.g. !@#$%^&*().");
	}

}
