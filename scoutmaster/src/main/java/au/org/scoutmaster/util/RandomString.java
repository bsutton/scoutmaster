package au.org.scoutmaster.util;

import java.security.SecureRandom;
import java.util.Random;

public class RandomString
{

	public enum Type
	{
		HEX
		{
			@Override
			char[] getSymbols()
			{
				return hex;
			}
		},
		ALPHA
		{
			@Override
			char[] getSymbols()
			{
				return alpha;
			}
		},
		ALPHANUMERIC
		{
			@Override
			char[] getSymbols()
			{
				return alphaNumeric;
			}
		},
		NUMERIC
		{
			@Override
			char[] getSymbols()
			{
				return numeric;
			}
		};
		abstract char[] getSymbols();
	};

	private static final char[] alpha = new char[52];
	private static final char[] alphaNumeric = new char[62];
	private static final char[] numeric = new char[10];
	private static final char[] hex = new char[16];

	static
	{
		// load the numeric symbols
		for (int idx = 0; idx < 10; idx++)
		{
			numeric[idx] = (char) ('0' + idx);
			hex[idx] = (char) ('0' + idx);
			alphaNumeric[idx] = (char) ('0' + idx);
		}

		// load the alpha symbols
		for (int idx = 0; idx < 26; idx++)
		{
			alpha[idx * 2] = (char) ('a' + idx);
			alpha[(idx * 2) + 1] = (char) ('A' + idx);
			alphaNumeric[(idx * 2) + 10] = (char) ('a' + idx);
			alphaNumeric[(idx * 2) + 11] = (char) ('A' + idx);
		}

		// load the hex symbols
		for (int idx = 0; idx < 6; idx++)
		{
			hex[idx + 10] = (char) ('A' + idx);
		}
	}

	private final Random random = new SecureRandom();

	private final char[] buf;
	private Type type;

	public RandomString(Type type, int length)
	{
		this.type = type;
		if (length < 1)
			throw new IllegalArgumentException("length < 1: " + length);
		buf = new char[length];
	}

	public String nextString()
	{
		for (int idx = 0; idx < buf.length; ++idx)
			buf[idx] = type.getSymbols()[random.nextInt(type.getSymbols().length)];
		return new String(buf);
	}

}
