package com.oo2.grupo15.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DireccionDTO {
	private int id;

	private String calleYAltura;

	private LocalidadDTO localidad;

	public DireccionDTO(int id, String calleYAltura, LocalidadDTO localidad) {
		this.id = id;
		this.calleYAltura = calleYAltura;
		this.localidad = localidad;
	}
}
