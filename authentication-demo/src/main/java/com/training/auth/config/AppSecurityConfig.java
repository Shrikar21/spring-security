package com.training.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	 // Authentication
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {		
		
		 auth.inMemoryAuthentication()
		       .withUser("alex").password(passwordEncoder.encode("alex@123")).roles("USER")
		       .and()
		       .withUser("anna").password(passwordEncoder.encode("anna@123")).roles("ADMIN")
		       .and()
		       .withUser("bob").password(passwordEncoder.encode("bob@123")).roles("USER")
		       .and()
		       .passwordEncoder(passwordEncoder);		        
	}
	
	// Authorization
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
		      .antMatchers("/api/v1/admin/**")
		      .hasRole("ADMIN")
		      .antMatchers("/api/v1/user/**")
		      .hasAnyRole("USER", "ADMIN")
		      .antMatchers("/api/v1/greet/**")
		      .hasAnyRole("USER", "ADMIN")
		      .anyRequest()
		      .permitAll()
		      .and()
		      .formLogin();		      
	}
}













