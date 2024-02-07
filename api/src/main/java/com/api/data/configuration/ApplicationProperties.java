package com.api.data.configuration;

import lombok.Data;

@Data
public class ApplicationProperties {
	private String environment;
	private String keycloakRealmUrl;
	private String keycloakClientId;
}

