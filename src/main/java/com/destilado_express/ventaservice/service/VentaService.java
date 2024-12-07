package com.destilado_express.ventaservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.destilado_express.ventaservice.model.Venta;
import com.destilado_express.ventaservice.model.VentaProducto;
import com.destilado_express.ventaservice.model.dto.ProductoDTO;
import com.destilado_express.ventaservice.model.dto.VentaDTO;
import com.destilado_express.ventaservice.repository.VentaProductoRepository;
import com.destilado_express.ventaservice.repository.VentaRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final VentaProductoRepository ventaProductoRepository;
    private final ProductoClient productoClient;

    @Autowired
    public VentaService(VentaRepository ventaRepository, VentaProductoRepository ventaProductoRepository,
            ProductoClient productoClient) {
        this.ventaRepository = ventaRepository;
        this.ventaProductoRepository = ventaProductoRepository;
        this.productoClient = productoClient;
    }

    // Obtener todas las ventas
    public List<Venta> obtenerVentas() {
        return ventaRepository.findAll();
    }

    // Obtener una venta por ID
    public Venta obtenerVentaPorId(Long ventaId) {
        return ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
    }

    // Obtener la venta activa del usuario
    public Venta obtenerVentaActivaPorUsuario(Long userId) {
        return ventaRepository.findByUserIdAndActivaTrue(userId)
                .orElseThrow(() -> new RuntimeException("No se encontró una venta activa para el usuario: " + userId));
    }

    // Crear una nueva venta
    public Venta crearVenta(Venta venta) {
        return ventaRepository.save(venta);
    }

    // Agregar un producto a la venta
    public VentaProducto agregarProducto(Long ventaId, VentaProducto producto) {
        Venta venta = getUpdatedVenta(ventaId);
        producto.setVenta(venta);
        producto.setMonto(producto.getPrecioUnidad() * producto.getCantidad());
        return ventaProductoRepository.save(producto);
    }

    // Actualizar un producto en la venta
    public VentaProducto actualizarProducto(Long ventaId, Long productoId, VentaProducto producto) {
        VentaProducto productoExistente = ventaProductoRepository.findByIdProductoAndVentaId(productoId, ventaId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado en la venta"));

        Venta venta = getUpdatedVenta(ventaId);
        productoExistente.setVenta(venta);

        productoExistente.setCantidad(producto.getCantidad());
        productoExistente.setPrecioUnidad(producto.getPrecioUnidad());
        productoExistente.setMonto(producto.getCantidad() * producto.getPrecioUnidad());

        return ventaProductoRepository.save(productoExistente);
    }

    // Eliminar un producto de la venta
    public void eliminarProducto(Long ventaId, Long productoId) {
        VentaProducto productoExistente = ventaProductoRepository.findByIdProductoAndVentaId(productoId, ventaId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado en la venta"));

        ventaProductoRepository.delete(productoExistente);
    }

    public VentaDTO toVentaDTO(Venta venta) {
        var ventaDTO = new VentaDTO();
        ventaDTO.setId(venta.getId());
        ventaDTO.setUserId(venta.getUserId());
        ventaDTO.setActiva(venta.getActiva());
        ventaDTO.setCreated(venta.getCreated());
        ventaDTO.setUpdated(venta.getUpdated());

        var prodList = new ArrayList<ProductoDTO>();
        for (VentaProducto vp : venta.getProductos()) {
            var producto = productoClient.obtenerDetalleProducto(vp.getIdProducto());
            prodList.add(producto);
        }

        ventaDTO.setProductos(prodList);

        return ventaDTO;
    }

    private Venta getUpdatedVenta(Long ventaId) {
        Venta venta = obtenerVentaPorId(ventaId);
        venta.setUpdated(LocalDateTime.now());
        return venta;
    }
}
