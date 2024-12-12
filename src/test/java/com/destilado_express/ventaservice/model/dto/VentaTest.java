package com.destilado_express.ventaservice.model.dto;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.destilado_express.ventaservice.model.Venta;

class VentaTest {

    private Venta venta;

    @Test
    void onCreateTest() {
        // arrange
        venta = new Venta();
        // act
        venta.onCreate();

        // assert
        assertNotNull(venta.getCreated());
        assertTrue(venta.getCreated().isBefore(LocalDateTime.now())
                || venta.getCreated().isEqual(LocalDateTime.now()));
    }

    @Test
    void onUpdateTest() {
        // arrange
        venta = new Venta();
        // act
        venta.onUpdate();

        // assert
        assertNotNull(venta.getUpdated());
        assertTrue(venta.getUpdated().isBefore(LocalDateTime.now())
                || venta.getUpdated().isEqual(LocalDateTime.now()));
    }
}
