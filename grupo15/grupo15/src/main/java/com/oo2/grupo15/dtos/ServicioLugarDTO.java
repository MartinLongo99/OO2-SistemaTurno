package com.oo2.grupo15.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioLugarDTO {
    private Long id;
    private Long servicioId;
    private String servicioNombre;
    private Long lugarId;
    private String lugarNombre;
    private Long profesionalId;
    private String profesionalNombre;
    private boolean activo;
}
