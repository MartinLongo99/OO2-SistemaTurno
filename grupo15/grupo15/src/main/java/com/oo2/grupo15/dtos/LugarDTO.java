package com.oo2.grupo15.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LugarDTO {
    private Integer id;
    private String nombre;
    private DireccionDTO direccion;

    public LugarDTO() {
        this.direccion = new DireccionDTO(); // ← ¡IMPORTANTE! Inicializar para que no sea null
    }

    public LugarDTO(int id, String nombre, DireccionDTO direccion) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
    }
}
