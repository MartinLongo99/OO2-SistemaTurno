package com.oo2.grupo15.services.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.oo2.grupo15.dtos.SolicitanteDTO;
import com.oo2.grupo15.entities.Contacto;
import com.oo2.grupo15.repositories.IContactoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oo2.grupo15.dtos.TurnoDTO;
import com.oo2.grupo15.entities.Solicitante;
import com.oo2.grupo15.entities.Turno;
import com.oo2.grupo15.repositories.IServicioLugarRepository;
import com.oo2.grupo15.repositories.ISolicitanteRepository;
import com.oo2.grupo15.repositories.ITurnoRepository;
import com.oo2.grupo15.services.IEmailService;
import com.oo2.grupo15.services.ITurnoService;

@Service
public class TurnoService implements ITurnoService {

    @Autowired
    private ITurnoRepository turnoRepository;

    @Autowired
    private IServicioLugarRepository servicioLugarRepository;

    @Autowired
    private ISolicitanteRepository solicitanteRepository;
    @Autowired
    private IContactoRepository contactoRepository;
    
    @Autowired
    private IEmailService emailService;

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public TurnoDTO crearTurno(TurnoDTO dto) {
        Turno turno = new Turno();
        turno.setFechaHora(dto.getFechaHora());
        turno.setEstado(dto.isEstado());

        if (dto.getServicioLugarId() != null) {
            turno.setServicioLugar(servicioLugarRepository.findById(dto.getServicioLugarId())
                    .orElseThrow(() -> new RuntimeException("ServicioLugar no encontrado")));
        }

        if (dto.getSolicitanteId() != null) {
            turno.setSolicitante(solicitanteRepository.findById(dto.getSolicitanteId())
                    .orElseThrow(() -> new RuntimeException("Solicitante no encontrado")));
        }

        Turno saved = turnoRepository.save(turno);
        
     // Enviar email de confirmación
        try {
            Solicitante solicitante = turno.getSolicitante();
            String email = solicitante.getEmail();

            if (email != null && !email.isEmpty()) {
                org.thymeleaf.context.Context context = new org.thymeleaf.context.Context();
                context.setVariable("solicitante", solicitante);
                context.setVariable("servicioLugar", turno.getServicioLugar());
                context.setVariable("fechaHora", turno.getFechaHora());
                context.setVariable("profesional", turno.getServicioLugar().getProfesional());

                emailService.sendMailWithThymeleafTemplate(email, "Confirmación de Turno", "mail/turno-confirmacion.html", context);
            } else {
                System.out.println("El solicitante no tiene un email válido, no se envía correo.");
            }

        } catch (Exception e) {
            System.err.println("Error al intentar enviar el correo de confirmación: " + e.getMessage());
        }

        
        return modelMapper.map(saved, TurnoDTO.class);
    }
    public boolean tieneTurnosActivos(Long usuarioId) {
        // Asumiendo que tienes un método en tu repositorio que cuenta turnos activos
        return turnoRepository.countBySolicitanteIdAndEstadoActivo(usuarioId) > 0;
    }

    @Override
    public TurnoDTO actualizarTurno(Long id, TurnoDTO dto) {
        Turno turno = turnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        turno.setFechaHora(dto.getFechaHora());
        turno.setEstado(dto.isEstado());

        if (dto.getServicioLugarId() != null) {
            turno.setServicioLugar(servicioLugarRepository.findById(dto.getServicioLugarId())
                    .orElseThrow(() -> new RuntimeException("ServicioLugar no encontrado")));
        }

        if (dto.getSolicitanteId() != null) {
            turno.setSolicitante(solicitanteRepository.findById(dto.getSolicitanteId())
                    .orElseThrow(() -> new RuntimeException("Solicitante no encontrado")));
        }

        Turno updated = turnoRepository.save(turno);
        return modelMapper.map(updated, TurnoDTO.class);
    }

    @Override
    public void eliminarTurno(Long id) {
        turnoRepository.deleteById(id);
    }

