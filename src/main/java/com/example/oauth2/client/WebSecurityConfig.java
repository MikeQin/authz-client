package com.example.oauth2.client;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableOAuth2Sso
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

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
}
