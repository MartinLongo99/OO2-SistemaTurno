package com.oo2.grupo15.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para solicitud de login")
public record LoginRequestDTO(
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    @Schema(description = "Email del usuario", example = "usuario@ejemplo.com")
    String email,
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(description = "Contraseña del usuario", example = "miPassword123")
    String password
    
) {
}