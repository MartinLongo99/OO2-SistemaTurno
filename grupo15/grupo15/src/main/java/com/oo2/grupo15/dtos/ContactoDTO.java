package com.oo2.grupo15.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactoDTO {
    private String nombre;
    private String apellido;
    private long dni;

    private String calleYAltura;
    private String localidad;
    private String provincia;
}
