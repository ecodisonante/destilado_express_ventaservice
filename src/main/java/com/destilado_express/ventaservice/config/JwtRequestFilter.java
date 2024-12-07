package com.destilado_express.ventaservice.config;

import java.io.IOException;

import java.util.List;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.destilado_express.ventaservice.service.auth.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private JwtService jwtService;

    @Autowired
    public JwtRequestFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String token = null;
        String username = null;
        String role = null;
        Long id = null;

        // Extraer el token JWT del encabezado Authorization
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            username = jwtService.extractUsername(token);
            role = jwtService.extractRole(token);
            id = jwtService.extractId(token);
        }

        // Token valido, contiuar
        if (username != null && role != null && jwtService.validateToken(token)) {
            // Agregar roles al usuario
            List<GrantedAuthority> authorities = Collections
                    .singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id,
                    null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);
        } else {
            // Token invalido, rechazar
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
        }
    }
}
