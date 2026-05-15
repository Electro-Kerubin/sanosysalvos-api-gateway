package org.sanosysalvos.gateway;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiGatewayApplicationTests {

    @Autowired
    private WebTestClient webClient;

    @Test
    void contextLoads() {
        // Prueba básica (Sintaxis)
    }

    @Test
    void publicasintoken() {
        // Simulamos una petición al endpoint de Auth
        webClient.get().uri("/api/auth/login")
                .exchange()
                // Como no hay microservicio detrás durante el test, puede dar 503 (Servicio no disponible)
                // o 404 (No encontrado), pero LO IMPORTANTE es que la seguridad lo deje pasar
                // y NO devuelva un 401 (No autorizado).
                .expectStatus().value(statusCode -> assertNotEquals(401, statusCode));
    }

    @Test
    void privadasintoken() {
        // Simulamos un intento de acceder a un microservicio protegido sin enviar credenciales
        webClient.get().uri("/api/reportes/crear")
                .exchange()
                // La configuración de seguridad debe interceptarlo y bloquearlo inmediatamente
                .expectStatus().isUnauthorized();
    }
}