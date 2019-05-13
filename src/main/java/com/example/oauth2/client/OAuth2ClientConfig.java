package com.example.oauth2.client;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private OAuth2ClientContext oauth2Context;

	@Value("${security.oauth2.client.clientId:SampleClientId}")
	private String clientId;
	
	@Value("${security.oauth2.client.clientSecret:secret}")
	private String clientSecret;
	
	@Value("${security.oauth2.client.userAuthorizationUri:http://localhost:8081/auth/oauth/authorize}")
	private String authorizeUrl;

	@Value("${security.oauth2.client.accessTokenUri:http://localhost:8081/auth/oauth/token}")
	private String tokenUrl;
	
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
