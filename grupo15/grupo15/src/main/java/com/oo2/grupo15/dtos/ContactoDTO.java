package com.oo2.grupo15.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para información de contacto del usuario")
public record ContactoDTO(
    
    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre del usuario", example = "Juan", required = true)
    String nombre,
    
    @NotBlank(message = "El apellido es obligatorio")
    @Schema(description = "Apellido del usuario", example = "Pérez", required = true)
    String apellido,
    
    @Positive(message = "El DNI debe ser un número positivo")
    @Schema(description = "Documento Nacional de Identidad", example = "12345678", required = true)
    long dni,
    
    @Schema(description = "Dirección completa (calle y altura)", example = "Av. Corrientes 1234")
    String calleYAltura,
    
    @Schema(description = "Localidad de residencia", example = "Capital Federal")
    String localidad,
    
    @Schema(description = "Provincia de residencia", example = "Buenos Aires")
    String provincia
    
) {
}