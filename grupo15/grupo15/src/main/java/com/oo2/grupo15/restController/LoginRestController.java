package com.oo2.grupo15.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oo2.grupo15.dtos.UsuarioDTO;
import com.oo2.grupo15.dtos.LoginDTO;
import com.oo2.grupo15.dtos.LoginResponseDTO;
import com.oo2.grupo15.services.IUsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;


@RestController
@RequestMapping("/api")
@Tag(name = "Login API", description = "Operaciones de inicio de sesión para la API")
public class LoginRestController {

    @Autowired
    private IUsuarioService usuarioService;

    @PostMapping("/login")
    @Operation(
        summary = "Login de usuario",
        description = "Verifica las credenciales de un usuario y devuelve un mensaje"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Login exitoso",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDTO.class))
    )
    @ApiResponse(
        responseCode = "401",
        description = "Credenciales inválidas",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDTO.class))
    )
    public ResponseEntity<?> login(@RequestBody LoginDTO request) {
        UsuarioDTO usuario = usuarioService.buscarPorEmail(request.getUsername());

        if (usuario != null && usuario.getPassword().equals(request.getPassword())) {
            String nombre = usuario.getContacto() != null ? usuario.getContacto().getNombre() : "Usuario";
            return ResponseEntity.ok(new LoginResponseDTO("Login exitoso: " + nombre));
        }

        return ResponseEntity.status(401).body(new LoginResponseDTO("Credenciales inválidas"));
    } 
}

