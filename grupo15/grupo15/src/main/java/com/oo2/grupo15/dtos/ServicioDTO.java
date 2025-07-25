package com.oo2.grupo15.dtos;

import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.constraints.*;

public record ServicioDTO(
        Long id,
        @NotBlank String nombre,
        @NotNull @Min(1) Integer duracionMinutos,
        boolean estado,
        @NotBlank String horarioInicio,
        @NotBlank String horarioFin,
        Set<DayOfWeek> diasSemana
)  {
    public String getDiasSemanaFormateados() {
        if (diasSemana == null || diasSemana.isEmpty()) {
            return "N/A";
        }
        List<DayOfWeek> sortedDays = diasSemana.stream()
                .sorted(Comparator.comparing(DayOfWeek::getValue))
                .collect(Collectors.toList());

        return sortedDays.stream()
                .map(this::convertirDiaAES)
                .collect(Collectors.joining(", "));
    }

    private String convertirDiaAES(DayOfWeek dia) {
        return switch (dia) {
            case MONDAY -> "Lunes";
            case TUESDAY -> "Martes";
            case WEDNESDAY -> "Miércoles";
            case THURSDAY -> "Jueves";
            case FRIDAY -> "Viernes";
            case SATURDAY -> "Sábado";
            case SUNDAY -> "Domingo";
            default -> dia.toString();
        };
    }
}