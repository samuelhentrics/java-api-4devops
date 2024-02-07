package com.api.jpa.bootstrap.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PreDestroy;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.eclipse.persistence.config.PersistenceUnitProperties;

@WebListener
public class JpaEntityManager implements ServletContextListener {

	private static EntityManagerFactory emFactoryObj;
	private static final String PERSISTENCE_UNIT_NAME = "api";
	private static final String JDBC_DRIVER = "jdbc_driver";
	private static final String JDBC_URL = "jdbc_url";
	private static final String JDBC_USER = "jdbc_user";
	private static final String JDBC_PASSWORD = "jdbc_password";
	private static final Map<String, String> jdbcProperties = new HashMap<String, String>();

	private JpaEntityManager() {
		Properties properties = new Properties();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		try(InputStream input = classLoader.getResourceAsStream("application.properties")){
			properties.load(input);	
		} catch (IOException e) {
			e.printStackTrace();
		}

		jdbcProperties.put(PersistenceUnitProperties.TRANSACTION_TYPE,
				PersistenceUnitTransactionType.RESOURCE_LOCAL.name());
		jdbcProperties.put(PersistenceUnitProperties.JDBC_DRIVER, properties.getProperty(JDBC_DRIVER));
		jdbcProperties.put(PersistenceUnitProperties.JDBC_URL, properties.getProperty(JDBC_URL));
		jdbcProperties.put(PersistenceUnitProperties.JDBC_USER, properties.getProperty(JDBC_USER));
		jdbcProperties.put(PersistenceUnitProperties.JDBC_PASSWORD, properties.getProperty(JDBC_PASSWORD));

		emFactoryObj = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, jdbcProperties);

	}
	
	public static EntityManagerFactory getEntityManagerFactory() {
		return emFactoryObj;
	}

	@PreDestroy
	public void destruct() {
		emFactoryObj.close();
	}


}
