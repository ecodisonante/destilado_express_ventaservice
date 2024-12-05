package com.destilado_express.ventaservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.destilado_express.ventaservice.model.Venta;

public interface VentaRepository extends JpaRepository<Venta, Long> {
}
