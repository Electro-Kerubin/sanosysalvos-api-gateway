package org.sanosysalvos.gateway.security;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

@Configuration
public class JwtDecoderConfig {

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder(@Value("${JWT_SECRET:c2Fub3N5c2Fsdm9zLXNlY3JldC1rZXktcGFyYS1qd3QtMjAyNg==}") String jwtSecret) {
        // El auth-service deriva la clave con getBytes(UTF_8) sobre el string,
        // lo que con 48 caracteres produce una clave de 48 bytes → HS384.
        // No se debe Base64-decodificar aquí o se obtienen bytes distintos.
        byte[] secretBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = new SecretKeySpec(secretBytes, "HmacSHA384");
        return NimbusReactiveJwtDecoder.withSecretKey(secretKey).build();
    }
}