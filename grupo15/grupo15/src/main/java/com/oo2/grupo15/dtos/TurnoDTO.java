package com.oo2.grupo15.dtos;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TurnoDTO {
    private Long id;
    private LocalDateTime fechaHora;
    private boolean estado;
    private Long servicioLugarId;
    private Long solicitanteId;
    
}
