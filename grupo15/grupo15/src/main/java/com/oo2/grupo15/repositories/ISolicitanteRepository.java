package com.oo2.grupo15.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo15.entities.Solicitante;

@Repository
public interface ISolicitanteRepository extends JpaRepository<Solicitante, Long> {
    // Modificamos el método para buscar a través de la relación con contacto
    Solicitante findByContactoDni(long dni);
}