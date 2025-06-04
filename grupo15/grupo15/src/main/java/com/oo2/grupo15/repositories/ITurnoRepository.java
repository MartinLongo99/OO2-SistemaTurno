package com.oo2.grupo15.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.oo2.grupo15.entities.Turno;

@Repository
public interface ITurnoRepository extends JpaRepository<Turno, Long> {
    // Agregá métodos custom si los necesitás
}
