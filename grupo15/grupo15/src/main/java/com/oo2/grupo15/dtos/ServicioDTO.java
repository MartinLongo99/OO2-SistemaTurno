package com.oo2.grupo15.dtos;

import java.time.DayOfWeek;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Comparator;

import java.util.List;

import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioDTO {
    private Long id;
    private String nombre;
    private Integer duracionMinutos;
    private boolean estado;
    private String horarioInicio;
    private String horarioFin;
    private Set<DayOfWeek> diasSemana; 
    private Set<ServicioLugarDTO> servicioLugares;
    
    private List<Long> lugarIds;
    private List<Long> profesionalIds;
	
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
