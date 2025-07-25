package com.oo2.grupo15.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para manejo de usuarios del sistema")
public record UsuarioDTO(
    
    @Schema(description = "ID único del usuario", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    Long id,
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    @Schema(description = "Email del usuario", example = "usuario@ejemplo.com", required = true)
    String email,
    
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Schema(description = "Contraseña del usuario", example = "miPassword123", 
            minLength = 6, accessMode = Schema.AccessMode.WRITE_ONLY)
    String password,
    
    @Valid
    @Schema(description = "Información de contacto del usuario")
    ContactoDTO contacto
    
) {
}