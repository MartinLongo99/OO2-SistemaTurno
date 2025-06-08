package com.oo2.grupo15.entities;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private boolean estado;
    private int duracionMinutos;
    private LocalTime horarioInicio;
    private LocalTime horarioFin;

    @ElementCollection(targetClass = DayOfWeek.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "servicio_dias_semana")
    private Set<DayOfWeek> diasSemana = new HashSet<>();

    // Relación con ServicioLugar
    @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ServicioLugar> servicioLugares = new HashSet<>();
}
