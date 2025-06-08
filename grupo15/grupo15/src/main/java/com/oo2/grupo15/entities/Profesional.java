package com.oo2.grupo15.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profesional extends Usuario {

    @ManyToMany(fetch = FetchType.EAGER) // Cambiar a LAZY si haces fetch join en repositorio
    @JoinTable(
        name = "profesional_especialidad",
        joinColumns = @JoinColumn(name = "profesional_id"),
        inverseJoinColumns = @JoinColumn(name = "especialidad_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Especialidad> especialidades = new HashSet<>();

    @Column(nullable = false, unique = true)
    private String matricula;
}
