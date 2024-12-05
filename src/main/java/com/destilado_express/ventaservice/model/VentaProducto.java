package com.destilado_express.ventaservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;

@Entity
@Data
@IdClass(VentaProductoId.class)
public class VentaProducto {

    @Id
    private Long idVenta;

    @Id
    private Long idProducto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double precioUnidad;

    @Column(nullable = false)
    private Double monto;
}