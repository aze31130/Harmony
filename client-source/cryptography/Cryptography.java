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
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

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
}
