package org.sanosysalvos.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;

import java.nio.charset.StandardCharsets;

@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @SuppressWarnings("null")
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (request.getHeaders().getContentType() != null && request.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON)) {
            return DataBufferUtils.join(request.getBody())
                    .defaultIfEmpty(exchange.getResponse().bufferFactory().wrap(new byte[0]))
                    .flatMap(dataBuffer -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        DataBufferUtils.release(dataBuffer);
                        String body = new String(bytes, StandardCharsets.UTF_8);
                        log.info("[Gateway] Incoming {} {} headers={} body={}", request.getMethod(), request.getURI(), request.getHeaders(), body);

                        ServerHttpRequest mutated = new ServerHttpRequestDecorator(request) {
                            @Override
                            public Flux<DataBuffer> getBody() {
                                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                                return Flux.just(buffer);
                            }
                        };

                        return chain.filter(exchange.mutate().request(mutated).build());
                    });
        }

        log.info("[Gateway] Incoming {} {} headers={}", request.getMethod(), request.getURI(), request.getHeaders());
        return chain.filter(exchange);
    }
}
