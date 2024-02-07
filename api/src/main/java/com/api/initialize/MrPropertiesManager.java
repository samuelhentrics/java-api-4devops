package com.api.initialize;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ejb.Stateless;
import javax.servlet.ServletContextListener;

import com.api.data.configuration.ApplicationProperties;

import lombok.extern.log4j.Log4j2;

@Stateless
@Log4j2
public class MrPropertiesManager implements ServletContextListener {

	private static ApplicationProperties appProperties;
	private static final String ENVIRONMENT = "environment";
	private static final String KEYCLOAK_REALM_URL = "keycloak_realm_url";
	private static final String KEYCLOAK_CLIENT_ID = "keycloak_client_id";

	public MrPropertiesManager()  {
		log.info("Start PropertiesManager Class");
		Properties properties = new Properties();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		try (InputStream input = classLoader.getResourceAsStream("application.properties")) {
			properties.load(input);
			appProperties = new ApplicationProperties();
			appProperties.setEnvironment(properties.getProperty(ENVIRONMENT));
			appProperties.setKeycloakRealmUrl(properties.getProperty(KEYCLOAK_REALM_URL));
			appProperties.setKeycloakClientId(properties.getProperty(KEYCLOAK_CLIENT_ID));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static ApplicationProperties get() {
		return appProperties;
	}

}
