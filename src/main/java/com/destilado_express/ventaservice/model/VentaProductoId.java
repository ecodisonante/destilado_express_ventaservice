package com.destilado_express.ventaservice.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class VentaProductoId implements Serializable {
    private Long idVenta;
    private Long idProducto;
}
