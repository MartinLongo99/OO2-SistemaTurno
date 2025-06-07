package com.oo2.grupo15.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.oo2.grupo15.dtos.UsuarioDTO;
import com.oo2.grupo15.services.IUsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@PostMapping
	public ResponseEntity<UsuarioDTO> crearUsuario(@RequestBody UsuarioDTO usuarioDTO){
		UsuarioDTO nuevo = usuarioService.crearUsuario(usuarioDTO);
		return ResponseEntity.ok(nuevo);
	}
	
	@GetMapping
	public ResponseEntity<List<UsuarioDTO>> obtenerTodos(){
		return ResponseEntity.ok(usuarioService.obtenerTodos());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioDTO> obtenerPorId(@PathVariable Long id){
		return ResponseEntity.ok(usuarioService.obtenerPorId(id));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<UsuarioDTO> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO){
		return ResponseEntity.ok(usuarioService.actualizarUsuario(id, usuarioDTO));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id){
		usuarioService.eliminarUsuario(id);
		return ResponseEntity.noContent().build();
	}
}