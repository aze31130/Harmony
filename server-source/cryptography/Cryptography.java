package cryptography;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Cryptography {
    public static final String cipherName = "RSA/ECB/PKCS1Padding";
	public static final String key_algorithm = "RSA";

	public static KeyPair generateKeyPair(int keySize) {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance(key_algorithm);
			generator.initialize(keySize);
			return generator.generateKeyPair();
		} catch (NoSuchAlgorithmException algoNotFound) {
			algoNotFound.printStackTrace();
		}
		return null;
	}

    public static byte[] encrypt(byte[] input, PublicKey pubKey) {
        try {
			Cipher cipher = Cipher.getInstance(cipherName);
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			cipher.update(input);
			return cipher.doFinal();
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
    }

    public static byte[] decrypt(byte[] input, PrivateKey priKey) {
        try {
			Cipher cipher = Cipher.getInstance(cipherName);
			cipher.init(Cipher.DECRYPT_MODE, priKey);
			cipher.update(input);
			return cipher.doFinal();
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
    }
}
