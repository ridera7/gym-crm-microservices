package com.gateway.service.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class AuthenticationFilter implements GatewayFilterFactory<AuthenticationFilter.Config> {

    private static final List<String> EXCLUDED_URLS = List.of(
            "/auth/api/v1/login",
            "/auth/api/v1/validate-token",
            "/gym-crm-main-app/api/v1/trainee/register",
            "/gym-crm-main-app/api/v1/trainer/register"
            );
    private final WebClient.Builder webClientBuilder;

    public AuthenticationFilter(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String requestPath = request.getURI().getPath();

            if (isExcludedPath(requestPath)) {
                return chain.filter(exchange);
            }

            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (token == null || !token.startsWith("Bearer ")) {
                return onError(exchange, "Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
            }

            return webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8080/auth/api/v1/validate-token")
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response -> Mono.error(new RuntimeException("Invalid token")))
                    .bodyToMono(Void.class)
                    .then(chain.filter(exchange))
                    .onErrorResume(error -> onError(exchange, error.getMessage(), HttpStatus.UNAUTHORIZED));
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        byte[] bytes = err.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);

        return response.writeWith(Mono.just(buffer));
    }

    private boolean isExcludedPath(String path) {
        return EXCLUDED_URLS.stream().anyMatch(path::startsWith);
    }

    @Override
    public Class<Config> getConfigClass() {
        return Config.class;
    }

    public static class Config {
    }
}
