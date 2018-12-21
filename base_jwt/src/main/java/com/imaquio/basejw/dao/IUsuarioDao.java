package com.imaquio.basejw.dao;

import org.springframework.data.repository.CrudRepository;

import com.imaquio.basejw.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long >{
	
	public Usuario findByUsername(String username);

}
