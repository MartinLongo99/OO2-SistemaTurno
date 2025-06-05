package com.oo2.grupo15.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServicioDTO {
    private Long id;
    private String nombre;
    private Integer duracionMinutos;
    private boolean estado;
    private LocalTime horarioInicio;
    private LocalTime horarioFin;
}
