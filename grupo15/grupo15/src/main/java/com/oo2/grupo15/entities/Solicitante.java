package com.oo2.grupo15.entities;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.Setter;
import lombok.NoArgsConstructor;  // Añadir esta anotación

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor  // Añadir esta anotación
public class Solicitante extends Usuario {
    private boolean pago;
    

}