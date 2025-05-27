package com.oo2.grupo15.repositories;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.oo2.grupo15.entities.Direccion;

@Repository("direccionRepository")
public interface IDireccionRepository extends JpaRepository<Direccion, Serializable>{
    List<Direccion> findByLocalidadIdOrderByCalleYAlturaAsc(int localidadId);

}
