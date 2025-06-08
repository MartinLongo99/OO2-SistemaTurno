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
    private Localidad localidad;

    @ManyToOne
    private Provincia provincia;
}
