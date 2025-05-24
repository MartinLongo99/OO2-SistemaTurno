package com.oo2.grupo15.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType; // Agrega esto si quieres cargarla lazy por defecto
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Localidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;

    // Si la provincia puede ser null, deberías añadir (optional = true)
    // O si no la necesitas en la vista, asegúrate de que no cause problemas si es null
    @ManyToOne(fetch = FetchType.LAZY) // Considera Lazy para evitar cargar la provincia si no se usa
    private Provincia provincia;

}