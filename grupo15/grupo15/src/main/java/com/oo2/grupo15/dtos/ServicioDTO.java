package com.oo2.grupo15.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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
    private List<DayOfWeek> diasSemana;

    // Método auxiliar para mostrar el horario completo en texto (por ejemplo "08:00 - 18:00")
    public String getHorarioCompleto() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return horarioInicio.format(formatter) + " - " + horarioFin.format(formatter);
    }

    // Método auxiliar para mostrar los días de la semana como texto (por ejemplo "LUNES, MIERCOLES, VIERNES")
    public String getDiasSemanaTexto() {
        return diasSemana.stream()
                .map(DayOfWeek::name)
                .collect(Collectors.joining(", "));
    }
}
