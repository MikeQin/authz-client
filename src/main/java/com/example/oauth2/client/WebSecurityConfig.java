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
