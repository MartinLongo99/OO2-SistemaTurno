package com.oo2.grupo15.dtos;

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
    private Integer duracionMinutos;
    private boolean estado;
    private String horarioInicio;
    private String horarioFin;
    private Set<String> diasSemana;  // String para JSON (ej: "MONDAY", "TUESDAY")
}


