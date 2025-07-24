package com.oo2.grupo15.dtos;

public record SolicitanteDTO(
    Long id,
    String nombre,
    String apellido,
    long dni,
    String email,
    String telefono
) {
}
