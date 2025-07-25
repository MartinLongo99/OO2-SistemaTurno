package com.oo2.grupo15.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

@Schema(description = "DTO para respuesta de login exitoso")
public record LoginResponseDTO(
    
    @Schema(description = "ID del usuario", example = "1")
    Long id,
    
    @Schema(description = "Email del usuario", example = "usuario@ejemplo.com")
    String email,
    
    @Schema(description = "Mensaje de éxito", example = "Login exitoso")
    String message,
    
    @Schema(description = "Roles del usuario", example = "[\"USER\", \"ADMIN\"]")
    Set<String> roles,
    
    @Schema(description = "Información de contacto del usuario")
    ContactoDTO contacto
    
) {
}