package com.oo2.grupo15.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.oo2.grupo15.dtos.UsuarioDTO;
import com.oo2.grupo15.services.IUsuarioService;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class LoginController {

	@Autowired
	private IUsuarioService usuarioService;
	
	@GetMapping("/login")
    public ModelAndView mostrarLogin(@RequestParam(name = "error", required = false) String error,
                                     @RequestParam(name = "logout", required = false) String logout) {
        ModelAndView mav = new ModelAndView("login/index");
        if (error != null) mav.addObject("error", "Email o contraseña incorrectos");
        if (logout != null) mav.addObject("logout", "Sesión cerrada correctamente");
        return mav;
    }

    @PostMapping("/login")
    public ModelAndView procesarLogin(@RequestParam String email,
                                      @RequestParam String password,
                                      HttpSession session) {
        UsuarioDTO usuario = usuarioService.buscarPorEmail(email);

        if (usuario != null && usuario.getPassword().equals(password)) {
            session.setAttribute("usuarioLogueado", usuario);
            return new ModelAndView("redirect:/");
        }

        ModelAndView mav = new ModelAndView("redirect:/auth/login");
        mav.addObject("error", true);
        return mav;
    }

    @PostMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        session.invalidate();
        return new ModelAndView("redirect:/auth/login?logout=true");
    }
    
    @GetMapping("/registro")
    public ModelAndView mostrarRegistro() {
        ModelAndView mav = new ModelAndView("registro/index");
        mav.addObject("usuarioNuevo", new UsuarioDTO());
        return mav;
    }
    
    @PostMapping("/registro")
    public ModelAndView procesarRegistro(@ModelAttribute("usuarioNuevo") UsuarioDTO usuarioNuevo) {
        System.out.println("Datos recibidos: " + usuarioNuevo.getEmail() + " / " + usuarioNuevo.getPassword());

        UsuarioDTO existente = usuarioService.buscarPorEmail(usuarioNuevo.getEmail());
        if (existente != null) {
            ModelAndView mav = new ModelAndView("registro/index");
            mav.addObject("error", "El correo ya está registrado");
            return mav;
        }

        System.out.println("Guardando usuario: " + usuarioNuevo.getEmail());

        UsuarioDTO guardado = usuarioService.guardar(usuarioNuevo);

        System.out.println("Usuario guardado: " + guardado.getEmail());

        return new ModelAndView("redirect:/auth/login");
    }


}
