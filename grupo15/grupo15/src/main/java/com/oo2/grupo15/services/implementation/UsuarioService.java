package com.oo2.grupo15.services.implementation;

import com.oo2.grupo15.dtos.UsuarioDTO;
import com.oo2.grupo15.entities.Contacto;
import com.oo2.grupo15.entities.Usuario;
import com.oo2.grupo15.exceptions.DNIExistenteException;
import com.oo2.grupo15.repositories.IUsuarioRepository;
import com.oo2.grupo15.services.IUsuarioService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements IUsuarioService, UserDetailsService {

	@Autowired
	private IUsuarioRepository usuarioRepository;

	@Autowired
	private ModelMapper modelMapper;

	// Implementación del método UserDetailsService
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

		Collection<GrantedAuthority> authorities = usuario.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
				.collect(Collectors.toList());

		return new User(usuario.getEmail(), usuario.getPassword(), authorities);
	}

	@Override
	public UsuarioDTO crearUsuario(UsuarioDTO dto) {
		Usuario usuario = modelMapper.map(dto, Usuario.class);

		Contacto contacto = new Contacto();
		contacto.setNombre(dto.getContacto() != null ? dto.getContacto().getNombre() : null);
		contacto.setApellido(dto.getContacto() != null ? dto.getContacto().getApellido() : null);
		contacto.setDni(dto.getContacto() != null ? dto.getContacto().getDni() : 0);
		usuario.setContacto(contacto);

		Usuario guardado = usuarioRepository.save(usuario);

		UsuarioDTO resultado = modelMapper.map(guardado, UsuarioDTO.class);

		return resultado;
	}

	@Override
	public List<UsuarioDTO> obtenerTodos(){
		return usuarioRepository.findAll()
				.stream()
				.map(usuario -> {
					UsuarioDTO dto = modelMapper.map(usuario, UsuarioDTO.class);
					return dto;
				})
				.collect(Collectors.toList());

	}

	@Override
	public UsuarioDTO obtenerPorId(Long id) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

		UsuarioDTO dto = modelMapper.map(usuario, UsuarioDTO.class);
		return dto;

	}

	@Override
	public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO dto) {
		Usuario usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        if (dto.getContacto() != null && dto.getContacto().getDni() != 0) { 
            Optional<Usuario> usuarioConMismoDni = usuarioRepository.findByContactoDni(dto.getContacto().getDni());

            if (usuarioConMismoDni.isPresent() && !usuarioConMismoDni.get().getId().equals(id)) {
                
                throw new DNIExistenteException("El DNI " + dto.getContacto().getDni() + " ya está registrado para otro usuario.");
            }
        }
		usuario.setEmail(dto.getEmail());
		usuario.setPassword(dto.getPassword());

		if(usuario.getContacto() == null){
			usuario.setContacto(new Contacto());
		}

		usuario.getContacto().setNombre(dto.getContacto() != null ? dto.getContacto().getNombre() : null);
		usuario.getContacto().setApellido(dto.getContacto() != null ? dto.getContacto().getApellido() : null);

		Usuario actualizado = usuarioRepository.save(usuario);

		UsuarioDTO resultado = modelMapper.map(actualizado, UsuarioDTO.class);
		return resultado;
	}

	@Override
	public void eliminarUsuario(Long id) {
		usuarioRepository.deleteById(id);
	}

	@Override
	public UsuarioDTO buscarPorEmail(String email) {
		Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

		if (usuario.isPresent()) {
			return modelMapper.map(usuario.get(), UsuarioDTO.class);
		}

		return null;
	}


	@Override
	public UsuarioDTO guardar(UsuarioDTO usuarioDTO) {

		Usuario entidad = modelMapper.map(usuarioDTO, Usuario.class);

		Usuario guardado = usuarioRepository.save(entidad);

		return modelMapper.map(guardado, UsuarioDTO.class);
	}


	public List<UsuarioDTO> getAll(){
		return usuarioRepository.findAll()
				.stream()
				.map(usuario -> modelMapper.map(usuario, UsuarioDTO.class))
				.collect(Collectors.toList());
	}

	public Optional<UsuarioDTO> findByContactoDni(long dni){
        Optional<Usuario> usuario = usuarioRepository.findByContactoDni(dni);
        return usuario.map(u -> modelMapper.map(u, UsuarioDTO.class));
    }
}