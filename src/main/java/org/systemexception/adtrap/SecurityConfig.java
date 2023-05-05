package org.systemexception.adtrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author leo
 * @date 23/11/2016 22:37
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	public static final String USER_ROLE = "USER";

	@Value("${adtrap.user}")
	private String user;

	@Value("${adtrap.password}")
	private String password;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
				.antMatchers("/css**").permitAll()
				.antMatchers("/health").permitAll()
				.antMatchers("/images/**").permitAll()
				.antMatchers("/metrics").permitAll()
				.antMatchers("/**").hasRole(USER_ROLE)
				.and().formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/clientlist", true)
				.and().logout().permitAll();
		http.authorizeRequests().antMatchers(Application.CONTEXT, HttpMethod.GET.toString()).hasRole(USER_ROLE)
				.and().httpBasic();
		http.authorizeRequests().antMatchers(Application.CONTEXT, HttpMethod.POST.toString()).hasRole(USER_ROLE)
				.and().httpBasic();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser(user).password(password).roles(USER_ROLE);
	}
}
