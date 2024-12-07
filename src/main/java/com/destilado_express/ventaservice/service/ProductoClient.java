package com.destilado_express.ventaservice.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.destilado_express.ventaservice.model.dto.ProductoDTO;


@Service
public class ProductoClient {

    private final WebClient webClient;

    public ProductoClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public ProductoDTO obtenerDetalleProducto(Long id) {
        // Recuperar el token del contexto de seguridad
        String token = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();

        return webClient.get()
                .uri("/{id}", id)
                .headers(headers -> headers.setBearerAuth(token)) // Incluir el token como Bearer
                .retrieve()
                .bodyToMono(ProductoDTO.class)
                .block();
    }

}
