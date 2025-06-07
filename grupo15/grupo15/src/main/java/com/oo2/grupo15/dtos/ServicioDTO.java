package com.oo2.grupo15.dtos;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @NoArgsConstructor
public class ServicioDTO {
	private String nombre;
    private boolean estado;
    private int duracionMinutos;
    private LocalTime horarioInicio;
    private LocalTime horarioFin;
    private Set<DayOfWeek> diasSemana = new HashSet<>();
    
	public ServicioDTO(String nombre, boolean estado, int duracionMinutos, LocalTime horarioInicio,
			LocalTime horarioFin, Set<DayOfWeek> diasSemana) {
		super();
		this.nombre = nombre;
		this.estado = estado;
		this.duracionMinutos = duracionMinutos;
		this.horarioInicio = horarioInicio;
		this.horarioFin = horarioFin;
		this.diasSemana = diasSemana;
	}
	
	public String getDiasSemanaFormateados() {
		String resultadoFormateado;
        if (diasSemana == null || diasSemana.isEmpty()) {
        	resultadoFormateado = "N/A";
        }

        List<DayOfWeek> sortedDays = diasSemana.stream()
                                             .sorted(Comparator.comparing(DayOfWeek::getValue))
                                             .collect(Collectors.toList());

        resultadoFormateado = sortedDays.stream()
                         .map(this::convertirDiaAES)
                         .collect(Collectors.joining(", "));
        
        return resultadoFormateado;
    }

    private String convertirDiaAES(DayOfWeek dia) {
        switch (dia) {
            case MONDAY: return "Lunes";
            case TUESDAY: return "Martes";
            case WEDNESDAY: return "Miércoles";
            case THURSDAY: return "Jueves";
            case FRIDAY: return "Viernes";
            case SATURDAY: return "Sábado";
            case SUNDAY: return "Domingo";
            default: return dia.toString(); 
        }
    }
    
    
}
