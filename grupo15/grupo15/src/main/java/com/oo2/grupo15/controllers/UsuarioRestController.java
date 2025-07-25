package com.oo2.grupo15.controllers;

import com.oo2.grupo15.dtos.ApiResponseDTO;
import com.oo2.grupo15.dtos.UsuarioDTO;
import com.oo2.grupo15.exceptions.DNIExistenteException;
import com.oo2.grupo15.services.IUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "API de Usuarios", description = "Operaciones de la API para la gestión de usuarios")
@CrossOrigin(origins = "*")

public class UsuarioRestController {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(
        summary = "Obtener todos los usuarios", 
        description = "Retorna una lista de todos los usuarios registrados en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Lista de usuarios obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
        )
    })
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<UsuarioDTO>>> obtenerTodosLosUsuarios() {
        try {
            List<UsuarioDTO> usuarios = usuarioService.obtenerTodos();
            
            // Limpiar contraseñas por seguridad
            usuarios.forEach(usuario -> usuario.setPassword(null));
            
            return ResponseEntity.ok(
                ApiResponseDTO.success("Usuarios obtenidos exitosamente", usuarios)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseDTO.error("Error al obtener usuarios: " + e.getMessage(), 500));
        }
    }

    @Operation(
        summary = "Obtener usuario por ID", 
        description = "Retorna un usuario específico basado en su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Usuario encontrado",
            content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Usuario no encontrado",
            content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UsuarioDTO>> obtenerUsuarioPorId(
            @Parameter(description = "ID del usuario a buscar", required = true)
            @PathVariable Long id) {
        try {
            UsuarioDTO usuario = usuarioService.obtenerPorId(id);
            
            // Limpiar contraseña por seguridad
            usuario.setPassword(null);
            
            return ResponseEntity.ok(
                ApiResponseDTO.success("Usuario encontrado", usuario)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponseDTO.error("Usuario no encontrado con ID: " + id, 404));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseDTO.error("Error al buscar usuario: " + e.getMessage(), 500));
        }
    }

    @Operation(
        summary = "Buscar usuario por DNI", 
        description = "Retorna un usuario específico basado en su DNI"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Usuario encontrado",
            content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Usuario no encontrado",
            content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
        )
    })
    @GetMapping("/dni/{dni}")
    public ResponseEntity<ApiResponseDTO<UsuarioDTO>> obtenerUsuarioPorDni(
            @Parameter(description = "DNI del usuario a buscar", required = true)
            @PathVariable long dni) {
        try {
            Optional<UsuarioDTO> usuario = usuarioService.findByContactoDni(dni);
            
            if (usuario.isPresent()) {
                UsuarioDTO usuarioDTO = usuario.get();
                usuarioDTO.setPassword(null); // Limpiar contraseña por seguridad
                
                return ResponseEntity.ok(
                    ApiResponseDTO.success("Usuario encontrado", usuarioDTO)
                );
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error("Usuario no encontrado con DNI: " + dni, 404));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseDTO.error("Error al buscar usuario: " + e.getMessage(), 500));
        }
    }

    @Operation(
        summary = "Crear nuevo usuario", 
        description = "Crea un nuevo usuario en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Usuario creado exitosamente",
            content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos de entrada inválidos o DNI ya existe",
            content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
        )
    })
    @PostMapping
    public ResponseEntity<ApiResponseDTO<UsuarioDTO>> crearUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            // Verificar si el email ya existe
            UsuarioDTO existente = usuarioService.buscarPorEmail(usuarioDTO.getEmail());
            if (existente != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error("El email ya está registrado", 400));
            }

            // Encriptar contraseña
            String passwordEncriptada = passwordEncoder.encode(usuarioDTO.getPassword());
            usuarioDTO.setPassword(passwordEncriptada);

            // Crear usuario
            UsuarioDTO usuarioCreado = usuarioService.crearUsuario(usuarioDTO);
            
            // Limpiar contraseña en la respuesta
            usuarioCreado.setPassword(null);

            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Usuario creado exitosamente", usuarioCreado));

        } catch (DNIExistenteException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDTO.error(e.getMessage(), 400));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseDTO.error("Error al crear usuario: " + e.getMessage(), 500));
        }
    }

    @Operation(
        summary = "Actualizar usuario", 
        description = "Actualiza un usuario existente en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Usuario actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Usuario no encontrado",
            content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos de entrada inválidos o DNI ya existe",
            content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UsuarioDTO>> actualizarUsuario(
            @Parameter(description = "ID del usuario a actualizar", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            // Si se proporciona una nueva contraseña, encriptarla
            if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
                String passwordEncriptada = passwordEncoder.encode(usuarioDTO.getPassword());
                usuarioDTO.setPassword(passwordEncriptada);
            }

            UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioDTO);
            
            // Limpiar contraseña en la respuesta
            usuarioActualizado.setPassword(null);

            return ResponseEntity.ok(
                ApiResponseDTO.success("Usuario actualizado exitosamente", usuarioActualizado)
            );

        } catch (RuntimeException e) {
            if (e.getMessage().contains("Usuario no encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseDTO.error(e.getMessage(), 404));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDTO.error(e.getMessage(), 400));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseDTO.error("Error al actualizar usuario: " + e.getMessage(), 500));
        }
    }

    @Operation(
        summary = "Eliminar usuario", 
        description = "Elimina un usuario del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Usuario eliminado exitosamente",
            content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Usuario no encontrado",
            content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<String>> eliminarUsuario(
            @Parameter(description = "ID del usuario a eliminar", required = true)
            @PathVariable Long id) {
        try {
            // Verificar que el usuario existe antes de eliminarlo
            usuarioService.obtenerPorId(id);
            
            usuarioService.eliminarUsuario(id);

            return ResponseEntity.ok(
                ApiResponseDTO.success("Usuario eliminado exitosamente", "Usuario con ID " + id + " eliminado")
            );

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponseDTO.error("Usuario no encontrado con ID: " + id, 404));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseDTO.error("Error al eliminar usuario: " + e.getMessage(), 500));
        }
    }
}