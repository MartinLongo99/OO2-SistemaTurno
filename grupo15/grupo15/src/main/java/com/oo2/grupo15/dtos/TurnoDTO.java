package com.oo2.grupo15.dtos;

import java.time.LocalDateTime;

public record TurnoDTO(
    Long id,
    LocalDateTime fechaHora,
    Boolean estado,
    Long servicioLugarId,
    Long solicitanteId,
    
    String nombreServicio,
    String nombreLugar
) {
}
