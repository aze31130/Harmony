package cryptography;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Cryptography {
    public static String cipherName = "RSA/ECB/PKCS1Padding";

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
