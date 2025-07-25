package com.oo2.grupo15.dtos;

public record LugarDTO(
    Integer id,
    String nombre,
    DireccionDTO direccion
) {
    public LugarDTO {
        if (direccion == null) {
            direccion = new DireccionDTO();  // inicializamos para evitar nulls
        }
    }

    // Constructor adicional sin id, por si lo necesitas
    public LugarDTO(String nombre, DireccionDTO direccion) {
        this(null, nombre, direccion);
    }
}
