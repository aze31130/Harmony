package cryptography;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Cryptography {
	/*
	 * Generates a random string with given length
	 * The length parameter must be positive
	 */
	public static String generateSecureRandomString(int length) {
		if (length <= 0)
			length = 16;
		final String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+-*/&()|_@={}[]$.";
		SecureRandom randomGenerator = new SecureRandom();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < length; i++)
			builder.append(symbols.charAt(randomGenerator.nextInt(symbols.length())));
		return builder.toString();
	}

	/*
	 * Generates RSA keypair
	 */
	public static KeyPair generateKeyPair(int keySize) {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(keySize);
			return generator.generateKeyPair();
		} catch (NoSuchAlgorithmException algoNotFound) {
			algoNotFound.printStackTrace();
		}
		return null;
	}

	/*
	 * Generate symmetric AES key
	 */
	public static SecretKey generateKey(int keySize) {
		try {
			KeyGenerator generator = KeyGenerator.getInstance("AES");
			generator.init(keySize);
			return generator.generateKey();
		} catch (NoSuchAlgorithmException noAlgo) {
			noAlgo.printStackTrace();
		}
		return null;
	}

	/*
	 * Saves RSA keypair into files
	 */
	public static void saveKeyPair(KeyPair keys) {
		PublicKey pubKey = keys.getPublic();
		PrivateKey priKey = keys.getPrivate();

		try {
			FileOutputStream pubKeyFile = new FileOutputStream("id_rsa.pub");
			pubKeyFile.write(pubKey.getEncoded());
			pubKeyFile.close();

			FileOutputStream priKeyFile = new FileOutputStream("id_rsa");
			priKeyFile.write(priKey.getEncoded());
			priKeyFile.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	/*
	 * Load a public key from byte array
	 */
	public static PublicKey loadPublicKey(byte[] publicKey) {
		try {
			X509EncodedKeySpec publicKeySpecs = new X509EncodedKeySpec(publicKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			
			return keyFactory.generatePublic(publicKeySpecs);
		} catch (NoSuchAlgorithmException noAlgoFound) {
			System.err.println("The given keys algo name is not found !");
			noAlgoFound.printStackTrace();
		} catch (InvalidKeySpecException invalidKey) {
			System.err.println("The given public key is invalid !");
			invalidKey.printStackTrace();
		}
		return null;
	}

	/*
	 * Load a symmetric key from byte array
	 */
	public static SecretKey loadSymmetricKey(byte[] symmetricKey) {
		return new SecretKeySpec(symmetricKey, 0, symmetricKey.length, "AES");
	}

	/*
	 * Load RSA keypair from file
	 */
	public static KeyPair loadKeyPair() {
		try {
			byte[] privateKeyBytes = Files.readAllBytes(Paths.get("./id_rsa"));
			byte[] publicKeyBytes = Files.readAllBytes(Paths.get("./id_rsa.pub"));

			PKCS8EncodedKeySpec privateKeySpecs = new PKCS8EncodedKeySpec(privateKeyBytes);
			X509EncodedKeySpec publicKeySpecs = new X509EncodedKeySpec(publicKeyBytes);

			KeyFactory keyFactory = KeyFactory.getInstance("RSA");

			KeyPair keyPair = new KeyPair(keyFactory.generatePublic(publicKeySpecs), keyFactory.generatePrivate(privateKeySpecs));

			return keyPair;
		} catch (IOException exception) {
			System.err.println("Error while reading key files !");
			exception.printStackTrace();
		} catch (NoSuchAlgorithmException noAlgoFound) {
			System.err.println("The given keys algo name is not found !");
			noAlgoFound.printStackTrace();
		} catch (InvalidKeySpecException invalidKey) {
			System.err.println("Given keys are invalid !");
			invalidKey.printStackTrace();
		}
		return null;
	}

	public static byte[] encrypt(PublicKey pubKey, byte[] input) {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			cipher.update(input);
			return cipher.doFinal();
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] decrypt(PrivateKey priKey, byte[] input) {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, priKey);
			cipher.update(input);
			return cipher.doFinal();
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] encrypt(SecretKey symmetricKey, byte[] messageToEncrypt) {
		try {
			Cipher aesCipher = Cipher.getInstance("AES");
			aesCipher.init(Cipher.ENCRYPT_MODE, symmetricKey);
			return aesCipher.doFinal(messageToEncrypt);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] decrypt(SecretKey symmetricKey, byte[] messageToDecrypt) {
		try {
			Cipher aesCipher = Cipher.getInstance("AES");
			aesCipher.init(Cipher.DECRYPT_MODE, symmetricKey);
			return aesCipher.doFinal(messageToDecrypt);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
