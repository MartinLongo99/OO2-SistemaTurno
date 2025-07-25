package com.oo2.grupo15.services.implementation;

import com.oo2.grupo15.dtos.ContactoDTO;
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
        Usuario usuario = new Usuario();
        usuario.setEmail(dto.email()); 
        usuario.setPassword(dto.password()); 

        if (dto.contacto() != null && dto.contacto().dni() != 0) { 
            Optional<Usuario> usuarioConMismoDni = usuarioRepository.findByContactoDni(dto.contacto().dni());

            if (usuarioConMismoDni.isPresent()) {
                throw new DNIExistenteException("El DNI " + dto.contacto().dni() + " ya está registrado para otro usuario.");
            }
        }

        Contacto contacto = new Contacto();
        if (dto.contacto() != null) {
            contacto.setNombre(dto.contacto().nombre());
            contacto.setApellido(dto.contacto().apellido());
            contacto.setDni(dto.contacto().dni());
        }
        usuario.setContacto(contacto);

        Usuario guardado = usuarioRepository.save(usuario);

        return convertirUsuarioADTO(guardado);
    }

    @Override
    public List<UsuarioDTO> obtenerTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::convertirUsuarioADTO)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioDTO obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        return convertirUsuarioADTO(usuario);
    }

    @Override
    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        if (dto.contacto() != null && dto.contacto().dni() != 0) {
            Optional<Usuario> usuarioConMismoDni = usuarioRepository.findByContactoDni(dto.contacto().dni());

            if (usuarioConMismoDni.isPresent() && !usuarioConMismoDni.get().getId().equals(id)) {
                throw new DNIExistenteException("El DNI " + dto.contacto().dni() + " ya está registrado para otro usuario.");
            }
        }

        usuario.setEmail(dto.email());
        if (dto.password() != null && !dto.password().isEmpty()) {
            usuario.setPassword(dto.password());
        }

        if (usuario.getContacto() == null) {
            usuario.setContacto(new Contacto());
        }

        if (dto.contacto() != null) {
            usuario.getContacto().setNombre(dto.contacto().nombre());
            usuario.getContacto().setApellido(dto.contacto().apellido());
            usuario.getContacto().setDni(dto.contacto().dni());
        }

        Usuario actualizado = usuarioRepository.save(usuario);

        return convertirUsuarioADTO(actualizado);
    }

    @Override
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public UsuarioDTO buscarPorEmail(String email) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        return usuario.map(this::convertirUsuarioADTO).orElse(null);
    }

    @Override
    public UsuarioDTO guardar(UsuarioDTO usuarioDTO) {
        Usuario entidad = new Usuario();
        entidad.setEmail(usuarioDTO.email());
        entidad.setPassword(usuarioDTO.password());

        if (usuarioDTO.contacto() != null) {
            Contacto contacto = new Contacto();
            contacto.setNombre(usuarioDTO.contacto().nombre());
            contacto.setApellido(usuarioDTO.contacto().apellido());
            contacto.setDni(usuarioDTO.contacto().dni());
            entidad.setContacto(contacto);
        }

        Usuario guardado = usuarioRepository.save(entidad);

        return convertirUsuarioADTO(guardado);
    }

    public List<UsuarioDTO> getAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::convertirUsuarioADTO)
                .collect(Collectors.toList());
    }

    public Optional<UsuarioDTO> findByContactoDni(long dni) {
        Optional<Usuario> usuario = usuarioRepository.findByContactoDni(dni);
        return usuario.map(this::convertirUsuarioADTO);
    }

    private UsuarioDTO convertirUsuarioADTO(Usuario usuario) {
        ContactoDTO contactoDTO = null;
        
        if (usuario.getContacto() != null) {
            Contacto contacto = usuario.getContacto();
            String calleYAltura = null;
            String localidad = null;
            String provincia = null;

            contactoDTO = new ContactoDTO(
                contacto.getNombre(),
                contacto.getApellido(),
                contacto.getDni(),
                calleYAltura,
                localidad,
                provincia
            );
        }

        return new UsuarioDTO(
            usuario.getId(),
            usuario.getEmail(),
            null,
            contactoDTO
        );
    }
}