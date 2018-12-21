package com.imaquio.basejw.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService usuarioService;
	
	/*
	 * Se debe registrar,en el AutheticationManagere, el servicio para autenticar
	 * 
	 * */
	
	@Override
	@Autowired // para inyectar 'AuthenticationManagerBuilder' via argumento
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {	
		auth.userDetailsService(usuarioService).passwordEncoder(passwordEncoder());
	}
	
	/*
	 * Se utiliza BCrypt para encriptar el password
	 * */
	
	@Bean // Para registrar el método y poder usarlo en cualquier otra clase con @Autowired
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); 
	}

	@Bean("authenticationManager")
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.anyRequest().authenticated()
		.and()
		.csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // Deshabilita el manejo de Session ya que vamos a usar Token para autenticarnos 
	}

	
	
	

}
