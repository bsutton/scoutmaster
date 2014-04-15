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
				return RandomString.hex;
			}
		},
		ALPHA
		{
			@Override
			char[] getSymbols()
			{
				return RandomString.alpha;
			}
		},
		ALPHANUMERIC
		{
			@Override
			char[] getSymbols()
			{
				return RandomString.alphaNumeric;
			}
		},
		NUMERIC
		{
			@Override
			char[] getSymbols()
			{
				return RandomString.numeric;
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
			RandomString.numeric[idx] = (char) ('0' + idx);
			RandomString.hex[idx] = (char) ('0' + idx);
			RandomString.alphaNumeric[idx] = (char) ('0' + idx);
		}

		// load the alpha symbols
		for (int idx = 0; idx < 26; idx++)
		{
			RandomString.alpha[idx * 2] = (char) ('a' + idx);
			RandomString.alpha[idx * 2 + 1] = (char) ('A' + idx);
			RandomString.alphaNumeric[idx * 2 + 10] = (char) ('a' + idx);
			RandomString.alphaNumeric[idx * 2 + 11] = (char) ('A' + idx);
		}

		// load the hex symbols
		for (int idx = 0; idx < 6; idx++)
		{
			RandomString.hex[idx + 10] = (char) ('A' + idx);
		}
	}

	private final Random random = new SecureRandom();

	private final char[] buf;
	private final Type type;

	public RandomString(final Type type, final int length)
	{
		this.type = type;
		if (length < 1)
		{
			throw new IllegalArgumentException("length < 1: " + length);
		}
		this.buf = new char[length];
	}

	public String nextString()
	{
		for (int idx = 0; idx < this.buf.length; ++idx)
		{
			this.buf[idx] = this.type.getSymbols()[this.random.nextInt(this.type.getSymbols().length)];
		}
		return new String(this.buf);
	}

}
