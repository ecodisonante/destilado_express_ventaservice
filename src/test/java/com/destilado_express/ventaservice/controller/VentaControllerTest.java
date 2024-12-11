package com.destilado_express.ventaservice.controller;

import com.destilado_express.ventaservice.model.Venta;
import com.destilado_express.ventaservice.model.VentaProducto;
import com.destilado_express.ventaservice.model.dto.VentaDTO;
import com.destilado_express.ventaservice.service.VentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VentaControllerTest {

    @Mock
    private VentaService ventaService;

    @InjectMocks
    private VentaController ventaController;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(1L); // Mock User ID
    }

    @Test
    void obtenerVentas() {
        List<Venta> ventas = List.of(new Venta());
        when(ventaService.obtenerVentas()).thenReturn(ventas);

        ResponseEntity<List<Venta>> response = ventaController.obtenerVentas();
        assertEquals(200, response.getStatusCode().value());
        assertEquals(ventas, response.getBody());
    }

    @Test
    void obtenerVentaActiva() {
        Venta venta = new Venta();
        venta.setUserId(1L);
        when(ventaService.obtenerVentaActivaPorUsuario(anyLong())).thenReturn(Optional.of(venta));
        when(ventaService.toVentaDTO(any(Venta.class))).thenReturn(new VentaDTO());

        ResponseEntity<VentaDTO> response = ventaController.obtenerVentaActiva();
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void obtenerVentaActivaCreateNew() {
        Venta venta = new Venta();
        venta.setUserId(1L);
        when(ventaService.obtenerVentaActivaPorUsuario(anyLong())).thenReturn(Optional.empty());
        when(ventaService.crearVenta(any())).thenReturn(venta);
        when(ventaService.toVentaDTO(any(Venta.class))).thenReturn(new VentaDTO());

        ResponseEntity<VentaDTO> response = ventaController.obtenerVentaActiva();
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }

    @Test
    void obtenerVentaDetalle() {
        Venta venta = new Venta();
        when(ventaService.obtenerVentaPorId(anyLong())).thenReturn(venta);
        when(ventaService.toVentaDTO(any(Venta.class))).thenReturn(new VentaDTO());

        ResponseEntity<VentaDTO> response = ventaController.obtenerVentaDetalle(1L);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
    }


    @Test
    void obtenerVentaDetalleNotFound() {
        when(ventaService.obtenerVentaPorId(anyLong())).thenReturn(null);

        ResponseEntity<VentaDTO> response = ventaController.obtenerVentaDetalle(1L);
        assertEquals(404, response.getStatusCode().value());
    }


    @Test
    void crearVenta() {
        Venta venta = new Venta();
        when(ventaService.crearVenta(any(Venta.class))).thenReturn(venta);

        ResponseEntity<Venta> response = ventaController.crearVenta(venta);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(venta, response.getBody());
    }

    @Test
    void agregarProducto() {
        VentaProducto producto = new VentaProducto();
        when(ventaService.agregarProducto(anyLong(), any(VentaProducto.class))).thenReturn(producto);

        ResponseEntity<VentaProducto> response = ventaController.agregarProducto(1L, producto);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(producto, response.getBody());
    }

    @Test
    void actualizarProducto() {
        VentaProducto producto = new VentaProducto();
        when(ventaService.actualizarProducto(anyLong(), anyLong(), any(VentaProducto.class))).thenReturn(producto);

        ResponseEntity<VentaProducto> response = ventaController.actualizarProducto(1L, 1L, producto);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(producto, response.getBody());
    }

    @Test
    void finalizarVenta() {
        Venta venta = new Venta();
        when(ventaService.finalizarVenta(anyLong())).thenReturn(venta);

        ResponseEntity<Venta> response = ventaController.finalizarVenta(1L);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(venta, response.getBody());
    }

    @Test
    void eliminarProducto() {
        doNothing().when(ventaService).eliminarProducto(anyLong(), anyLong());

        ResponseEntity<Void> response = ventaController.eliminarProducto(1L, 1L);
        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
    }
}
