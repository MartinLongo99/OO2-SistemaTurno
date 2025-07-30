// src/main/java/com/oo2/grupo15/controllers/TurnoController.java
package com.oo2.grupo15.controllers;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.oo2.grupo15.exceptions.RecursoNoEncontradoException;
import com.oo2.grupo15.dtos.SolicitanteDTO;
import com.oo2.grupo15.dtos.TurnoDTO;
import com.oo2.grupo15.entities.Servicio;
import com.oo2.grupo15.repositories.ITurnoRepository;
import com.oo2.grupo15.services.IServicioService;
import com.oo2.grupo15.services.ITurnoService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/turnos")
public class TurnoController {

    @Autowired
    private ITurnoRepository turnoRepository;
    
    @Autowired
    private IServicioService servicioService;
    
    @Autowired
    private ITurnoService turnoService;

    @GetMapping
    public String index(Model model) {
        // Pasar los servicios al modelo para mostrarlos en el dropdown
        model.addAttribute("servicios", servicioService.findAll());
        return "turno/index";
    }
    
    @GetMapping("/misTurnos")
    public String verMisTurnos(Model model, Principal principal) {
    	try {
            String emailUsuario = principal.getName();
            List<TurnoDTO> turnosDelUsuario = turnoService.obtenerTurnosPorEmailSolicitante(emailUsuario);

            if (turnosDelUsuario == null || turnosDelUsuario.isEmpty()) {
                throw new RecursoNoEncontradoException("No se encontraron turnos para el usuario con email: " + emailUsuario);
            }

            model.addAttribute("turnos", turnosDelUsuario);
            return "turno/mis-turnos";

        } catch (RecursoNoEncontradoException ex) {
            model.addAttribute("mensaje", ex.getMessage());
            return "error/500";
        }
    }
    
    @GetMapping("/reserva")
    public String reservaForm(
            @RequestParam Long servicioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHora,
            Model model) {
        model.addAttribute("servicioId", servicioId);
        model.addAttribute("fechaHora", fechaHora);
        // Crear un DTO vacío para el formulario usando el constructor canónico de Record Class
        // Se pasan valores por defecto (null, false, 0) para los campos.
        model.addAttribute("turno", new TurnoDTO(null, null, false, null, null, null, null));
        model.addAttribute("solicitante", new SolicitanteDTO(null, null, null, 0, null, null));
        return "turno/reserva";
    }
    

    // Endpoint que retorna turnos existentes
    @GetMapping("/disponibilidad")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> verificarDisponibilidad(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        List<Object[]> resultados = turnoRepository.buscarTurnosSimplificados(fechaInicio, fechaFin);
        List<Map<String, Object>> turnos = new ArrayList<>();

        for (Object[] resultado : resultados) {
            Map<String, Object> turno = new HashMap<>();
            turno.put("id", resultado[0]);
            turno.put("fechaHora", resultado[1]);
            turno.put("estado", resultado[2]);

            turnos.add(turno);
        }

        return ResponseEntity.ok(turnos);
    }

