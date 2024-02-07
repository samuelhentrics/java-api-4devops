package com.jwt;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.auth0.jwt.algorithms.Algorithm;

class KeyGeneratorUtilsTest {

	@Test
	void testGenerateRSA256() {
		Algorithm algorithm = KeyGeneratorUtils.generateRSA256();
		assertTrue(algorithm.getName().equals("RS256"));
	}

	@Test
	void testGenerateRSA512() {
		Algorithm algorithm = KeyGeneratorUtils.generateRSA512();
		assertTrue(algorithm.getName().equals("RS512"));
	}

	@Test
	void testGenerateES256() {
		Algorithm algorithm = KeyGeneratorUtils.generateES256();
		assertTrue(algorithm.getName().equals("ES256"));
	}

	@Test
	void testGenerateES512() {
		Algorithm algorithm = KeyGeneratorUtils.generateES512();
		assertTrue(algorithm.getName().equals("ES512"));
	}

	@Test
	void testGenerateHSA512() {
		Algorithm algorithm = KeyGeneratorUtils.generateHSA512();
		assertTrue(algorithm.getName().equals("HS512"));
	}

	@Test
	void testKeyGenerator() {
		String key = KeyGeneratorUtils.keyGenerator(128);
		assertTrue(key.length() == 128);
	}

}
