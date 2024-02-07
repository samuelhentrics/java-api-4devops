package com.jwt;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.apache.commons.lang3.RandomStringUtils;

import com.auth0.jwt.algorithms.Algorithm;

public class KeyGeneratorUtils {
	
	private static final int MIN_KEY_LENGTH_HSA512 = 512;
	private static final int MIN_KEY_SIZE = 3584;
	private static final int MIN_KEY_ECC_SIZE = 256;

	public static Algorithm generateRSA256() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(MIN_KEY_SIZE);
			KeyPair kp = kpg.generateKeyPair();
			RSAPublicKey rsaPublicKey = (RSAPublicKey) kp.getPublic();
			RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) kp.getPrivate();
			return Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("You need to enable Algorithm.RSA256");
		}
	}
	
	public static Algorithm generateRSA512() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(MIN_KEY_SIZE);
			KeyPair kp = kpg.generateKeyPair();
			RSAPublicKey rsaPublicKey = (RSAPublicKey) kp.getPublic();
			RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) kp.getPrivate();
			return Algorithm.RSA512(rsaPublicKey, rsaPrivateKey);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("You need to enable Algorithm.RSA512");
		}
	}

	public static Algorithm generateES256() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
			kpg.initialize(MIN_KEY_ECC_SIZE);
			KeyPair kp = kpg.generateKeyPair();
			ECPrivateKey ecPrivateKey = (ECPrivateKey) kp.getPrivate();
			ECPublicKey ecPublicKey = (ECPublicKey) kp.getPublic();
			return Algorithm.ECDSA256(ecPublicKey, ecPrivateKey);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("You need to enable Algorithm.ES256");
		}
	}

	public static Algorithm generateES512() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
			kpg.initialize(MIN_KEY_ECC_SIZE);
			KeyPair kp = kpg.generateKeyPair();
			ECPrivateKey ecPrivateKey = (ECPrivateKey) kp.getPrivate();
			ECPublicKey ecPublicKey = (ECPublicKey) kp.getPublic();
			return Algorithm.ECDSA512(ecPublicKey, ecPrivateKey);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("You need to enable Algorithm.ES512");
		}
	}

	public static Algorithm generateHSA512() {
		String secretKey = keyGenerator(MIN_KEY_LENGTH_HSA512);
		return Algorithm.HMAC512(secretKey);
	}

	public static String keyGenerator(int keyLength) {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
		String key = RandomStringUtils.random( keyLength, characters );
		return key;
	}
	
}
