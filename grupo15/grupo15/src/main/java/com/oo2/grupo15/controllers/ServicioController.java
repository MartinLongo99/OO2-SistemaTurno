package com.oo2.grupo15.controllers;

import com.oo2.grupo15.dtos.ServicioDTO;
import com.oo2.grupo15.services.IServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/servicios")
public class ServicioController {

    @Autowired
    private IServicioService servicioService;

    // Mostrar la vista HTML
    @GetMapping
    public String mostrarVistaServicios() {
        return "servicio/index"; // templates/servicio/index.html
    }

    // API para obtener todos los servicios
    @GetMapping("/api")
    @ResponseBody
    public List<ServicioDTO> getAll() {
        return servicioService.getAll();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<ServicioDTO> getById(@PathVariable Long id) {
        ServicioDTO dto = servicioService.getById(id);
        return (dto != null) ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping("/api")
    @ResponseBody
    public ServicioDTO create(@RequestBody ServicioDTO dto) {
        return servicioService.save(dto);
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<ServicioDTO> update(@PathVariable Long id, @RequestBody ServicioDTO dto) {
        ServicioDTO updated = servicioService.update(id, dto);
        return (updated != null) ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servicioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
