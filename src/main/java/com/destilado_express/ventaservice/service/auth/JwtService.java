package com.destilado_express.ventaservice.service.auth;

public interface JwtService {
    boolean validateToken(String token);

    String extractUsername(String token);

    String extractRole(String token);

    Long extractId(String token);
}
