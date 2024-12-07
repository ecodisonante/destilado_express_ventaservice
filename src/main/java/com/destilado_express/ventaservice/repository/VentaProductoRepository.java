package com.destilado_express.ventaservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.destilado_express.ventaservice.model.VentaProducto;

public interface VentaProductoRepository extends JpaRepository<VentaProducto, Long> {

    List<VentaProducto> findAllByVentaId(Long ventaId);
    Optional<VentaProducto> findByIdProductoAndVentaId(Long id, Long ventaId);
    
}