    @Override
    public List<TurnoDTO> obtenerTodos() {
        return turnoRepository.findAll().stream()
                .map(turno -> modelMapper.map(turno, TurnoDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public TurnoDTO obtenerPorId(Long id) {
        return modelMapper.map(
                turnoRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Turno no encontrado")),
                TurnoDTO.class);
    }

    @Override
    public List<TurnoDTO> obtenerTurnosEntreFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Object[]> resultados = turnoRepository.buscarTurnosSimplificados(fechaInicio, fechaFin);
        return resultados.stream()
                .map(resultado -> {
                    TurnoDTO dto = new TurnoDTO();
                    dto.setId((Long) resultado[0]);
                    dto.setFechaHora((LocalDateTime) resultado[1]);
                    dto.setEstado((Boolean) resultado[2]);
                    return dto;
                })
                .collect(Collectors.toList());
    }
    @Override
    public TurnoDTO crearTurnoConDni(TurnoDTO dto, SolicitanteDTO solicitanteDTO) {
        // Primero verificamos si existe un solicitante con ese DNI usando el método
        Solicitante solicitanteExistente = solicitanteRepository.findByContactoDni(solicitanteDTO.getDni());

        // Si encontramos un solicitante, usamos su ID
        if (solicitanteExistente != null) {
            System.out.println("Solicitante encontrado con DNI " + solicitanteDTO.getDni() + ", ID: " + solicitanteExistente.getId());
            dto.setSolicitanteId(solicitanteExistente.getId());
        } else {
            System.out.println("No se encontró solicitante con DNI: " + solicitanteDTO.getDni() + ". Creando nuevo solicitante...");

            try {
                // Crear nuevo solicitante - con constructor booleano para el atributo 'pago'
                Solicitante nuevoSolicitante = new Solicitante(false);

                // Configurar propiedades del Usuario
                nuevoSolicitante.setEmail(solicitanteDTO.getEmail());
                nuevoSolicitante.setPassword("password_temporal"); // Contraseña temporal

                // Crear y configurar el contacto
                Contacto contacto = new Contacto();
                contacto.setNombre(solicitanteDTO.getNombre());
                contacto.setApellido(solicitanteDTO.getApellido());
                contacto.setDni(solicitanteDTO.getDni());

                // Guardar primero el contacto
                Contacto contactoGuardado = contactoRepository.save(contacto);

                // Asignar el contacto guardado al solicitante
                nuevoSolicitante.setContacto(contactoGuardado);

                // Guardar el solicitante
                Solicitante saved = solicitanteRepository.save(nuevoSolicitante);
                System.out.println("Nuevo solicitante creado con ID: " + saved.getId());

                // Asignar el ID del solicitante al DTO del turno
                dto.setSolicitanteId(saved.getId());
            } catch (Exception e) {
                System.err.println("Error al crear solicitante: " + e.getMessage());
                e.printStackTrace();
                // Si falla la creación del solicitante, continuamos sin asignar uno al turno
            }
        }

        // Asegurarnos de que el DTO tenga el solicitanteId
        System.out.println("Creando turno con solicitanteId: " + dto.getSolicitanteId());

        // Ahora creamos el turno con el solicitante asignado
        return crearTurno(dto);
    }
    
    @Override
    public List<TurnoDTO> obtenerTurnosPorEmailSolicitante(String email) {
        List<Turno> turnos = turnoRepository.findBySolicitante_Email(email);


        return turnos.stream().map(t -> {
            TurnoDTO dto = modelMapper.map(t, TurnoDTO.class);

            if (t.getServicioLugar() != null) {
                if (t.getServicioLugar().getServicio() != null) {
                    dto.setNombreServicio(t.getServicioLugar().getServicio().getNombre());
                }
                if (t.getServicioLugar().getLugar() != null) {
                    dto.setNombreLugar(t.getServicioLugar().getLugar().getNombre());
                }
            }

            return dto;
        }).collect(Collectors.toList());
    }


}