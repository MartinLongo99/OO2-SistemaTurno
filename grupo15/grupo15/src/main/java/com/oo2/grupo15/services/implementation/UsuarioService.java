package com.oo2.grupo15.services.implementation;

import com.oo2.grupo15.dtos.UsuarioDTO;
import com.oo2.grupo15.entities.Usuario;
import com.oo2.grupo15.entities.Contacto;
import com.oo2.grupo15.repositories.IUsuarioRepository;
import com.oo2.grupo15.services.IUsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements IUsuarioService {

	@Autowired
	private IUsuarioRepository usuarioRepository;
	
	@Override
	public UsuarioDTO crearUsuario(UsuarioDTO dto) {
		Usuario usuario = new Usuario();
		usuario.setEmail(dto.getEmail());
		usuario.setPassword(dto.getPassword());
		
		Contacto contacto = new Contacto();
		contacto.setNombre(dto.getNombre());
		usuario.setContacto(contacto);
		
		Usuario guardado = usuarioRepository.save(usuario);
		
		return new UsuarioDTO(
			guardado.getId(),
			guardado.getContacto() != null ? guardado.getContacto().getNombre() : null,
			guardado.getEmail(),
			guardado.getPassword(),
			false
		);
	}
		
	@Override
	public List<UsuarioDTO> obtenerTodos(){
		return usuarioRepository.findAll()
				.stream()
				.map(usuario -> new UsuarioDTO(
						usuario.getId(),
						usuario.getContacto() != null ? usuario.getContacto().getNombre() : null,
						usuario.getEmail(),
						usuario.getPassword(),
						false
				))
				.collect(Collectors.toList());
				
	}
	
	@Override
	public UsuarioDTO obtenerPorId(Long id) {
		Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
		if(usuarioOpt.isEmpty()) {
			throw new RuntimeException("Usuario no encontrado con ID: " + id);
		}
		
		Usuario usuario = usuarioOpt.get();
		
		return new UsuarioDTO(
				usuario.getId(),
				usuario.getContacto() != null ? usuario.getContacto().getNombre() : null,
				usuario.getEmail(),
				usuario.getPassword(),
				false
		);
	
	}	
	
	@Override
	public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO dto) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
		
		usuario.setEmail(dto.getEmail());
		usuario.setPassword(dto.getPassword());
		
		if(usuario.getContacto() == null){
			usuario.setContacto(new Contacto());
		}
		
		usuario.getContacto().setNombre(dto.getNombre());
		
		Usuario actualizado = usuarioRepository.save(usuario);
		
		return new UsuarioDTO(
			actualizado.getId(),
			actualizado.getContacto() != null ? actualizado.getContacto().getNombre() : null,
			actualizado.getEmail(),
			actualizado.getPassword(),
			false
		);	
	}
	
	@Override
	public void eliminarUsuario(Long id) {
		usuarioRepository.deleteById(id);
	}
	
	
}
