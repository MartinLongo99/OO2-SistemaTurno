package com.oo2.grupo15.dtos;

import com.oo2.grupo15.entities.Provincia;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class LocalidadDTO {

    private Integer id;

    private String nombre;
    
    private ProvinciaDTO provincia;

    public LocalidadDTO(int id, String nombre, ProvinciaDTO provincia) {
        this.id = id;
        this.nombre = nombre;
        this.provincia = provincia;
    }
}