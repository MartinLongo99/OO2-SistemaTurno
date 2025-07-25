package com.oo2.grupo15.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para respuesta de login exitoso")
public class LoginResponseDTO {
    
    @Schema(description = "ID del usuario", example = "1")
    private Long id;
    
    @Schema(description = "Email del usuario", example = "usuario@ejemplo.com")
    private String email;
    
    @Schema(description = "Mensaje de éxito", example = "Login exitoso")
    private String message;
    
    @Schema(description = "Roles del usuario", example = "[\"USER\", \"ADMIN\"]")
    private Set<String> roles;
    
    @Schema(description = "Información de contacto del usuario")
    private ContactoDTO contacto;
}