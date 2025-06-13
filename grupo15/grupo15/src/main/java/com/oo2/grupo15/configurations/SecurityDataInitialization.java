package com.oo2.grupo15.configurations;

import com.oo2.grupo15.entities.Contacto;
import com.oo2.grupo15.entities.Role;
import com.oo2.grupo15.entities.Usuario;
import com.oo2.grupo15.repositories.IRoleRepository;
import com.oo2.grupo15.repositories.IUsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityDataInitialization {

    @Bean
    CommandLineRunner initSecurityData(IRoleRepository roleRepository,
                                       IUsuarioRepository usuarioRepository,
                                       PasswordEncoder passwordEncoder) {
        return args -> {
            // Crear roles si no existen
            if (roleRepository.count() == 0) {
                roleRepository.save(new Role("ADMIN"));
                roleRepository.save(new Role("USER"));
                System.out.println("Roles creados: ADMIN, USER");
            }

            // Crear usuario administrador si no existe
            if (usuarioRepository.findByEmail("admin@sistema.com").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setEmail("admin@sistema.com");
                admin.setPassword(passwordEncoder.encode("admin123"));

                Contacto contacto = new Contacto();
                contacto.setNombre("Administrador");
                contacto.setApellido("Sistema");
                contacto.setDni(0);
                admin.setContacto(contacto);

                admin.addRole(roleRepository.findByName("ADMIN"));

                usuarioRepository.save(admin);
                System.out.println("Usuario admin creado: admin@sistema.com");
            }
        };
    }
}