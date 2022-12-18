package cryptography;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
	public static String sha256sum(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			return Hash.toHexadecimalString(md.digest(input.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return new String();
	}

	public static String toHexadecimalString(byte[] input) {
		BigInteger number = new BigInteger(1, input);
		StringBuilder result = new StringBuilder(number.toString(16));

		while (result.length() < 64)
			result.insert(0, '0');

		return result.toString();
	}
}
