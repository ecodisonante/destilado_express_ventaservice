package com.destilado_express.ventaservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.destilado_express.ventaservice.model.Venta;
import com.destilado_express.ventaservice.model.VentaProducto;
import com.destilado_express.ventaservice.model.dto.VentaDTO;
import com.destilado_express.ventaservice.service.VentaService;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService ventaService;

    @Autowired
    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    // Obtener todas las ventas
    @GetMapping
    public ResponseEntity<List<Venta>> obtenerVentas() {
        List<Venta> productos = ventaService.obtenerVentas();
        return ResponseEntity.ok(productos);
    }

    // Obtener la venta activa del usuario
    @GetMapping("/activa")
    public ResponseEntity<VentaDTO> obtenerVentaActiva() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Venta venta;

        var existent = ventaService.obtenerVentaActivaPorUsuario(userId);
        if (!existent.isPresent()) {
            var nueva = new Venta();
            nueva.setUserId(userId);
            venta = ventaService.crearVenta(nueva);
        } else
            venta = existent.get();

        return ResponseEntity.ok(ventaService.toVentaDTO(venta));
    }

    // Obtener la venta activa del usuario
    @GetMapping("/{ventaId}")
    public ResponseEntity<VentaDTO> obtenerVentaDetalle(@PathVariable Long ventaId) {
        Venta venta = ventaService.obtenerVentaPorId(ventaId);

        if (venta != null) {
            return ResponseEntity.ok(ventaService.toVentaDTO(venta));
        }

        return ResponseEntity.notFound().build();

    }

    // Crear una nueva venta
    @PostMapping
    public ResponseEntity<Venta> crearVenta(@RequestBody Venta venta) {
        Venta nuevaVenta = ventaService.crearVenta(venta);
        return ResponseEntity.ok(nuevaVenta);
    }

    // Agregar un producto a la venta (carrito)
    @PostMapping("/{ventaId}/productos")
    public ResponseEntity<VentaProducto> agregarProducto(
            @PathVariable Long ventaId, @RequestBody VentaProducto producto) {
        VentaProducto nuevoProducto = ventaService.agregarProducto(ventaId, producto);
        return ResponseEntity.ok(nuevoProducto);
    }

    // Actualizar un producto en la venta
    @PutMapping("/{ventaId}/productos/{productoId}")
    public ResponseEntity<VentaProducto> actualizarProducto(
            @PathVariable Long ventaId,
            @PathVariable Long productoId,
            @RequestBody VentaProducto producto) {
        VentaProducto productoActualizado = ventaService.actualizarProducto(ventaId, productoId, producto);
        return ResponseEntity.ok(productoActualizado);
    }

    // Actualizar un producto en la venta
    @PutMapping("/{ventaId}")
    public ResponseEntity<Venta> finalizarVenta(@PathVariable Long ventaId) {
        Venta ventaFinalizada = ventaService.finalizarVenta(ventaId);
        return ResponseEntity.ok(ventaFinalizada);
    }

    // Eliminar un producto de la venta
    @DeleteMapping("/{ventaId}/productos/{productoId}")
    public ResponseEntity<Void> eliminarProducto(
            @PathVariable Long ventaId, @PathVariable Long productoId) {
        ventaService.eliminarProducto(ventaId, productoId);
        return ResponseEntity.noContent().build();
    }

}
