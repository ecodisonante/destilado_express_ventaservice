// package com.destilado_express.ventaservice.service;

// import com.destilado_express.ventaservice.model.dto.ProductoDTO;
// import okhttp3.mockwebserver.MockResponse;
// import okhttp3.mockwebserver.MockWebServer;
// import org.junit.jupiter.api.*;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContext;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.web.reactive.function.client.WebClient;
// import reactor.core.publisher.Mono;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.Mockito.*;

// class ProductoClientTest {

//     private MockWebServer mockWebServer;

//     @Mock
//     private SecurityContext securityContext;

//     @Mock
//     private Authentication authentication;

//     @InjectMocks
//     private ProductoClient productoClient;

//     @BeforeEach
//     void setUp() throws Exception {
//         MockitoAnnotations.openMocks(this);
//         mockWebServer = new MockWebServer();
//         mockWebServer.start();
//         SecurityContextHolder.setContext(securityContext);
//         when(securityContext.getAuthentication()).thenReturn(authentication);
//         when(authentication.getCredentials()).thenReturn("mockToken");

//         WebClient webClient = WebClient.builder()
//                 .baseUrl(mockWebServer.url("/").toString())
//                 .build();

//         productoClient = new ProductoClient(webClient);
//     }

//     @AfterEach
//     void tearDown() throws Exception {
//         mockWebServer.shutdown();
//     }

//     @Test
//     void obtenerDetalleProducto() throws InterruptedException {
//         ProductoDTO productoDTO = new ProductoDTO();
//         productoDTO.setId(1L);
//         productoDTO.setNombre("Producto 1");

//         mockWebServer.enqueue(new MockResponse()
//                 .setBody("{ \"id\": 1, \"nombre\": \"Producto 1\" }")
//                 .addHeader("Content-Type", "application/json"));

//         ProductoDTO result = productoClient.obtenerDetalleProducto(1L);

//         assertEquals(1L, result.getId());
//         assertEquals("Producto 1", result.getNombre());

//         // Verify that the request was made with the expected headers
//         assertEquals("/1", mockWebServer.takeRequest().getPath());
//         assertEquals("Bearer mockToken", mockWebServer.takeRequest().getHeader("Authorization"));
//     }
// }
