package com.destilado_express.ventaservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.destilado_express.ventaservice.model.Venta;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    
    // Obtener la venta activa de un usuario (si solo hay una)
    Optional<Venta> findByUserIdAndActivaTrue(Long userId);
    List<Venta> findByActiva(Boolean activa);

}
