package com.oo2.grupo15.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.oo2.grupo15.entities.Servicio;

public interface IServicioRepository extends JpaRepository<Servicio, Long> {
}
