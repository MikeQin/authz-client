package com.example.oauth2;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableOAuth2Sso
@RestController
@Slf4j
public class OAuth2Application extends WebSecurityConfigurerAdapter {
	
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
	
	@GetMapping(path = "/token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<OAuth2AccessToken> tokens() {		
		log.info("/tokens called ...");		
		OAuth2AccessToken token = oauth2Template.getAccessToken();
		
		return ResponseEntity.ok(token);
	}
	
	@GetMapping(path = "/secret", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> secretResource() {
		log.info("/secret called ...");		
		String json = oauth2Template.getForObject("http://localhost:8081/auth/secret", String.class);

		return ResponseEntity.ok(json);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/**")
		.authorizeRequests()
		.antMatchers("/", "/login**", "/webjars/**", "/error**").permitAll()
		.anyRequest().authenticated()
		.and()
		.exceptionHandling()
	      .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
	    .and()
		.logout()
		//.logoutUrl("/logout")
		.logoutSuccessUrl("/")
		//.invalidateHttpSession(true)
		//.deleteCookies("JSESSIONID")
		//.permitAll()
		.and()
		.csrf() // create cookie for client and put jsessionid in cookie
		.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
	}

	/**
	 * Main
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(OAuth2Application.class, args);
	}

}
