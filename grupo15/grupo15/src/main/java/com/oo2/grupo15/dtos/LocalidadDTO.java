package com.oo2.grupo15.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class LocalidadDTO {

    private int id;

    private String nombre;

    public LocalidadDTO(int id, String nombre) {
        this.setId(id);
        this.nombre = nombre;
    }
}