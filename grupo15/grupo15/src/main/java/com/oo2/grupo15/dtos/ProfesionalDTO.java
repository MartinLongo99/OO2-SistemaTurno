package com.oo2.grupo15.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfesionalDTO {
    private Long id;
    private String email;
    private String matricula;
    private ContactoDTO contacto;
    private List<String> especialidades;
}
