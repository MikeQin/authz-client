package com.example.oauth2;

import java.security.Principal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        //.antMatcher("/**")
        .authorizeRequests()
          .antMatchers("/", "/login**", "/webjars/**", "/error**").permitAll()
          .antMatchers("/user").access("hasRole('ADMIN') and hasRole('USER')")
          .anyRequest().authenticated()
        .and()  // .logoutUrl("/logout")
          .logout()
          .logoutUrl("/logout")
          .logoutSuccessUrl("/")
          .invalidateHttpSession(true)
          .deleteCookies("JSESSIONID")
        .and()
          .csrf() // create cookie for client and put jsessionid in cookie
          .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
  }

  /**
   * Main
   * @param args
   */
  public static void main(String[] args) {
    SpringApplication.run(OAuth2Application.class, args);
  }

}
