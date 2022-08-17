package cryptography;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.PBEKeySpec;

public class Cryptography {
	public static final String cipherName = "RSA/ECB/PKCS1Padding";
	public static final String key_algorithm = "RSA";

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

	public static PublicKey loadPublicKey(byte[] publicKey) {
		try {
			X509EncodedKeySpec publicKeySpecs = new X509EncodedKeySpec(publicKey);
			KeyFactory keyFactory = KeyFactory.getInstance(key_algorithm);
			
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

	public static KeyPair loadKeyPair() {
		try {
			byte[] privateKeyBytes = Files.readAllBytes(Paths.get("./id_rsa"));
			byte[] publicKeyBytes = Files.readAllBytes(Paths.get("./id_rsa.pub"));

			PKCS8EncodedKeySpec privateKeySpecs = new PKCS8EncodedKeySpec(privateKeyBytes);
			X509EncodedKeySpec publicKeySpecs = new X509EncodedKeySpec(publicKeyBytes);

			KeyFactory keyFactory = KeyFactory.getInstance(key_algorithm);

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
	
	public static String encrypt(String strToEncrypt, String secret, String salt) {
		try {
			byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
			IvParameterSpec ivspec = new IvParameterSpec(iv);
			
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(secret.toCharArray(), salt.getBytes(), 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
			
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String decrypt(String strToDecrypt, String secret, String salt) {
		try {
			byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
			IvParameterSpec ivspec = new IvParameterSpec(iv);
			
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(secret.toCharArray(), salt.getBytes(), 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
			
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
