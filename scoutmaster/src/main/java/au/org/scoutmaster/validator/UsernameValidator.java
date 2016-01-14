package au.org.scoutmaster.validator;

import com.vaadin.data.Validator;

/**
 * Validator for validating the username
 */
public final class UsernameValidator implements Validator
{
	private static final long serialVersionUID = 1L;

	public UsernameValidator()
	{
	}

	@Override
	public void validate(final Object objectValue)
	{
		final String value = (String) objectValue;
		//
		// username must be at least 7 characters long and contain at least
		// one number
		//
		if (value == null)
		{
			throw new InvalidValueException("The username must not be null.");
		}
		if (value.length() < 7)
		{
			throw new InvalidValueException("The username must be at least 8 characters long.");
		}

		// printable characters only.
		if (!value.matches("[a-zA-Z0-9!#$%&'*+\\-/=?\\^_`{|}~.@]*"))
		{
			throw new InvalidValueException("The username may only contain valid email address characters.");
		}
	}
}