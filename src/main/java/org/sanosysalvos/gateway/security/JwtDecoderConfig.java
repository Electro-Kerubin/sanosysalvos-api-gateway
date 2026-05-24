package org.sanosysalvos.gateway.security;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

@Configuration
public class JwtDecoderConfig {

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder(@Value("${JWT_SECRET:c2Fub3N5c2Fsdm9zLXNlY3JldC1rZXktcGFyYS1qd3QtMjAyNg==}") String jwtSecret) {
        // El auth-service usa getBytes(UTF_8) sobre el string raw (48 bytes) → jjwt elige HS384.
        // NimbusReactiveJwtDecoder usa HS256 por defecto aunque la clave diga HmacSHA384,
        // por eso hay que indicar el algoritmo explícitamente con macAlgorithm().
        byte[] secretBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        SecretKey secretKey = new SecretKeySpec(secretBytes, "HmacSHA384");
        return NimbusReactiveJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS384)
                .build();
    }
}