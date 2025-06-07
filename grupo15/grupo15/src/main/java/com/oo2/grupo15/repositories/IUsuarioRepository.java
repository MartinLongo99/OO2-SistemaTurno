package com.oo2.grupo15.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.oo2.grupo15.entities.Usuario;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long>{
	
	Optional<Usuario> findById(Long id);
	
	Optional<Usuario> findByEmail(String email);
	
	@Query(value = "from Usuario u order by u.id")
	List<Usuario> obtenerTodos();
}
