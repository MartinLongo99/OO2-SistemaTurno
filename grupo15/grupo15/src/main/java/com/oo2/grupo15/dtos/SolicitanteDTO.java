package com.oo2.grupo15.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitanteDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private long dni;
    private String email;
    private String telefono;
}