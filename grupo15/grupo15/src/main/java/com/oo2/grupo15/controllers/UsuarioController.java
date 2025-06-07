package com.oo2.grupo15.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.oo2.grupo15.dtos.UsuarioDTO;
import com.oo2.grupo15.helpers.ViewRouteHelper;
import com.oo2.grupo15.services.IUsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;
    
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

    @PostMapping("/guardar")
    public ModelAndView guardarUsuario(@ModelAttribute("usuarioNuevo") UsuarioDTO dto) {
    	if (dto.getId() == null) {
    	    usuarioService.crearUsuario(dto);
    	} else {
    	    usuarioService.actualizarUsuario(dto.getId(), dto);
    	}

        return new ModelAndView("redirect:/usuarios");
    }

    @GetMapping("/eliminar/{id}")
    public ModelAndView eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return new ModelAndView("redirect:/usuarios");
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editarUsuario(@PathVariable Long id) {
        UsuarioDTO usuario = usuarioService.obtenerPorId(id);
        ModelAndView mAV = new ModelAndView("usuarios");
        mAV.addObject("usuarioNuevo", usuario);
        mAV.addObject("usuarios", usuarioService.obtenerTodos());
        return mAV;
    }
}