package com.jwt;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.HttpHeaders;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.extern.log4j.Log4j2;


@Log4j2
public class JWTTokenHelper {
	
	private static final String AUTH_HEADER_KEY = "Authorization";
	private static final String AUTH_HEADER_VALUE_PREFIX = "bearer ";
	private static JWTTokenHelper jwtTokenHelper;
	private final int EXPIRATION_LIMIT; // Time to validate token
	private final String AUDIENCE;
	private final String ISSUER;
	private Algorithm algorithm;
	
	private List<String> blackList;

	private JWTTokenHelper(JWTAlgorithm jwtAlgorithm, int expirationLimitDay, String audience, String issuer) {
		if (jwtAlgorithm == JWTAlgorithm.HS512) {
			this.algorithm = KeyGeneratorUtils.generateHSA512();
		} else if (jwtAlgorithm == JWTAlgorithm.RS256) {
			this.algorithm = KeyGeneratorUtils.generateRSA256(); 
		} else if (jwtAlgorithm == JWTAlgorithm.RS512) {
			this.algorithm = KeyGeneratorUtils.generateRSA512(); 
		} else if (jwtAlgorithm == JWTAlgorithm.ES256) {
			this.algorithm = KeyGeneratorUtils.generateES256();
		} else if (jwtAlgorithm == JWTAlgorithm.ES512) {
			this.algorithm = KeyGeneratorUtils.generateES512();
		}
		this.AUDIENCE = audience;
		this.ISSUER = issuer;
		this.EXPIRATION_LIMIT = expirationLimitDay;
		this.blackList = new ArrayList<String>();
	}
	
	public static JWTTokenHelper getInstance(JWTAlgorithm jwtAlgorithm, int expirationLimitDay, String audience,
			String issuer) {
		if (jwtTokenHelper == null) {
			jwtTokenHelper = new JWTTokenHelper(jwtAlgorithm, expirationLimitDay, audience, issuer);
		}
		return jwtTokenHelper;
	}
	
	public static JWTTokenHelper getInstance() {
		return jwtTokenHelper;
	}

	public String generateToken(String userId) {
		return JWT.create()
				.withJWTId(UUID.randomUUID().toString())
				.withSubject(userId)
				.withAudience(AUDIENCE).withIssuer(ISSUER)
				.withIssuedAt(new Date(System.currentTimeMillis()))
				.withExpiresAt(getExpirationDate())
				.sign(algorithm);
	}
	
	public Date getExpirationDate() {
		long currentTimeInMillis = System.currentTimeMillis();
		long expMilliSeconds = TimeUnit.DAYS.toMillis(EXPIRATION_LIMIT);
		return new Date(currentTimeInMillis + expMilliSeconds);
	}
	
	public void checkJwtValidity(String token) throws JWTException {
		JWTVerifier verifier = JWT.require(algorithm).withAudience(AUDIENCE).withIssuer(ISSUER).build();
		DecodedJWT jwt = verifier.verify(token);
		Date expiresAt = jwt.getExpiresAt();
		Date nowDate = new Date(System.currentTimeMillis());
		if(nowDate.after(expiresAt)) {
			throw new JWTException("The token is expired.");
		}
		if (blackList.contains(jwt.getId())) {
			throw new JWTException("The token is no longer valid due to previous logout");
		}
	}
	
	public void checkJwtValidity(String token, PublicKey rsaKey) throws JWTException {
		Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) rsaKey, null);
		JWTVerifier verifier = JWT.require(algorithm).build();
		DecodedJWT jwt = verifier.verify(token);
		Date expiresAt = jwt.getExpiresAt();
		Date nowDate = new Date(System.currentTimeMillis());
		if(nowDate.after(expiresAt)) {
			throw new JWTException("The token is expired.");
		}
		if (blackList.contains(jwt.getId())) {
			throw new JWTException("The token is no longer valid due to previous logout");
		}
	}
	
	public DecodedJWT decodedJWT(String token) {
		return JWT.decode(token);
	}

	public Algorithm getAlgorithm() {
		return this.algorithm;
	}

	public String refreshToken(HttpHeaders headers) throws JWTException {
		String token = getBearerToken(headers.getHeaderString(AUTH_HEADER_KEY));
		checkJwtValidity(token);
		return generateToken(JWT.decode(token).getSubject());
	}

	public String getBearerToken(String authHeader) throws JWTException {
		if (authHeader != null && authHeader.toLowerCase().startsWith(AUTH_HEADER_VALUE_PREFIX)) {
			return authHeader.substring(AUTH_HEADER_VALUE_PREFIX.length());
		}

		throw new JWTException("No BEARER TOKEN.");
	}

	public String logout(HttpHeaders headers) throws JWTException {
		String token = getBearerToken(headers.getHeaderString(AUTH_HEADER_KEY));
		DecodedJWT jwt = this.getDecodedToken(token);
		this.blackList.add(jwt.getId());
		blackList.forEach(arg -> log.info("list arg : " + arg));
		return "done";
	}

	public String getTGIFromJWT(HttpHeaders headers) throws JWTException {
		String token = getBearerToken(headers.getHeaderString(AUTH_HEADER_KEY));
		return JWT.decode(token).getSubject();
	}
	
	public String getTGIFromJWTKeyCloak(HttpHeaders headers) throws JWTException {
		String token = getBearerToken(headers.getHeaderString(AUTH_HEADER_KEY));
		return JWT.decode(token).getClaim("preferred_username").asString().toUpperCase();
	}

	public DecodedJWT getDecodedToken(String token) {
		JWTVerifier verifier = JWT.require(algorithm).withAudience(AUDIENCE).withIssuer(ISSUER).build();
		DecodedJWT jwt = verifier.verify(token);
		return jwt;
	}

}
