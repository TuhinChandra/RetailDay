package com.tcs.KingfisherDay.service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncryptionService {

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

	public String generateSecurePassword(String password, String salt) {
		String returnValue = null;
		byte[] securePassword = hash(password.toCharArray(), salt.getBytes());

		returnValue = Base64.getEncoder().encodeToString(securePassword);

		return returnValue;
	}

	public boolean verifyUserPassword(String providedPassword, String securedPassword, String salt) {
		boolean returnValue = false;

		// Generate New secure password with the same salt
		String newSecurePassword = generateSecurePassword(providedPassword, salt);

		// Check if two passwords are equal
		returnValue = newSecurePassword.equalsIgnoreCase(securedPassword);

		return returnValue;
	}

	public String getEncryptedPassword(String myPassword) {

		// Protect user's password. The generated value can be stored in DB.
		String mySecurePassword = generateSecurePassword(myPassword, passwordSalt);

		// Print out protected password
		System.out.println("My secure password = " + mySecurePassword);
		System.out.println("Salt value = " + passwordSalt);
		return mySecurePassword;
	}

	public boolean isPasswordValid(String providedPassword, String securePassword) {

		boolean passwordMatch = verifyUserPassword(providedPassword, securePassword, passwordSalt);

		if (passwordMatch) {
			System.out.println("Provided user password " + providedPassword + " is correct.");
		} else {
			System.out.println("Provided password is incorrect");
		}
		return passwordMatch;
	}
}