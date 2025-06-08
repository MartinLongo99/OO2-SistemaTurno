package com.oo2.grupo15.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioDTO {

	private Long id;
	
	private String email;
	
	private String password;
	
	private ContactoDTO contacto;

	public UsuarioDTO(Long id, String email, String password, ContactoDTO contacto) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.contacto = contacto;
	}
	
	
}