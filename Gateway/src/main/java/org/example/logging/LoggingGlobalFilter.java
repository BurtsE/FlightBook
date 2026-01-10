package org.example.logging;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@Order(-1) // Run before other filters
public class LoggingGlobalFilter implements GlobalFilter {

    private static final Logger log = LoggerFactory.getLogger(LoggingGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().toString();
        String host = exchange.getRequest().getHeaders().getFirst("Host");

        long startTime = System.currentTimeMillis();

        return chain.filter(exchange)
                .doOnTerminate(() -> {
                    long duration = System.currentTimeMillis() - startTime;
                    int status = exchange.getResponse().getStatusCode() != null
                            ? exchange.getResponse().getStatusCode().value()
                            : 500;
                    log.info("Request: {} {}{} (Host: {}) → Status: {} | Duration: {}ms",
                            method,
                            host != null ? "http://" + host : "",
                            path,
                            host,
                            status,
                            duration);
                });
    }
}
