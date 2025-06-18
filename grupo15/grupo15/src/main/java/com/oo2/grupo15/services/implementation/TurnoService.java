package com.oo2.grupo15.services.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.oo2.grupo15.dtos.ContactoDTO;
import com.oo2.grupo15.dtos.DireccionDTO;
import com.oo2.grupo15.dtos.LocalidadDTO;
import com.oo2.grupo15.dtos.SolicitanteDTO;
import com.oo2.grupo15.entities.Contacto;
import com.oo2.grupo15.entities.Direccion;
import com.oo2.grupo15.entities.Localidad;
import com.oo2.grupo15.repositories.IContactoRepository;
import com.oo2.grupo15.repositories.ILocalidadRepository;

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
    
    @Autowired
    private ILocalidadRepository localidadRepository;


    private ModelMapper modelMapper = new ModelMapper();
    
    @Override
    public List<TurnoDTO> obtenerTurnosPorSolicitante(Long solicitanteId) {
        return turnoRepository.findBySolicitanteId(solicitanteId).stream()
                .map(turno -> modelMapper.map(turno, TurnoDTO.class))
                .collect(Collectors.toList());
    }


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
    public Turno obtenerEntidadPorId(Long id) {
        return turnoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));
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
        Long dni = solicitanteDTO.getContacto().getDni();
        Solicitante solicitanteExistente = solicitanteRepository.findByContactoDni(dni);

        if (solicitanteExistente != null) {
            System.out.println("Solicitante encontrado con DNI " + dni + ", ID: " + solicitanteExistente.getId());
            dto.setSolicitanteId(solicitanteExistente.getId());
        } else {
            System.out.println("No se encontró solicitante con DNI: " + dni + ". Creando nuevo solicitante...");

            try {
                Solicitante nuevoSolicitante = new Solicitante(false);
                nuevoSolicitante.setEmail(solicitanteDTO.getEmail());
                nuevoSolicitante.setPassword("password_temporal");

                // Crear contacto
                Contacto contacto = new Contacto();
                ContactoDTO contactoDTO = solicitanteDTO.getContacto();
                contacto.setNombre(contactoDTO.getNombre());
                contacto.setApellido(contactoDTO.getApellido());
                contacto.setDni(contactoDTO.getDni());
                contacto.setTelefono(contactoDTO.getTelefono());

                // Crear dirección (si existe)
                DireccionDTO direccionDTO = contactoDTO.getDireccion();
                if (direccionDTO != null && direccionDTO.getLocalidad() != null) {
                    Direccion direccion = new Direccion();
                    direccion.setCalleYAltura(direccionDTO.getCalleYAltura());

                    LocalidadDTO localidadDTO = direccionDTO.getLocalidad();
                    Localidad localidad = localidadRepository.findById(localidadDTO.getId()).orElse(null);

                    if (localidad != null) {
                        direccion.setLocalidad(localidad);
                        contacto.setDireccion(direccion);
                    }
                }

                Contacto contactoGuardado = contactoRepository.save(contacto);
                nuevoSolicitante.setContacto(contactoGuardado);

                Solicitante saved = solicitanteRepository.save(nuevoSolicitante);
                System.out.println("Nuevo solicitante creado con ID: " + saved.getId());

                dto.setSolicitanteId(saved.getId());

            } catch (Exception e) {
                System.err.println("Error al crear solicitante: " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("Creando turno con solicitanteId: " + dto.getSolicitanteId());
        return crearTurno(dto);
    }



}