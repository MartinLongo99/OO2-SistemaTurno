package com.oo2.grupo15.services;

import java.util.List;
import com.oo2.grupo15.dtos.UsuarioDTO;

public interface IUsuarioService {

	UsuarioDTO crearUsuario(UsuarioDTO dto);
	UsuarioDTO actualizarUsuario(Long id, UsuarioDTO dto);
	void eliminarUsuario(Long id);
	List<UsuarioDTO> obtenerTodos();
	UsuarioDTO obtenerPorId(Long id);
	
}
