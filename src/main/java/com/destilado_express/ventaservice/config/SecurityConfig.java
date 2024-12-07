package com.destilado_express.ventaservice.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private JwtRequestFilter jwtRequestFilter; // Reutiliza o adapta el filtro de JWT
    private String ventasMapping = "/api/ventas";
    private String adminRole = "ADMIN";
    private String userRole = "USER";

    @Autowired
    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)

                .authorizeHttpRequests(req -> req
                        // solo usuarios
                        .requestMatchers(HttpMethod.GET, ventasMapping + "/activa").hasRole(userRole)
                        .requestMatchers(HttpMethod.POST, ventasMapping).hasRole(userRole) // crear venta
                        .requestMatchers(HttpMethod.PUT, ventasMapping).hasRole(userRole)
                        .requestMatchers(HttpMethod.GET, ventasMapping + "/{ventaId}/productos").hasRole(userRole) // Obtener productos de venta
                        .requestMatchers(HttpMethod.POST, ventasMapping + "/{ventaId}/productos").hasRole(userRole) // Agregar producto a venta
                        .requestMatchers(HttpMethod.PUT, ventasMapping + "/{ventaId}/productos/{productoId}").hasRole(userRole) // Actualizar producto venta
                        .requestMatchers(HttpMethod.DELETE, ventasMapping + "/{ventaId}/productos/{productoId}").hasRole(userRole) // Actualizar producto venta
                        // solo admin
                        .requestMatchers(HttpMethod.GET, ventasMapping).hasRole(adminRole) // ver todas las ventas
                        .requestMatchers(HttpMethod.GET, ventasMapping + "/{id}").hasRole(adminRole) // Obtener venta por ID
                        .requestMatchers(HttpMethod.DELETE, ventasMapping + "/**").hasRole(adminRole)
                        // otros
                        .anyRequest().authenticated());

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost");
        config.addAllowedOriginPattern("http://localhost:*");
        config.addAllowedOrigin("http://frontend");
        config.addAllowedOriginPattern("http://frontend:*");
        config.addAllowedHeader("*");
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
