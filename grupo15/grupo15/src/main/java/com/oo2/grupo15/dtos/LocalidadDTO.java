package com.oo2.grupo15.dtos;

import com.oo2.grupo15.entities.Provincia;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class LocalidadDTO {

    private int id;

    private String nombre;
    
    private Provincia provincia;

    public LocalidadDTO(int id, String nombre, Provincia provincia) {
        this.setId(id);
        this.nombre = nombre;
        this.provincia = provincia;
    }
}