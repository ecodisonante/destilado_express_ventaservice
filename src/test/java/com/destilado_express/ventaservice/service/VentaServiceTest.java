package com.destilado_express.ventaservice.service;

import com.destilado_express.ventaservice.model.Venta;
import com.destilado_express.ventaservice.model.VentaProducto;
import com.destilado_express.ventaservice.model.dto.ProductoDTO;
import com.destilado_express.ventaservice.model.dto.VentaDTO;
import com.destilado_express.ventaservice.repository.VentaProductoRepository;
import com.destilado_express.ventaservice.repository.VentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private VentaProductoRepository ventaProductoRepository;

    @Mock
    private ProductoClient productoClient;

    @InjectMocks
    private VentaService ventaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void obtenerVentas() {
        List<Venta> ventas = new ArrayList<>();
        when(ventaRepository.findByActiva(false)).thenReturn(ventas);

        List<Venta> result = ventaService.obtenerVentas();

        assertEquals(ventas, result);
        verify(ventaRepository, times(1)).findByActiva(false);
    }

    @Test
    void obtenerVentaPorId() {
        Venta venta = new Venta();
        when(ventaRepository.findById(anyLong())).thenReturn(Optional.of(venta));

        Venta result = ventaService.obtenerVentaPorId(1L);

        assertEquals(venta, result);
        verify(ventaRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerVentaPorId_NotFound() {
        when(ventaRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ventaService.obtenerVentaPorId(1L));
    }

    @Test
    void obtenerVentaActivaPorUsuario() {
        Venta venta = new Venta();
        when(ventaRepository.findByUserIdAndActivaTrue(anyLong())).thenReturn(Optional.of(venta));

        Optional<Venta> result = ventaService.obtenerVentaActivaPorUsuario(1L);

        assertTrue(result.isPresent());
        assertEquals(venta, result.get());
        verify(ventaRepository, times(1)).findByUserIdAndActivaTrue(1L);
    }

    @Test
    void crearVenta() {
        Venta venta = new Venta();
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);

        Venta result = ventaService.crearVenta(venta);

        assertEquals(venta, result);
        verify(ventaRepository, times(1)).save(venta);
    }

    @Test
    void agregarProducto() {
        Venta venta = new Venta();
        VentaProducto producto = new VentaProducto();
        producto.setCantidad(10);
        producto.setPrecioUnidad(1234D);

        when(ventaRepository.findById(anyLong())).thenReturn(Optional.of(venta));
        when(ventaProductoRepository.save(any(VentaProducto.class))).thenReturn(producto);

        VentaProducto result = ventaService.agregarProducto(1L, producto);

        assertEquals(producto, result);
        verify(ventaRepository, times(1)).findById(1L);
        verify(ventaProductoRepository, times(1)).save(producto);
    }

    @Test
    void actualizarProducto() {
        Venta venta = new Venta();
        VentaProducto productoExistente = new VentaProducto();
        productoExistente.setCantidad(10);
        productoExistente.setPrecioUnidad(1234D);

        when(ventaRepository.findById(anyLong())).thenReturn(Optional.of(venta));
        when(ventaProductoRepository.findByIdProductoAndVentaId(anyLong(), anyLong())).thenReturn(Optional.of(productoExistente));
        when(ventaProductoRepository.save(any(VentaProducto.class))).thenReturn(productoExistente);

        VentaProducto result = ventaService.actualizarProducto(1L, 1L, productoExistente);

        assertEquals(productoExistente, result);
        verify(ventaProductoRepository, times(1)).findByIdProductoAndVentaId(1L, 1L);
        verify(ventaProductoRepository, times(1)).save(productoExistente);
    }

    @Test
    void finalizarVenta() {
        Venta venta = new Venta();
        when(ventaRepository.findById(anyLong())).thenReturn(Optional.of(venta));

        Venta result = ventaService.finalizarVenta(1L);

        assertFalse(result.getActiva());
        verify(ventaRepository, times(1)).findById(1L);
        verify(ventaRepository, times(1)).save(venta);
    }

    @Test
    void eliminarProducto() {
        VentaProducto productoExistente = new VentaProducto();
        when(ventaProductoRepository.findByIdProductoAndVentaId(anyLong(), anyLong())).thenReturn(Optional.of(productoExistente));
        doNothing().when(ventaProductoRepository).delete(any(VentaProducto.class));

        ventaService.eliminarProducto(1L, 1L);

        verify(ventaProductoRepository, times(1)).findByIdProductoAndVentaId(1L, 1L);
        verify(ventaProductoRepository, times(1)).delete(productoExistente);
    }

    @Test
    void toVentaDTO() {
        Venta venta = new Venta();
        venta.setId(1L);
        venta.setUserId(1L);
        venta.setActiva(true);
        venta.setCreated(LocalDateTime.now());
        venta.setUpdated(LocalDateTime.now());

        VentaProducto vp = new VentaProducto();
        vp.setIdProducto(1L);
        List<VentaProducto> productos = new ArrayList<>();
        productos.add(vp);
        venta.setProductos(productos);

        ProductoDTO productoDTO = new ProductoDTO();
        when(productoClient.obtenerDetalleProducto(anyLong())).thenReturn(productoDTO);

        VentaDTO result = ventaService.toVentaDTO(venta);

        assertEquals(venta.getId(), result.getId());
        assertEquals(venta.getUserId(), result.getUserId());
        assertEquals(venta.getActiva(), result.getActiva());
        assertEquals(venta.getCreated(), result.getCreated());
        assertEquals(venta.getUpdated(), result.getUpdated());
        assertEquals(1, result.getDetalle().size());
        assertEquals(productoDTO, result.getDetalle().get(0));
        verify(productoClient, times(1)).obtenerDetalleProducto(1L);
    }
}
