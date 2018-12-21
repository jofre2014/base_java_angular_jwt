package com.imaquio.basejw.auth;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{
	
	/*
	 * Acá se encuentra la Configuración para proteger las rutas (Endpoints)
	 * */
	
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/clientes").permitAll() // Solo permite cundo se hace un GET al endpoint especificado
		//.antMatchers(HttpMethod.GET, "/api/clientes/{id}").hasAnyRole("USER", "ADMIN") // Solo pueden ingresar los que tengan Role Usuario o Admin
		//.antMatchers(HttpMethod.POST, "/api/clientes").hasRole("ADMIN") // Solo pueden ingresar los que tengan Role Admin
		//.antMatchers("/api/clientes/**").hasRole("ADMIN") // indica que todas las rutas luego de /clientes/ requiere permiso ADMIN
		.anyRequest().authenticated() // Siempre se pone al final para todos los endpoints que no hayammos asignado permisos
		.and()
		.cors().configurationSource(corsConfigurationSource());
		
		
		/*
		 * Se dejan solo el permitAll() y el authenticated()
		 * Los otros, se llevan al controlador utilizando anotacion @Secured, previamente habilitando esta configuración
		 * en SpringSecurityConfig con la anotación 'EnableGlobalMethodSecurity'
		 * 
		 * En el RestController, en cada método que queramos manejar la seguridad, colocamos:
		 * 
		 * @Secured({"ROLE_USER","ROLE_ADMIN"})
		 * 
		 * En este caso si se debe colocar el prefijo ROLE_
		 * Para indicar más de un ROLE, se debe colocar entre llaves {}
		 * 
		 * Los métodos publicos que no requieran autenticación, no se coloca la anotación @Secured
		 * 
		 * */
		
	}
	
	
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();  
		config.setAllowedOrigins(Arrays.asList("*")); // con * se indica que para todos los dominios
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowCredentials(true);
		config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
		
		
		// Registrar la configuracion del CORS para todas las rutas
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		
		return source;
		
	}
	
	/*
	 * Se debe configurar un Filtro 
	 * 
	 * */
	
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter(){
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE); // Mientras más bajo es el orden, mayor es la prioridad
		return bean;
	}
	
	
}