    @GetMapping("/disponibles")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> turnosDisponibles(
            @RequestParam Long servicioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        // Agregar logs para depuración
        System.out.println("Buscando turnos para servicioId: " + servicioId + " y fecha: " + fecha);

        // Obtener el servicio seleccionado
        Servicio servicio = servicioService.findEntityById(servicioId);

        System.out.println("Servicio encontrado: " + servicio.getNombre());
        System.out.println("Estado del servicio: " + servicio.isEstado());
        System.out.println("Días habilitados en el servicio: " + servicio.getDiasSemana());

        // Verificamos que el servicio esté activo
        if (!servicio.isEstado()) {
            System.out.println("Servicio inactivo, retornando lista vacía");
            return ResponseEntity.ok(new ArrayList<>());
        }

        // Obtenemos el día de la semana para la fecha seleccionada
        DayOfWeek diaSemana = fecha.getDayOfWeek();
        System.out.println("Día de la semana para la fecha " + fecha + ": " + diaSemana);

        // Verificar si el servicio opera ese día de la semana
        boolean operable = servicio.getDiasSemana() != null && servicio.getDiasSemana().contains(diaSemana);
        System.out.println("¿El servicio opera ese día? " + operable);

        if (!operable) {
            // Devolvemos lista vacía ya que el servicio no opera ese día
            System.out.println("El servicio no opera ese día, retornando lista vacía");
            return ResponseEntity.ok(new ArrayList<>());
        }

        // Establecer el rango horario para ese día según el servicio
        LocalDateTime fechaInicio = LocalDateTime.of(fecha, servicio.getHorarioInicio());
        LocalDateTime fechaFin = LocalDateTime.of(fecha, servicio.getHorarioFin());
        System.out.println("Buscando turnos entre: " + fechaInicio + " y " + fechaFin);

        // Obtener los turnos ya ocupados para ese rango de tiempo
        List<Object[]> turnosOcupados = turnoRepository.buscarTurnosSimplificados(fechaInicio, fechaFin);
        System.out.println("Turnos ocupados encontrados: " + turnosOcupados.size());

        // Generar todos los posibles horarios con intervalos de duración del servicio
        List<Map<String, Object>> turnosDisponibles = new ArrayList<>();
        LocalDateTime horarioActual = fechaInicio;

        while (horarioActual.plusMinutes(servicio.getDuracionMinutos()).isBefore(fechaFin) ||
                horarioActual.plusMinutes(servicio.getDuracionMinutos()).equals(fechaFin)) {
            final LocalDateTime horarioTurno = horarioActual;

            // Verificar si este horario ya está ocupado
            boolean estaOcupado = turnosOcupados.stream()
                    .anyMatch(t -> ((LocalDateTime)t[1]).equals(horarioTurno));

            if (!estaOcupado) {
                Map<String, Object> turnoDisponible = new HashMap<>();
                turnoDisponible.put("fechaHora", horarioTurno);
                turnoDisponible.put("servicioId", servicioId);
                turnosDisponibles.add(turnoDisponible);
                System.out.println("Agregando turno disponible: " + horarioTurno);
            } else {
                System.out.println("Horario ocupado: " + horarioTurno);
            }

            // Avanzar al siguiente horario según la duración del servicio
            horarioActual = horarioActual.plusMinutes(servicio.getDuracionMinutos());
        }

        System.out.println("Total de turnos disponibles generados: " + turnosDisponibles.size());
        return ResponseEntity.ok(turnosDisponibles);
    }
    @PostMapping("/guardar")
    public String guardarTurno(
            @ModelAttribute("turno") TurnoDTO turnoDTO,
            @ModelAttribute("solicitante") SolicitanteDTO solicitanteDTO,
            RedirectAttributes redirectAttributes) {

        // Forzá estado activo (en caso de que venga false por defecto)
        TurnoDTO turnoParaGuardar = new TurnoDTO(
                null,
                turnoDTO.fechaHora(),
                true,
                turnoDTO.servicioLugarId(),
                null,
                null,
                null
        );

        TurnoDTO turnoCreado = turnoService.crearTurnoConDni(turnoParaGuardar, solicitanteDTO);

        redirectAttributes.addFlashAttribute("turnoId", turnoCreado.id());
        redirectAttributes.addFlashAttribute("fechaHora", turnoCreado.fechaHora());
        redirectAttributes.addFlashAttribute("nombreSolicitante", solicitanteDTO.nombre());
        redirectAttributes.addFlashAttribute("apellidoSolicitante", solicitanteDTO.apellido());
        redirectAttributes.addFlashAttribute("dniSolicitante", solicitanteDTO.dni());
        redirectAttributes.addFlashAttribute("emailSolicitante", solicitanteDTO.email());
        redirectAttributes.addFlashAttribute("telefonoSolicitante", solicitanteDTO.telefono());

        return "redirect:/turnos/confirmacion";
    }

    @GetMapping("/confirmacion")
    public String mostrarConfirmacion(Model model) {
        // Si no hay datos en el modelo, obtenemos el último turno creado
        if (!model.containsAttribute("turnoId")) {
            try {
                List<TurnoDTO> ultimosTurnos = turnoService.obtenerTodos();
                if (!ultimosTurnos.isEmpty()) {
                    // Ordenamos por ID descendente para obtener el último creado
                    // Acceder a los campos de Record Class se hace directamente con el nombre del campo (ej. TurnoDTO::id)
                    Collections.sort(ultimosTurnos, Comparator.comparing(TurnoDTO::id).reversed());
                    TurnoDTO ultimoTurno = ultimosTurnos.get(0);

                    model.addAttribute("turno", ultimoTurno);
                    model.addAttribute("turnoId", ultimoTurno.id()); // Accesor de Record Class

                    // Aquí deberías obtener los datos del solicitante asociado al turno
                    // Acceder a los campos de Record Class se hace directamente con el nombre del campo (ej. ultimoTurno.solicitanteId())
                    if (ultimoTurno.solicitanteId() != null) {
                        // Aquí deberías tener un método para obtener el solicitante por ID
                        // Ejemplo (debes implementar este método o similar):
                        // SolicitanteDTO solicitante = solicitanteService.findById(ultimoTurno.solicitanteId());

                        // Por ahora, usamos datos de ejemplo:
                        model.addAttribute("nombreSolicitante", "Juan");
                        model.addAttribute("apellidoSolicitante", "Pérez");
                        model.addAttribute("dniSolicitante", "12345678");
                        model.addAttribute("emailSolicitante", "juan.perez@email.com");
                        model.addAttribute("telefonoSolicitante", "+54 9 11 1234-5678");
                    }

                    // También podrías obtener la información del servicio/lugar
                    // Acceder a los campos de Record Class se hace directamente con el nombre del campo (ej. ultimoTurno.servicioLugarId())
                    if (ultimoTurno.servicioLugarId() != null) {
                        // Ejemplo (debes implementar estos métodos o similares):
                        // ServicioLugarDTO servicioLugar = servicioLugarService.findById(ultimoTurno.servicioLugarId());

                        model.addAttribute("nombreServicio", "Consulta Médica"); // Ejemplo
                        model.addAttribute("nombreLugar", "Consultorio Central"); // Ejemplo
                    }
                }
            } catch (Exception e) {
                // Manejar posibles errores
                e.printStackTrace();
            }
        }

        return "turno/confirmacion";
    }
}
