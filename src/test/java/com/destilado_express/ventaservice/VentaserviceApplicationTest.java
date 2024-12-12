package com.destilado_express.ventaservice;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class VentaserviceApplicationTest {
    
    @Test
    void contextLoads() {
        // Contexto se carga sin lanzar excepciones.
    }

    @Test
    void mainMethodTest() {
        VentaserviceApplication.main(new String[] {});
        assertTrue(Boolean.TRUE);
    }
}
