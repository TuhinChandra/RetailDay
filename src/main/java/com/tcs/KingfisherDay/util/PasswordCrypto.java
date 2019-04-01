package com.tcs.KingfisherDay.util;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PasswordCrypto {
	@Value("${password.encryption.salt}")
	private String passwordSalt;
	@Value("${password.encryption.keyLength}")
	private int keyLength;
	@Value("${password.encryption.iterations}")
	private int iterations;
	@Value("${password.encryption.algorithm}")
	private String algorithm;

	public byte[] hash(char[] password, byte[] salt) {
		PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
		Arrays.fill(password, Character.MIN_VALUE);
		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
			return skf.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
		} finally {
			spec.clearPassword();
		}
	}

	public String encrypt(String password) {
		String returnValue = null;
		byte[] securePassword = hash(password.toCharArray(), passwordSalt.getBytes());
		returnValue = Base64.getEncoder().encodeToString(securePassword);
		return returnValue;
	}

	public boolean validate(String providedPassword, String securedPassword) {
		boolean returnValue = false;
		String newSecurePassword = encrypt(providedPassword);
		returnValue = newSecurePassword.equalsIgnoreCase(securedPassword);
		return returnValue;
	}

}