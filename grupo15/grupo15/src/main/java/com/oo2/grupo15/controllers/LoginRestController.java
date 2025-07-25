package com.oo2.grupo15.controllers;

import com.oo2.grupo15.dtos.ApiResponseDTO;
import com.oo2.grupo15.dtos.LoginRequestDTO;
import com.oo2.grupo15.dtos.LoginResponseDTO;
import com.oo2.grupo15.dtos.UsuarioDTO;
import com.oo2.grupo15.services.IUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
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

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "API de Login", description = "Operaciones de la API para la gestión de login")
@CrossOrigin(origins = "*")
public class LoginRestController {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(
        summary = "Iniciar sesión", 
        description = "Autentica un usuario con email y contraseña"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Login exitoso",
            content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Credenciales inválidas",
            content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos de entrada inválidos",
            content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
        )
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            // Buscar usuario por email
            UsuarioDTO usuarioDTO = usuarioService.buscarPorEmail(loginRequest.getEmail());
            
            if (usuarioDTO == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseDTO.error("Credenciales inválidas", 401));
            }

            // Verificar contraseña
            if (!passwordEncoder.matches(loginRequest.getPassword(), usuarioDTO.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseDTO.error("Credenciales inválidas", 401));
            }

            // Crear respuesta de login exitoso
            LoginResponseDTO loginResponse = new LoginResponseDTO();
            loginResponse.setId(usuarioDTO.getId());
            loginResponse.setEmail(usuarioDTO.getEmail());
            loginResponse.setMessage("Login exitoso");
            loginResponse.setContacto(usuarioDTO.getContacto());
            
            // Nota: En una implementación real con JWT, aquí generarías el token
            // Por ahora, simulamos roles básicos
            loginResponse.setRoles(Set.of("USER"));

            return ResponseEntity.ok(
                ApiResponseDTO.success("Usuario autenticado correctamente", loginResponse)
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseDTO.error("Error interno del servidor: " + e.getMessage(), 500));
        }
    }

    @Operation(
        summary = "Verificar estado de autenticación", 
        description = "Verifica si un usuario está autenticado (endpoint de prueba)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Estado verificado",
            content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
        )
    })
    @GetMapping("/status")
    public ResponseEntity<ApiResponseDTO<String>> checkAuthStatus() {
        return ResponseEntity.ok(
            ApiResponseDTO.success("API de autenticación funcionando", "OK")
        );
    }
}