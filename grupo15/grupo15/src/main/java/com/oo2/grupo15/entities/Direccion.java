package com.oo2.grupo15.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Direccion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String calleYAltura;

	@ManyToOne
	private Localidad localidad;

}
