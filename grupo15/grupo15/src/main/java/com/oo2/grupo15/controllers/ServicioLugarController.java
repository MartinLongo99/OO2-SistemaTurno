package com.oo2.grupo15.controllers;

import com.oo2.grupo15.dtos.ServicioLugarDTO;
import com.oo2.grupo15.services.IServicioLugarService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/serviciolugar/api")
public class ServicioLugarController {

    @Autowired
    private IServicioLugarService servicioLugarService;

    @GetMapping
    public List<ServicioLugarDTO> getAll() {
        return servicioLugarService.getAll();
    }

    @GetMapping("/{id}")
    public ServicioLugarDTO getById(@PathVariable Long id) {
        return servicioLugarService.getById(id);
    }

    @PostMapping
    public ServicioLugarDTO save(@RequestBody ServicioLugarDTO dto) {
        return servicioLugarService.save(dto);
    }

    @PutMapping("/{id}")
    public ServicioLugarDTO update(@PathVariable Long id, @RequestBody ServicioLugarDTO dto) {
        return servicioLugarService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        servicioLugarService.delete(id);
    }
}
