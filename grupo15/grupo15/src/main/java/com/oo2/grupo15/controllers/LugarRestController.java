package com.oo2.grupo15.controllers;

import com.oo2.grupo15.dtos.LugarDTO;
import com.oo2.grupo15.services.ILugarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lugares")
@Tag(name = "API de Lugares", description = "Operaciones de la API para la gestión de lugares")
public class LugarRestController {

    @Autowired
    private ILugarService lugarService;

    @Operation(summary = "Listar todos los lugares")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de lugares recuperada exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<LugarDTO>> getAllLugares() {
        return ResponseEntity.ok(lugarService.getAll());
    }

    @Operation(summary = "Obtener un lugar por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lugar encontrado"),
            @ApiResponse(responseCode = "404", description = "Lugar no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LugarDTO> getLugarById(@PathVariable int id) {
        LugarDTO lugar = lugarService.getById(id);
        if (lugar == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(lugar);
    }

    @Operation(summary = "Crear un lugar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lugar creado exitosamente")
    })
    @PostMapping
    public ResponseEntity<LugarDTO> saveLugar(@RequestBody LugarDTO dto) {
        try {
            LugarDTO savedLugar = lugarService.save(dto);
            return new ResponseEntity<>(savedLugar, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Actualizar un lugar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lugar actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Lugar no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<LugarDTO> updateLugar(@PathVariable int id, @RequestBody LugarDTO dto) {
        LugarDTO updatedLugar = lugarService.update(id, dto);
        if (updatedLugar == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(updatedLugar);
    }

    @Operation(summary = "Eliminar un lugar por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lugar eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Lugar no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLugar(@PathVariable int id) {
        if (lugarService.delete(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
