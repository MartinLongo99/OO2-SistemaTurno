package com.oo2.grupo15.repositories;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.oo2.grupo15.entities.Servicio;

@Repository("servicioRepository")
public interface IServicioRepository extends JpaRepository<Servicio, Serializable>{
}
