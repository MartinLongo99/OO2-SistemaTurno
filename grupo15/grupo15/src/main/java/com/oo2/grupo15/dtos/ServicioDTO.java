package com.oo2.grupo15.dtos;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServicioDTO {
    private Long id;
    private String nombre;
    private boolean estado;
    private int duracionMinutos;
    private LocalTime horarioInicio;
    private LocalTime horarioFin;
    private Set<DayOfWeek> diasSemana;
}
