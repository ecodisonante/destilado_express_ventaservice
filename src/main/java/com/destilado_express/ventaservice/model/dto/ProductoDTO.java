package com.destilado_express.ventaservice.model.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductoDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private String imagen;
    private int precio;
    private int oferta;
    private int stock;
    private Boolean disponible;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public ProductoDTO(String nombre, String descripcion, String imagen, int precio, int oferta, int stock,
            Boolean disponible) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.precio = precio;
        this.oferta = oferta;
        this.stock = stock;
        this.disponible = disponible;
    }
}
