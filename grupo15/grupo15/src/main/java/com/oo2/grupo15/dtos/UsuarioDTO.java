package com.oo2.grupo15.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioDTO {

	private Long id;
	
	private String nombre;
	
	private String email;
	
	private String password;
	
	private boolean estaActivo;

	public UsuarioDTO(Long id, String nombre, String email, String password, boolean estaActivo) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.email = email;
		this.password = password;
		this.estaActivo = estaActivo;
	}
	
	
	
}