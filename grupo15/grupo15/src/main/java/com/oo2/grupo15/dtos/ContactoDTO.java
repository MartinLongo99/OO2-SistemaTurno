package com.oo2.grupo15.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactoDTO {
    private String nombre;
    private String apellido;
    private long dni;
    private long telefono;
    private DireccionDTO direccion;
}