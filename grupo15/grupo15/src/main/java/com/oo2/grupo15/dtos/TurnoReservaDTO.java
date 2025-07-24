package com.oo2.grupo15.dtos;

import java.time.LocalDateTime;

public record TurnoReservaDTO(
	    LocalDateTime fechaHora,
	    Long servicioLugarId,
	    String nombreSolicitante,
	    String apellidoSolicitante,
	    long dniSolicitante,
	    String emailSolicitante,
	    String telefonoSolicitante
	) {

}
