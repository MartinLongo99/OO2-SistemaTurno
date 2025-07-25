package com.oo2.grupo15.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO genérico para respuestas de la API")
public class ApiResponseDTO<T> {
    
    @Schema(description = "Indica si la operación fue exitosa", example = "true")
    private boolean success;
    
    @Schema(description = "Mensaje descriptivo", example = "Operación realizada exitosamente")
    private String message;
    
    @Schema(description = "Datos de la respuesta")
    private T data;
    
    @Schema(description = "Código de error si aplica", example = "404")
    private Integer errorCode;
    
    public static <T> ApiResponseDTO<T> success(String message, T data) {
        return new ApiResponseDTO<>(true, message, data, null);
    }
    
    public static <T> ApiResponseDTO<T> error(String message, Integer errorCode) {
        return new ApiResponseDTO<>(false, message, null, errorCode);
    }
}