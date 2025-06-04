package com.oo2.grupo15.dtos;

import java.time.LocalDateTime;

import com.oo2.grupo15.entities.ServicioLugar;
import com.oo2.grupo15.entities.Solicitante;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TurnoDTO {
    private Long id;
    private LocalDateTime fechaHora;
    private boolean estado;
    private Long servicioLugarId;
    private Long solicitanteId;
	
    public TurnoDTO(Long id, LocalDateTime fechaHora, boolean estado, Long servicioLugarId, Long solicitanteId) {
		super();
		this.id = id;
		this.fechaHora = fechaHora;
		this.estado = estado;
		this.servicioLugarId = servicioLugarId;
		this.solicitanteId = solicitanteId;
	}
    
    
}
