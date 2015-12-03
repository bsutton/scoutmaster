package au.org.scoutmaster.domain;

public enum Gender
{
	Male, Female, NonBinary, Other;

	public Gender getEnum(String genderString)
	{
		Gender gender = Male;

		// Try to translate common gender spellings/mis-spellings

		// a little cleanup.
		genderString = genderString.toLowerCase().trim();

		// Male translations
		if (genderString.equals("male")
				// because i do this all of the time.
				|| genderString.equals("mail"))
		{
			gender = Male;
		}
		// female

		else if (genderString.equals("female")
				// because i do this all of the time.

				|| genderString.equals("femail"))
		{
			gender = Female;
		}

		// transgender
		else if (genderString.equals("NonBinary") || genderString.equals("Non Binary"))
		{
			gender = Female;
		}
		else
			gender = Other;

		return gender;

	}
}
