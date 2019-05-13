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

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ClientAppController {

	@GetMapping(path = "/user")
	public Principal user(Principal principal) {
		log.info("OAuth2Application /user endpoint called ...");
		log.info("Principal Name: {}", principal.getName());
		log.info("Principal: {}", principal);

		return principal;
	}

	@Autowired
	OAuth2RestTemplate oauth2Template;

	@GetMapping(path = "/access_tokens", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> accessTokens() {
		log.info("/access_tokens called ...");
		String json = oauth2Template.getForObject("http://localhost:8081/auth/tokens", String.class);

		return ResponseEntity.ok(json);
	}
	
	@PostMapping(path = "/new_token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> refreshingToken(@RequestParam(name = "refresh_token") String refreshToken) {
		
		log.info("/new_token called ...");
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setBasicAuth("SampleClientId", "secret");
		
		MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
		map.add("grant_type", "refresh_token");
		map.add("refresh_token", refreshToken);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
		ResponseEntity<String> response = oauth2Template.postForEntity(
				"http://localhost:8081/auth/tokens", request , String.class);
		
		log.info("[**] refresh_token: {}", response.getBody());
		
		return ResponseEntity.ok(response.getBody());
	}

	@GetMapping(path = "/r_token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<OAuth2RefreshToken> refreshToken() {
		
		log.info("/refresh_token called ...");
		OAuth2AccessToken token = oauth2Template.getAccessToken();
		OAuth2RefreshToken refreshToken = token.getRefreshToken();
		log.info("[**] refresh_token: {}", refreshToken.getValue());

		return ResponseEntity.ok(refreshToken);
	}
	
	@GetMapping(path = "/token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<OAuth2AccessToken> accessToken() {
		
		log.info("/access_token called ...");		
		OAuth2AccessToken token = oauth2Template.getAccessToken();
		log.info("[**] access_token: {}", token.getValue());
		
		return ResponseEntity.ok(token);
	}	

	@GetMapping(path = "/secret", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> secretResource() {
		log.info("/secret called ...");
		String json = oauth2Template.getForObject("http://localhost:8081/auth/secret", String.class);

		return ResponseEntity.ok(json);
	}
}
