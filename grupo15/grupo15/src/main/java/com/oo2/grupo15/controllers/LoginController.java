package com.oo2.grupo15.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.oo2.grupo15.dtos.UsuarioDTO;
import com.oo2.grupo15.entities.Contacto;
import com.oo2.grupo15.entities.Role;
import com.oo2.grupo15.entities.Usuario;
import com.oo2.grupo15.repositories.IRoleRepository;
import com.oo2.grupo15.services.IUsuarioService;

@Controller
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public ModelAndView mostrarLogin(@RequestParam(name = "error", required = false) String error,
                                     @RequestParam(name = "logout", required = false) String logout) {
        ModelAndView mav = new ModelAndView("login/index");
        if (error != null) mav.addObject("error", "Email o contraseña incorrectos");
        if (logout != null) mav.addObject("logout", "Sesión cerrada correctamente");
        return mav;
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess() {
        return "redirect:/";
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

        // Encriptar la contraseña antes de guardar
        String passwordEncriptada = passwordEncoder.encode(usuarioNuevo.getPassword());
        usuarioNuevo.setPassword(passwordEncriptada);

        // Asignar rol de USER al nuevo usuario
        UsuarioDTO guardado = usuarioService.guardar(usuarioNuevo);

        // Ahora necesitamos asignar el rol USER manualmente
        // Como estamos trabajando con DTOs, tenemos que obtener la entidad
        Usuario usuario = new Usuario();
        usuario.setId(guardado.getId());

        // Obtener el rol USER
        Role rolUser = roleRepository.findByName("USER");
        if (rolUser != null) {
            usuario.addRole(rolUser);
            // Guardar el usuario con el rol asignado
            usuarioService.guardar(guardado);
        }

        System.out.println("Usuario registrado: " + guardado.getEmail());

        return new ModelAndView("redirect:/auth/login");
    }
}