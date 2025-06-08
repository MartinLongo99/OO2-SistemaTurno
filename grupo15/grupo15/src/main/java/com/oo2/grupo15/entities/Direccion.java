package com.oo2.grupo15.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String calleYAltura;

    @ManyToOne
    @JoinColumn(name = "localidad_id")
    private Localidad localidad;

    @ManyToOne
    @JoinColumn(name = "provincia_id")
    private Provincia provincia;
}

