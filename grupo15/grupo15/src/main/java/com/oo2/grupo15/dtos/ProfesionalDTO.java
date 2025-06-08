package com.oo2.grupo15.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfesionalDTO {
    private Long id;
    private String email;
    private String matricula;
    private ContactoDTO contacto;
    
    // Para enviar los nombres
    private List<String> especialidades;

    // (Opcional) si lo necesitás al guardar:
    private List<Integer> especialidadesIds;
}
