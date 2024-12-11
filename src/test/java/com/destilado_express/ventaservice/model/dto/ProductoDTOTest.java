package com.destilado_express.ventaservice.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ProductoDTOTest {

    @Test
    void constructorTest() {
        var producto = new ProductoDTO("Producto", "Descripcion", "http://fake.url", 9990, 6990, 100, true);
        assertEquals(ProductoDTO.class, producto.getClass());
        assertEquals(9990, producto.getPrecio());
    }
}
