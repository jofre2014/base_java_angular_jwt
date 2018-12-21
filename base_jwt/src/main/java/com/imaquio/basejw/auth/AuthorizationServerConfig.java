package com.imaquio.basejw.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{
	/*
	 * Se encarga del proceso de autenticación por parte de OAUTH2.
	 * Se encarga de la creación del token, validarlo, etc
	 * 
	 * */
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	/*
	 * Se necesita el authenticationManager para el proceso de login
	 * */
	@Autowired
	@Qualifier("authenticationManager") // Le indicamos el nombre del @Bean que queremos inyectar
	private AuthenticationManager authenticationManager;

	
	/*
	 * Brinda los permisos para que cualquier persona pueda loguearse
	 * /oauth/token
	 * 
	 * tokenKeyAcces se encarga de generar el token
	 * checktokenAccess se encarga de validar el token
	 * 
	 * */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

		security.tokenKeyAccess("permitAll()")
		.checkTokenAccess("isAuthenticated()");
	}

	/*
	 * Se configuran los clientes (aplicaciones) que van a acceder al APIRest
	 * 
	 * */
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient("angularapp")
		.secret(passwordEncoder.encode("12345"))
		.scopes("read", "write")
		.authorizedGrantTypes("password", "refresh_token")
		.accessTokenValiditySeconds(3600)
		.refreshTokenValiditySeconds(3600);

		super.configure(clients);
	}
	
	/*
	 * Se encarga de todo el proceso de Autenticación y validar el token
	 * 
	 * */

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

		endpoints.authenticationManager(authenticationManager)
		.accessTokenConverter(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();  
		jwtAccessTokenConverter.setSigningKey(JwtConfig.LLAVE_SECRETA);
		return jwtAccessTokenConverter;
	}
	
	
}
