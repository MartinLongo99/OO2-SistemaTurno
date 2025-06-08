package com.oo2.grupo15.services.implementation;

import com.oo2.grupo15.dtos.UsuarioDTO;
import com.oo2.grupo15.entities.Usuario;
import com.oo2.grupo15.entities.Contacto;
import com.oo2.grupo15.repositories.IUsuarioRepository;
import com.oo2.grupo15.services.IUsuarioService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements IUsuarioService {

	@Autowired
	private IUsuarioRepository usuarioRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public UsuarioDTO crearUsuario(UsuarioDTO dto) {
		Usuario usuario = modelMapper.map(dto, Usuario.class);
		
		Contacto contacto = new Contacto();
		contacto.setNombre(dto.getNombre());
		usuario.setContacto(contacto);
		
		Usuario guardado = usuarioRepository.save(usuario);
		
		UsuarioDTO resultado = modelMapper.map(guardado, UsuarioDTO.class);
        resultado.setNombre(guardado.getContacto() != null ? guardado.getContacto().getNombre() : null);
        
        return resultado;
	}
		
	@Override
	public List<UsuarioDTO> obtenerTodos(){
		return usuarioRepository.findAll()
				.stream()
				.map(usuario -> {
					UsuarioDTO dto = modelMapper.map(usuario, UsuarioDTO.class);
					dto.setNombre(usuario.getContacto() != null ? usuario.getContacto().getNombre() : null);
                    return dto;
				})
				.collect(Collectors.toList());
				
	}
	
	@Override
	public UsuarioDTO obtenerPorId(Long id) {
		 Usuario usuario = usuarioRepository.findById(id)
	                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

	     UsuarioDTO dto = modelMapper.map(usuario, UsuarioDTO.class);
	     dto.setNombre(usuario.getContacto() != null ? usuario.getContacto().getNombre() : null);
	     return dto;
	
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
		
		 UsuarioDTO resultado = modelMapper.map(actualizado, UsuarioDTO.class);
	     resultado.setNombre(actualizado.getContacto() != null ? actualizado.getContacto().getNombre() : null);
	     return resultado;
	}
	
	@Override
	public void eliminarUsuario(Long id) {
		usuarioRepository.deleteById(id);
	}
	
	@Override
	public UsuarioDTO buscarPorEmail(String email) {
	    Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
	    return (usuario != null) ? modelMapper.map(usuario, UsuarioDTO.class) : null;
	}
	
	@Override
	public UsuarioDTO guardar(UsuarioDTO usuarioDTO) {
	    Usuario entidad = modelMapper.map(usuarioDTO, Usuario.class);
	    Usuario guardado = usuarioRepository.save(entidad);
	    return modelMapper.map(guardado, UsuarioDTO.class);
	}

	
}
