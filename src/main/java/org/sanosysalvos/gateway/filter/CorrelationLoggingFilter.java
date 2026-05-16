package org.sanosysalvos.gateway.filter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class CorrelationLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(CorrelationLoggingFilter.class);
    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String correlationId = resolveCorrelationId(exchange);
        Instant start = Instant.now();

        ServerHttpRequest request = exchange.getRequest().mutate()
                .header(CORRELATION_ID_HEADER, correlationId)
                .header(REQUEST_ID_HEADER, correlationId)
                .build();

        ServerWebExchange mutatedExchange = exchange.mutate().request(request).build();
        mutatedExchange.getResponse().beforeCommit(() -> {
            mutatedExchange.getResponse().getHeaders().set(CORRELATION_ID_HEADER, correlationId);
            mutatedExchange.getResponse().getHeaders().set(REQUEST_ID_HEADER, correlationId);
            return Mono.empty();
        });

        return chain.filter(mutatedExchange)
                .doFinally(signalType -> logRequest(mutatedExchange, correlationId, start));
    }

    private String resolveCorrelationId(ServerWebExchange exchange) {
        String correlationId = exchange.getRequest().getHeaders().getFirst(CORRELATION_ID_HEADER);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = exchange.getRequest().getHeaders().getFirst(REQUEST_ID_HEADER);
        }

        return (correlationId == null || correlationId.isBlank()) ? UUID.randomUUID().toString() : correlationId;
    }

    private void logRequest(ServerWebExchange exchange, String correlationId, Instant start) {
        HttpStatusCode status = exchange.getResponse().getStatusCode();
        long durationMs = Duration.between(start, Instant.now()).toMillis();
        String method = exchange.getRequest().getMethodValue();
        String path = exchange.getRequest().getPath().value();
        String statusValue = status != null ? String.valueOf(status.value()) : "500";

        log.info("[{}] {} {} -> {} ({} ms)", correlationId, method, path, statusValue, durationMs);
    }
}