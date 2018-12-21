package com.imaquio.basejw.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imaquio.basejw.dao.IUsuarioDao;
import com.imaquio.basejw.entity.Usuario;

@Service
public class UsuarioService implements UserDetailsService{
	/*
	 * Implementa una interfaz de spring security 'UserDetailsService' 
	 * es una interfaz propia para trabajar con JPA o cualquier tipo de proveedor
	 * para implementar el proceso de login. 
	 * 
	 * Se debe implementar el método 'loadUserByUsername' y colocar anotación @Transactional(se debe importar de spring y no de javx)
	 * 
	 * */
	
	private Logger logger = LoggerFactory.getLogger(UsuarioService.class);
	
	@Autowired
	private IUsuarioDao usuarioDao;
	
	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		
		Usuario usuario = usuarioDao.findByUsername(username);
		
		if(usuario == null) {
			logger.error("Error en el login: no existe el usuario '" + username +"' en el sistema");
			throw new UsernameNotFoundException("Error en el login: no existe el usuario '" + username +"' en el sistema");
		}
		
		/*
		 * Se obtienen los Roles del usuario.
		 * Se debe convertir el tipo de role a tipo GrantedAuthority, para esto se puede usar stream.
		 * como es una lista, se puede convertir en un stream, donde se obtiene los elementos de este flujo para luego convertirlo
		 * al tipo de dato GrantedAuthority.
		 * Utilizamos el método map para convertir los datos del flujo utilizando la implementación SimpleGrantedAuthority
		 * Se utiliza peek para mostrar cada flujo por consola
		 * Como sigue siendo un stream, hay que convertirlo a una Lista. Esto se hace con collect
		 * 
		 * */
		List<GrantedAuthority> authorities = usuario.getRoles()
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getNombre()))
				.peek(authority -> logger.info("Role: " + authority.getAuthority()))
				.collect(Collectors.toList());
		
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
	}

}
