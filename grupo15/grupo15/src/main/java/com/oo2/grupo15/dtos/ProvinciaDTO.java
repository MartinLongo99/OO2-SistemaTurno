package com.oo2.grupo15.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @NoArgsConstructor
public class ProvinciaDTO {
	private Integer id;
	private String nombre;
	
	
	public ProvinciaDTO(Integer id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}
	
	
}
