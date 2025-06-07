package com.oo2.grupo15.entities;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor

public class Solicitante extends Usuario {
    private boolean pago;

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }
}
