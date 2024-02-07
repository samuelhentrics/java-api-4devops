package com.api.app;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import com.api.exception.mapper.DefaultExceptionMapper;
import com.api.exception.mapper.JWTExceptionMapper;
import com.api.exception.mapper.MrExceptionMapper;
import com.api.filters.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwt.JWTAlgorithm;
import com.jwt.JWTTokenHelper;

import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.log4j.Log4j2;

@ApplicationPath("api")
@WebListener
@Log4j2
public class ApiApplication extends ResourceConfig implements ServletContextListener {

	@SuppressWarnings("rawtypes")
	public ApiApplication() {
		super();
		packages("com.api.resources").register(MultiPartFeature.class);
		register(new JacksonFeature());
		register(new ObjectMapperContextResolver());
		register(DefaultExceptionMapper.class);
		register(MrExceptionMapper.class);
		register(JWTExceptionMapper.class);
		register(JwtAuthenticationFilter.class);
		
		log.debug("Lancement du ressource config de l'application.");
		
		log.debug("Création de l'instance JWTTokenHelper.");
		JWTTokenHelper.getInstance(JWTAlgorithm.RS512, 7, "api", "/security");
		log.debug("Instance JWTTokenHelper Alg : " + JWTTokenHelper.getInstance().getAlgorithm());

		log.debug("Création de l'OpenAPI (Swagger).");
		OpenAPI openApi = new OpenAPI();
		Info info = new Info().title("Open API").description("Documentation de l'API.")
				.contact(new Contact().email("nicolas@example.com")).version("0.0.1");
		openApi.info(info);
		openApi.schemaRequirement("bearer-auth",
				new SecurityScheme().scheme("BEARER").description("JWT authentication with bearer token.")
						.name("Authorization").type(SecurityScheme.Type.APIKEY).bearerFormat("BEARER [token]")
						.in(SecurityScheme.In.HEADER));
		log.debug("Open API info. " + openApi.toString());
		
		Server server = new Server();
		server.setUrl("http://localhost:8080/api-0.0.1");
		openApi.addServersItem(server);
		log.debug("Server URL : " + server.getUrl());
		
		SwaggerConfiguration oasConfig = new SwaggerConfiguration()
				.resourcePackages(Stream
						.of("com.api.resources")
						.collect(Collectors.toSet()))
				.prettyPrint(true).cacheTTL(0L)
				.scannerClass("io.swagger.v3.jaxrs2.integration.JaxrsAnnotationScanner")
				.readAllResources(false).openAPI(openApi);
		log.debug("Swagger Configuration : " + oasConfig.getResourcePackages().toString());
		
		OpenApiResource openApiResource = new OpenApiResource();
		openApiResource.setOpenApiConfiguration(oasConfig);
		register(openApiResource);
		try {
			new JaxrsOpenApiContextBuilder().application(this.getApplication()).openApiConfiguration(oasConfig).buildContext(true);
		} catch (OpenApiConfigurationException e) {
			log.error("Error on swagger config.");
			throw new RuntimeException(e.getMessage(), e);
		}
		log.debug("Fin de la génération de l'OpenAPI descriptor.");
		log.info("Token généré pour tester l'api : " + JWTTokenHelper.getInstance(JWTAlgorithm.RS256, 365, "Test", "Test").generateToken("test"));
	}
	

	@Provider
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

		private final ObjectMapper mapper;

		public ObjectMapperContextResolver() {
			this.mapper = createObjectMapper();
		}

		@Override
		public ObjectMapper getContext(Class<?> type) {
			return mapper;
		}

		private ObjectMapper createObjectMapper() {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
			return mapper;
		}

	}

}
