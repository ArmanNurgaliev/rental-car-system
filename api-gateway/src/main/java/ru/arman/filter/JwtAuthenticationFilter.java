package ru.arman.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.arman.utils.JwtTokenUtil;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter implements GatewayFilter {
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("JwtAuthenticationFilter | filter is working");

        ServerHttpRequest request = exchange.getRequest();

        final List<String> permitAll = List.of("/api/user/login", "/api/user/register","/eureka");
        final List<String> checkAdmin = List.of("/api/user/make-admin", "/api/vehicle/add");


        log.info("JwtAuthenticationFilter | filter | isPermittedApi : " + checkApi(request, permitAll));

        if (!checkApi(request, permitAll)) {
            if (!request.getHeaders().containsKey("Authorization")) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);

                return response.setComplete();
            }

            final String authorization = request.getHeaders().getOrEmpty("Authorization").get(0);
            final String token = authorization.replace("Bearer ", "");

            log.info("JwtAuthenticationFilter | filter | token : " + token);

            try {
                boolean isPermitted = checkApi(request, checkAdmin) ?
                        jwtTokenUtil.validateToken(token, "ROLE_ADMIN") :
                        jwtTokenUtil.validateToken(token, "ROLE_CUSTOMER");

                if (!isPermitted) {
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.FORBIDDEN);

                    return response.setComplete();
                }
            } catch (ExpiredJwtException e) {
                log.info("JwtAuthenticationFilter | filter | ExpiredJwtException | error : " + e.getMessage());
                ServerHttpResponse response = exchange.getResponse();

                response.setStatusCode(HttpStatus.UNAUTHORIZED);

                return response.setComplete();

            } catch (IllegalArgumentException | UnsupportedJwtException e) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.BAD_REQUEST);

                return response.setComplete();
            }

            exchange.getRequest().mutate().header("username", jwtTokenUtil.getUsernameFromToken(token)).build();
        }

        return chain.filter(exchange);
    }

    private boolean checkApi(ServerHttpRequest request, List<String> apis) {
        return apis.stream()
                .anyMatch(uri -> request.getURI().getPath().contains(uri));
    }
}
