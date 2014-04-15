package au.org.scoutmaster.util;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/*
 * PBKDF2 salted password hashing.
 * Author: havoc AT defuse.ca
 * www: http://crackstation.net/hashing-security.htm
 */
public class PasswordHash
{
	public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

	// The following constants may be changed without breaking existing hashes.
	public static final int SALT_BYTE_SIZE = 24;
	public static final int HASH_BYTE_SIZE = 24;
	public static final int PBKDF2_ITERATIONS = 1000;

	public static final int ITERATION_INDEX = 0;
	public static final int SALT_INDEX = 1;
	public static final int PBKDF2_INDEX = 2;

	/**
	 * Returns a salted PBKDF2 hash of the password.
	 *
	 * @param password
	 *            the password to hash
	 * @return a salted PBKDF2 hash of the password
	 */
	public static String createHash(final String password) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		return PasswordHash.createHash(password.toCharArray());
	}

	/**
	 * Returns a salted PBKDF2 hash of the password.
	 *
	 * @param password
	 *            the password to hash
	 * @return a salted PBKDF2 hash of the password
	 */
	public static String createHash(final char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		// Generate a random salt
		final SecureRandom random = new SecureRandom();
		final byte[] salt = new byte[PasswordHash.SALT_BYTE_SIZE];
		random.nextBytes(salt);

		// Hash the password
		final byte[] hash = PasswordHash.pbkdf2(password, salt, PasswordHash.PBKDF2_ITERATIONS,
				PasswordHash.HASH_BYTE_SIZE);
		// format iterations:salt:hash
		return PasswordHash.PBKDF2_ITERATIONS + ":" + PasswordHash.toHex(salt) + ":" + PasswordHash.toHex(hash);
	}

	/**
	 * Validates a password using a hash.
	 *
	 * @param password
	 *            the password to check
	 * @param correctHash
	 *            the hash of the valid password
	 * @return true if the password is correct, false if not
	 */
	public static boolean validatePassword(final String password, final String correctHash)
			throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		return PasswordHash.validatePassword(password.toCharArray(), correctHash);
	}

	/**
	 * Validates a password using a hash.
	 *
	 * @param password
	 *            the password to check
	 * @param correctHash
	 *            the hash of the valid password
	 * @return true if the password is correct, false if not
	 */
	public static boolean validatePassword(final char[] password, final String correctHash)
			throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		// Decode the hash into its parameters
		final String[] params = correctHash.split(":");
		final int iterations = Integer.parseInt(params[PasswordHash.ITERATION_INDEX]);
		final byte[] salt = PasswordHash.fromHex(params[PasswordHash.SALT_INDEX]);
		final byte[] hash = PasswordHash.fromHex(params[PasswordHash.PBKDF2_INDEX]);
		// Compute the hash of the provided password, using the same salt,
		// iteration count, and hash length
		final byte[] testHash = PasswordHash.pbkdf2(password, salt, iterations, hash.length);
		// Compare the hashes in constant time. The password is correct if
		// both hashes match.
		return PasswordHash.slowEquals(hash, testHash);
	}

	/**
	 * Compares two byte arrays in length-constant time. This comparison method
	 * is used so that password hashes cannot be extracted from an on-line
	 * system using a timing attack and then attacked off-line.
	 *
	 * @param a
	 *            the first byte array
	 * @param b
	 *            the second byte array
	 * @return true if both byte arrays are the same, false if not
	 */
	private static boolean slowEquals(final byte[] a, final byte[] b)
	{
		int diff = a.length ^ b.length;
		for (int i = 0; i < a.length && i < b.length; i++)
		{
			diff |= a[i] ^ b[i];
		}
		return diff == 0;
	}

	/**
	 * Computes the PBKDF2 hash of a password.
	 *
	 * @param password
	 *            the password to hash.
	 * @param salt
	 *            the salt
	 * @param iterations
	 *            the iteration count (slowness factor)
	 * @param bytes
	 *            the length of the hash to compute in bytes
	 * @return the PBDKF2 hash of the password
	 */
	private static byte[] pbkdf2(final char[] password, final byte[] salt, final int iterations, final int bytes)
			throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		final PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
		final SecretKeyFactory skf = SecretKeyFactory.getInstance(PasswordHash.PBKDF2_ALGORITHM);
		return skf.generateSecret(spec).getEncoded();
	}

	/**
	 * Converts a string of hexadecimal characters into a byte array.
	 *
	 * @param hex
	 *            the hex string
	 * @return the hex string decoded into a byte array
	 */
	private static byte[] fromHex(final String hex)
	{
		final byte[] binary = new byte[hex.length() / 2];
		for (int i = 0; i < binary.length; i++)
		{
			binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return binary;
	}

	/**
	 * Converts a byte array into a hexadecimal string.
	 *
	 * @param array
	 *            the byte array to convert
	 * @return a length*2 character string encoding the byte array
	 */
	private static String toHex(final byte[] array)
	{
		final BigInteger bi = new BigInteger(1, array);
		final String hex = bi.toString(16);
		final int paddingLength = array.length * 2 - hex.length();
		if (paddingLength > 0)
		{
			return String.format("%0" + paddingLength + "d", 0) + hex;
		}
		else
		{
			return hex;
		}
	}

	/**
	 * Tests the basic functionality of the PasswordHash class
	 *
	 * @param args
	 *            ignored
	 */
	public static void main(final String[] args)
	{
		try
		{
			// Print out 10 hashes
			for (int i = 0; i < 10; i++)
			{
				System.out.println(PasswordHash.createHash("p\r\nassw0Rd!"));
			}

			// Test password validation
			boolean failure = false;
			System.out.println("Running tests...");
			for (int i = 0; i < 100; i++)
			{
				final String password = "" + i;
				final String hash = PasswordHash.createHash(password);
				final String secondHash = PasswordHash.createHash(password);
				if (hash.equals(secondHash))
				{
					System.out.println("FAILURE: TWO HASHES ARE EQUAL!");
					failure = true;
				}
				final String wrongPassword = "" + (i + 1);
				if (PasswordHash.validatePassword(wrongPassword, hash))
				{
					System.out.println("FAILURE: WRONG PASSWORD ACCEPTED!");
					failure = true;
				}
				if (!PasswordHash.validatePassword(password, hash))
				{
					System.out.println("FAILURE: GOOD PASSWORD NOT ACCEPTED!");
					failure = true;
				}
			}
			if (failure)
			{
				System.out.println("TESTS FAILED!");
			}
			else
			{
				System.out.println("TESTS PASSED!");
			}
		}
		catch (final Exception ex)
		{
			System.out.println("ERROR: " + ex);
		}
	}

}