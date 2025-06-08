package com.oo2.grupo15.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @NoArgsConstructor
@Builder
public class LugarDTO {
	private int id;
	private String nombre;
	private DireccionDTO direccion;
	
	
	public LugarDTO(int id, String nombre, DireccionDTO direccion) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.direccion = direccion;
	}
	
	
}
