package org.sanosysalvos.gateway;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import org.sanosysalvos.gateway.security.ApiGatewayApplication;

@SpringBootTest(classes = ApiGatewayApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiGatewayApplicationTests {

    @Autowired
    private WebTestClient webClient;

    @Test
    void contextLoads() {
        // El contexto del gateway arranca correctamente.
    }

    @Test
    void publicRoutesDoNotRequireAuthentication() {
        webClient.get().uri("/api/auth/login")
                .exchange()
                .expectStatus().value(statusCode -> assertNotEquals(401, statusCode));
    }

    @Test
    void protectedRoutesRequireAuthentication() {
        webClient.get().uri("/api/reportes/crear")
                .exchange()
                .expectStatus().isUnauthorized();
    }
}