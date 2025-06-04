package com.oo2.grupo15.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.oo2.grupo15.dtos.TurnoDTO;
import com.oo2.grupo15.services.ITurnoService;

@RestController
@RequestMapping("/turnos")
public class TurnoController {

    @Autowired
    private ITurnoService turnoService;

    @PostMapping
    public ResponseEntity<TurnoDTO> crear(@RequestBody TurnoDTO dto) {
        return ResponseEntity.ok(turnoService.crearTurno(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TurnoDTO> actualizar(@PathVariable Long id, @RequestBody TurnoDTO dto) {
        return ResponseEntity.ok(turnoService.actualizarTurno(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        turnoService.eliminarTurno(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TurnoDTO>> listar() {
        return ResponseEntity.ok(turnoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurnoDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(turnoService.obtenerPorId(id));
    }
}
