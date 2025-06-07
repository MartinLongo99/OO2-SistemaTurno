package com.oo2.grupo15.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo15.entities.Contacto;

@Repository
public interface IContactoRepository extends JpaRepository<Contacto, Integer> {
    Contacto findByDni(long dni);
}