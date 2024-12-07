package com.destilado_express.ventaservice.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class VentaDTO {

    private Long id;
    private Long userId;
    private List<ProductoDTO> productos;
    private Boolean activa = true;
    private LocalDateTime created;
    private LocalDateTime updated;

}