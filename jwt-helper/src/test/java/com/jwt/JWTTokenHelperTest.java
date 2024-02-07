package com.jwt;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class JWTTokenHelperTest {

	@Test
	void testGetInstance() {
		JWTTokenHelper jwtTokenHelper = JWTTokenHelper.getInstance(JWTAlgorithm.HS512, 1, "my app",
				"mondomaine.com/security");
		JWTTokenHelper jwtTokenHelperSameInstance = JWTTokenHelper.getInstance(JWTAlgorithm.ES256, 1, "my app",
				"mondomaine.com/security");
		assertTrue(jwtTokenHelper == jwtTokenHelperSameInstance);
	}

	@Test
	void testGenerateToken() {
		JWTTokenHelper jwtTokenHelper = JWTTokenHelper.getInstance(JWTAlgorithm.HS512, 1, "my app",
				"mondomaine.com/security");
		String token = jwtTokenHelper.generateToken("login");
		jwtTokenHelper.decodedJWT(token);
	}

	@Test
	void testCheckJwtValidity() throws JWTException {
		JWTTokenHelper jwtTokenHelper = JWTTokenHelper.getInstance(JWTAlgorithm.HS512, 1, "my app",
				"mondomaine.com/security");
		String token = jwtTokenHelper.generateToken("login");
		jwtTokenHelper.checkJwtValidity(token);
	}

}
