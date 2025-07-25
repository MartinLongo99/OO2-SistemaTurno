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
@Tag(name = "API de Lugares", description = "Operaciones para gestión de lugares")
public class LugarRestController {

    @Autowired
    private ILugarService lugarService;

    @Operation(summary = "Listar todos los lugares")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de lugares obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<LugarDTO>> getAllLugares() {
        List<LugarDTO> lugares = lugarService.getAll();
        return ResponseEntity.ok(lugares);
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

    @Operation(summary = "Crear un nuevo lugar")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Lugar creado exitosamente"),
        @ApiResponse(responseCode = "409", description = "Lugar duplicado")
    })
    @PostMapping
    public ResponseEntity<?> createLugar(@RequestBody LugarDTO dto) {
        if (lugarService.existeLugarDuplicado(dto)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya existe un lugar con ese nombre, calle y localidad.");
        }
        LugarDTO lugarCreado = lugarService.save(dto);
        return new ResponseEntity<>(lugarCreado, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un lugar existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lugar actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Lugar no encontrado"),
        @ApiResponse(responseCode = "409", description = "Lugar duplicado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLugar(@PathVariable int id, @RequestBody LugarDTO dto) {
        LugarDTO lugarExistente = lugarService.getById(id);
        if (lugarExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        // Para evitar que se duplique otro lugar al actualizar
        if (lugarService.existeLugarDuplicado(dto) && !idEquals(dto.id(), id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya existe un lugar con ese nombre, calle y localidad.");
        }
        // Aseguramos que el id que llega en dto sea el correcto para no crear otro lugar
        LugarDTO lugarAActualizar = new LugarDTO(id, dto.nombre(), dto.direccion());
        LugarDTO lugarActualizado = lugarService.save(lugarAActualizar);
        return ResponseEntity.ok(lugarActualizado);
    }

    private boolean idEquals(Integer dtoId, int pathId) {
        return dtoId != null && dtoId == pathId;
    }

    @Operation(summary = "Eliminar un lugar por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lugar eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Lugar no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLugar(@PathVariable int id) {
        LugarDTO lugarExistente = lugarService.getById(id);
        if (lugarExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        boolean eliminado = lugarService.delete(id);
        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();
    }
}
