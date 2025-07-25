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

import com.oo2.grupo15.dtos.ContactoDTO;
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
        // Crear un DTO vacío para el formulario
        ContactoDTO contactoVacio = new ContactoDTO(null, null, 0, null, null, null);
        UsuarioDTO usuarioVacio = new UsuarioDTO(null, null, null, contactoVacio);
        mav.addObject("usuarioNuevo", usuarioVacio);
        return mav;
    }

    @PostMapping("/registro")
    public ModelAndView procesarRegistro(@ModelAttribute("usuarioNuevo") UsuarioDTO usuarioNuevo) {
        System.out.println("Datos recibidos: " + usuarioNuevo.email() + " / " + usuarioNuevo.password());

        UsuarioDTO existente = usuarioService.buscarPorEmail(usuarioNuevo.email());
        if (existente != null) {
            ModelAndView mav = new ModelAndView("registro/index");
            mav.addObject("error", "El correo ya está registrado");
            return mav;
        }

        String passwordEncriptada = passwordEncoder.encode(usuarioNuevo.password());
        
        UsuarioDTO usuarioConPasswordEncriptada = new UsuarioDTO(
            usuarioNuevo.id(),
            usuarioNuevo.email(),
            passwordEncriptada,
            usuarioNuevo.contacto()
        );

        UsuarioDTO guardado = usuarioService.guardar(usuarioConPasswordEncriptada);

        Usuario usuario = new Usuario();
        usuario.setId(guardado.id());

        Role rolUser = roleRepository.findByName("USER");
        if (rolUser != null) {
            usuario.addRole(rolUser);
            usuarioService.guardar(guardado);
        }

        System.out.println("Usuario registrado: " + guardado.email());

        return new ModelAndView("redirect:/auth/login");
    }
}