package com.algaworks.algafood.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
            		.requestMatchers("v1/cidades/**").permitAll()
            		.requestMatchers("v1/restaurantes/**").permitAll()
                .anyRequest().authenticated() // Exige autenticação para todas as requisições
            )
            .httpBasic(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
	
	@Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
            .inMemoryAuthentication()
            .passwordEncoder(passwordEncoder())
            .withUser("algafood").password(passwordEncoder().encode("123456")).roles("ADMIN")
            .and()
            .withUser("herlon").password(passwordEncoder().encode("123456")).roles("ADMIN");
        
        return authenticationManagerBuilder.build();
    }
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Usando BCrypt para codificar senhas
    }
}
