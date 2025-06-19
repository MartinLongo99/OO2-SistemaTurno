package com.oo2.grupo15.services;

import java.util.List;
import java.util.Optional;

import com.oo2.grupo15.dtos.UsuarioDTO;

public interface IUsuarioService {

	UsuarioDTO crearUsuario(UsuarioDTO dto);
	UsuarioDTO actualizarUsuario(Long id, UsuarioDTO dto);
	List<UsuarioDTO> obtenerTodos();
	UsuarioDTO obtenerPorId(Long id);
	UsuarioDTO buscarPorEmail(String email);
	UsuarioDTO guardar(UsuarioDTO usuarioDTO);
	void eliminarUsuario(Long id);
	List<UsuarioDTO> getAll();
	Optional<UsuarioDTO> findByContactoDni(long dni);
	
}
