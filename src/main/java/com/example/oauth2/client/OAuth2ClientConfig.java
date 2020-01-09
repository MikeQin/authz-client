/*******************************************************************************
 * Copyright (c) 2019 Michael Qin
 *
 * The freedom to run the program as you wish, for any purpose (freedom 0).
 *
 * The freedom to study how the program works, and change it so it does your computing 
 * as you wish (freedom 1). Access to the source code is a precondition for this.
 *
 * The freedom to redistribute copies so you can help your neighbor (freedom 2).
 *
 * The freedom to distribute copies of your modified versions to others (freedom 3). 
 * By doing this you can give the whole community a chance to benefit from your changes. 
 * Access to the source code is a precondition for this.
 *
 * Contributors:
 *     Michael Qin - initial API and implementation
 *******************************************************************************/
package com.example.oauth2.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Configuration
@EnableOAuth2Client
public class OAuth2ClientConfig {

	private final OAuth2ClientContext oauth2Context;

	@Value("${security.oauth2.client.clientId:SampleClientId}")
	private String clientId;
	
	@Value("${security.oauth2.client.clientSecret:secret}")
	private String clientSecret;
	
	@Value("${security.oauth2.client.userAuthorizationUri:http://localhost:8081/auth/oauth/authorize}")
	private String authorizeUrl;

	@Value("${security.oauth2.client.accessTokenUri:http://localhost:8081/auth/oauth/token}")
	private String tokenUrl;

	public OAuth2ClientConfig(@Qualifier("oauth2ClientContext") OAuth2ClientContext oauth2ClientContext) {
		this.oauth2Context = oauth2ClientContext;
	}

	@Bean
	public OAuth2RestTemplate restTemplate() {

		return new OAuth2RestTemplate(resource(), oauth2Context);
	}
	
	@Bean
	protected OAuth2ProtectedResourceDetails resource() {
		
		AuthorizationCodeResourceDetails resource = new AuthorizationCodeResourceDetails();
		resource.setClientId(clientId);
		resource.setAccessTokenUri(tokenUrl);
		resource.setUserAuthorizationUri(authorizeUrl);
		resource.setClientSecret(clientSecret);
		
		return resource ;
	}
}
