// src/main/java/com/oo2/grupo15/configurations/SecurityConfiguration.java
package com.oo2.grupo15.configurations;

import com.oo2.grupo15.services.implementation.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final UsuarioService usuarioService;

    public SecurityConfiguration(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    // Permitir acceso a los recursos de Swagger UI
                    auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll();

                    // Recursos públicos
                    auth.requestMatchers("/css/**", "/imgs/**", "/js/**", "/vendor/bootstrap/css/**",
                            "/vendor/jquery/**", "/vendor/bootstrap/js/**", "/api/v1/**").permitAll();
                    
                    //
                    auth.requestMatchers("/api/lugares/**").permitAll();  

                    // Páginas de autenticación
                    auth.requestMatchers("/auth/login", "/auth/loginProcess", "/auth/loginSuccess",
                            "/auth/logout", "/auth/registro").permitAll();

                    // Home page y turnos - acceso público
                    auth.requestMatchers("/", "/home", "/index").permitAll();
                    auth.requestMatchers("/turnos/**").permitAll();

                    // Páginas de administración - solo ADMIN
                    auth.requestMatchers("/usuarios/**").hasRole("ADMIN");
                    auth.requestMatchers("/SERVICIOS/**").hasRole("ADMIN");
                    auth.requestMatchers("/LUGARES/**").hasRole("ADMIN");

                    // Todo lo demás requiere autenticación
                    auth.anyRequest().authenticated();
                })
                .formLogin(login -> {
                    login.loginPage("/auth/login");
                    login.loginProcessingUrl("/auth/loginProcess");
                    login.usernameParameter("username");
                    login.passwordParameter("password");
                    login.defaultSuccessUrl("/auth/loginSuccess", true);
                    login.permitAll();
                })
                .logout(logout -> {
                    logout.logoutUrl("/auth/logout");
                    logout.logoutSuccessUrl("/auth/login");
                    logout.permitAll();
                })
                .build();
    }


    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(usuarioService);
        return provider;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
