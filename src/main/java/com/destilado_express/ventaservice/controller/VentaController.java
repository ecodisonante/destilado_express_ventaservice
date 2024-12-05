package com.destilado_express.ventaservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.destilado_express.ventaservice.model.Venta;
import com.destilado_express.ventaservice.service.VentaService;

import java.util.List;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    private VentaService ventaService;

    @Autowired
    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    // Obtener todas las ventas
    @GetMapping
    public List<Venta> getAllVentas() {
        return ventaService.getAllVentas();
    }

    // Obtener una venta por ID
    @GetMapping("/{id}")
    public ResponseEntity<Venta> getVentaById(@PathVariable Long id) {
        Venta venta = ventaService.getVentaById(id);
        if (venta != null) {
            return ResponseEntity.ok(venta);
        }
        return ResponseEntity.notFound().build();
    }

    // Crear una nueva venta
    @PostMapping
    public ResponseEntity<Venta> createVenta(@RequestBody Venta venta) {
        Venta createdVenta = ventaService.createVenta(venta);
        return ResponseEntity.ok(createdVenta);
    }

    // Actualizar una venta existente
    @PutMapping("/{id}")
    public ResponseEntity<Venta> updateVenta(@PathVariable Long id, @RequestBody Venta venta) {
        Venta updatedVenta = ventaService.updateVenta(id, venta);
        if (updatedVenta != null) {
            return ResponseEntity.ok(updatedVenta);
        }
        return ResponseEntity.notFound().build();
    }

    // Eliminar una venta por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenta(@PathVariable Long id) {
        boolean deleted = ventaService.deleteVenta(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
