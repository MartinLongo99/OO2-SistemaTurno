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
    private ContactoDTO contacto;
    private String email; // porque está en Usuario
    private Boolean pago;
}
