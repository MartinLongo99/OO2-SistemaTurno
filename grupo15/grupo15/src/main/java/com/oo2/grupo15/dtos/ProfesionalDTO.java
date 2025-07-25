package com.oo2.grupo15.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO para manejo de profesionales del sistema")
public record ProfesionalDTO(
    
    @Schema(description = "ID único del profesional", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    Long id,
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    @Schema(description = "Email del profesional", example = "doctor@ejemplo.com", required = true)
    String email,
    
    @NotBlank(message = "La matrícula es obligatoria")
    @Schema(description = "Matrícula profesional", example = "MP12345", required = true)
    String matricula,
    
    @Valid
    @Schema(description = "Información de contacto del profesional")
    ContactoDTO contacto,
    
    @Schema(description = "Lista de nombres de especialidades", example = "[\"Cardiología\", \"Medicina General\"]")
    List<String> especialidades,
    
    @Schema(description = "Lista de IDs de especialidades para operaciones de guardado", 
            example = "[1, 2]", accessMode = Schema.AccessMode.WRITE_ONLY)
    List<Integer> especialidadesIds
    
) {
}