package com.oo2.grupo15.controllers;

import com.oo2.grupo15.exceptions.DNIExistenteException;
import com.oo2.grupo15.exceptions.UsuarioConTurnosActivosException;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.oo2.grupo15.dtos.UsuarioDTO;
import com.oo2.grupo15.helpers.ViewRouteHelper;
import com.oo2.grupo15.services.IUsuarioService;
import com.oo2.grupo15.services.ITurnoService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private ITurnoService turnoService;

	@GetMapping
	public String index(Model model) {
		model.addAttribute("usuarios", usuarioService.obtenerTodos());
		model.addAttribute("usuarioNuevo", new UsuarioDTO());
		return ViewRouteHelper.USUARIO_INDEX;
	}

	@GetMapping("/todos")
	public ModelAndView mostrarUsuarios() {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.USUARIO_INDEX);
		mAV.addObject("usuarios", usuarioService.obtenerTodos());
		mAV.addObject("usuarioNuevo", new UsuarioDTO());
		return mAV;
	}

	@GetMapping("/gestion")
	public ModelAndView gestionarUsuarios(@RequestParam(name = "dni", required = false) Long dni) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.USUARIO_ALL);
		List<UsuarioDTO> usuarios;

		if (dni != null) {
			Optional<UsuarioDTO> usuarioEncontrado = usuarioService.findByContactoDni(dni);

			if (usuarioEncontrado.isPresent()) {
				usuarios = List.of(usuarioEncontrado.get());
			} else {
				usuarios = List.of();
			}
		} else {
			usuarios = usuarioService.getAll();
		}

		mAV.addObject("usuarios", usuarios);
		mAV.addObject("dniBuscado", dni);
		return mAV;
	}

	@PostMapping("/guardar")
	public ModelAndView guardarUsuario(@ModelAttribute("usuarioNuevo") UsuarioDTO dto,
			RedirectAttributes redirectAttributes) {

		if (dto.getId() == null) {
			usuarioService.crearUsuario(dto);
			redirectAttributes.addFlashAttribute("mensaje", "Usuario creado correctamente.");
		} else {
			usuarioService.actualizarUsuario(dto.getId(), dto);
			redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado correctamente.");
		}
		return new ModelAndView(ViewRouteHelper.REDIRECT_USUARIOS_ROOT);

	}

	@GetMapping("/eliminar/{id}")
	public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			// Verificar si el usuario tiene turnos activos
			if (turnoService.tieneTurnosActivos(id)) {
				throw new UsuarioConTurnosActivosException(id);
			}

			// Si no tiene turnos activos, proceder con la eliminación
			usuarioService.eliminarUsuario(id);
			redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado correctamente");

		} catch (UsuarioConTurnosActivosException ex) {
			// Capturar la excepción y añadir un mensaje de error
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
		}

		return "redirect:/usuarios";
	}

	@GetMapping("/editar/{id}")
	public ModelAndView editarUsuario(@PathVariable Long id) {
		UsuarioDTO usuario = usuarioService.obtenerPorId(id);
		ModelAndView mAV = new ModelAndView("usuario/usuario-editar"); // Incluir la subcarpeta
		mAV.addObject("usuarioNuevo", usuario);
		return mAV;
	}
}