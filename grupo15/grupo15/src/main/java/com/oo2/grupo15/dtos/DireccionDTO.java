package com.oo2.grupo15.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DireccionDTO {
    private Integer id;
    private String calleYAltura;
    private LocalidadDTO localidad;

    public DireccionDTO() {
        this.localidad = new LocalidadDTO();
    }

    public DireccionDTO(int id, String calleYAltura, LocalidadDTO localidad) {
        this.id = id;
        this.calleYAltura = calleYAltura;
        this.localidad = localidad;
    }
}
