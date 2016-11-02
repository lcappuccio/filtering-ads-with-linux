package org.systemexception.adtrap.logarchiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author leo
 * @date 27/02/16 11:31
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String USER_USER = "user", USER_PASSWORD = "user_pwd", USER_ROLE = "USER";
	public static final String ADMIN_USER = "admin", ADMIN_PASSWORD = "admin_pwd", ADMIN_ROLE = "ADMIN";

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser(ADMIN_USER).password(ADMIN_PASSWORD).roles(ADMIN_ROLE);
		auth.inMemoryAuthentication().withUser(USER_USER).password(USER_PASSWORD).roles(USER_ROLE);
	}

	@Override
	public void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeRequests().antMatchers("/h2-console").hasRole(ADMIN_ROLE).and().httpBasic();
		httpSecurity.authorizeRequests().antMatchers(HttpMethod.DELETE).hasRole(ADMIN_ROLE).and().httpBasic();
		httpSecurity.authorizeRequests().antMatchers(HttpMethod.POST).hasRole(ADMIN_ROLE).and().httpBasic();
		httpSecurity.authorizeRequests().antMatchers(HttpMethod.PUT).hasRole(ADMIN_ROLE).and().httpBasic();
		httpSecurity.csrf().disable().headers().frameOptions().sameOrigin();
	}
}
