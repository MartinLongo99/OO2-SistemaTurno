package com.oo2.grupo15.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.oo2.grupo15.entities.ServicioLugar;

import java.util.List;

public interface IServicioLugarRepository extends JpaRepository<ServicioLugar, Long> {
    List<ServicioLugar> findByServicioId(Long servicioId);
    void deleteByServicioId(Long servicioId);
}
