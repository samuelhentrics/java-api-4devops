package com.api.filters;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.ejb.Stateless;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import com.api.exception.ErrorResponse;
import com.api.initialize.MrPropertiesManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwt.JWTTokenHelper;

import lombok.extern.log4j.Log4j2;

@WebFilter("/*")
@Log4j2
@Stateless
public class JwtAuthenticationFilter implements Filter {

	private static final String AUTH_HEADER_KEY = "Authorization";
	private static final String AUTH_HEADER_VALUE_PREFIX = "BEARER "; // with trailing space to separate token

	private static final int STATUS_CODE_UNAUTHORIZED = 401;
//	private final JWTTokenHelper jwtTokenHelper = JWTTokenHelper.getInstance();
	private static final String OPEN_API_SERVICE = "/api/openapi.json";
	private static final String SWAGGER_UI = "/swagger-ui/";
	
//	private MrPropertiesManager init = new MrPropertiesManager();
//	private String keycloakRealmUrl = MrPropertiesManager.get().getKeycloakRealmUrl();
	
	private JwtAuthenticationFilter() {
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("JwtAuthenticationFilter initialized");
	}

	@Override
	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
			final FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		httpResponse.setHeader("Access-Control-Allow-Headers", "*");
		httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, PATCH");
		httpResponse.setHeader("Access-Control-Max-Age", "3600");

		if (httpRequest.getMethod().equals("OPTIONS")) {
			httpResponse.setStatus(HttpServletResponse.SC_ACCEPTED);
			return;
		}
		String uri = httpRequest.getRequestURI();
		String privateKeyHeaderValue = getBearerToken(httpRequest.getHeader(AUTH_HEADER_KEY));
		//Premier If pour définir les uri qui ne seront pas filtré : ici on laisse accessible sans token OPEN_API_SERVICE et SWAGGER_UI
		log.info(privateKeyHeaderValue);
		if (!uri.contains(OPEN_API_SERVICE) && !uri.contains(SWAGGER_UI)) {
			if (privateKeyHeaderValue == null || privateKeyHeaderValue.isEmpty()) {
				log.warn("Token manquant dans le header HTTP.");
				httpResponse.setContentType("application/json");
				ErrorResponse errorResponse = new ErrorResponse("Token is missing in header");
				httpResponse.setStatus(STATUS_CODE_UNAUTHORIZED);
				httpResponse.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
				httpResponse.getWriter().flush();
				return;
			}
//			try {
//				String user = JWTTokenHelper.getInstance().decodedJWT(privateKeyHeaderValue).getClaim("preferred_username").asString();
//				log.debug(httpRequest.getRemoteAddr() + " USER : " + user + "  " + httpRequest.getMethod() + " " + uri);
//				byte[] keyBytes = Base64.getDecoder().decode(getKeycloakPublicKey());
//				X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(keyBytes);
//				KeyFactory fact = KeyFactory.getInstance("RSA");
//				PublicKey rsakey = fact.generatePublic(encodedKeySpec);
//				jwtTokenHelper.checkJwtValidity(privateKeyHeaderValue, rsakey);
//			} catch (Exception e) {
//				log.warn("Error HTTP 511 : Token is not correct.");
//				httpResponse.setContentType("application/json");
//				ErrorResponse errorResponse = new ErrorResponse("Token is not correct");
//				httpResponse.setStatus(511);
//				httpResponse.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
//				httpResponse.getWriter().flush();
//				return;
//			}
		}

		filterChain.doFilter(httpRequest, httpResponse);
		return;
	}

	@Override
	public void destroy() {
		log.info("JwtAuthenticationFilter destroyed");
	}

	/**
	 * Get the bearer token from the HTTP request. The token is in the HTTP request
	 * "Authorization" header in the form of: "Bearer [token]"
	 */
	private String getBearerToken(String authHeader) {
		if (authHeader != null && authHeader.toUpperCase().startsWith(AUTH_HEADER_VALUE_PREFIX)) {
			return authHeader.substring(AUTH_HEADER_VALUE_PREFIX.length());
		}
		return null;
	}
	
//	private String getKeycloakPublicKey() {
//		Client client = ClientBuilder.newClient();
//		try {
//			String responseBody = client.target(keycloakRealmUrl).request(MediaType.APPLICATION_JSON).get().readEntity(String.class);
//			JSONObject jsonBody = new JSONObject(responseBody);
//			return jsonBody.getString("public_key");
//		} catch (Exception e) {
//			log.warn("The keycloak Realm is unreachable to retrieve the public key");
//		}
//		return null;
//	}

}
