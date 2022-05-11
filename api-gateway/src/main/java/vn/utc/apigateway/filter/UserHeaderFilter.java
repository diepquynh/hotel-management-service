package vn.utc.apigateway.filter;

import com.google.common.base.Strings;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.file.AccessDeniedException;

@Component
public class UserHeaderFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() != null)
                .flatMap(c -> {
                    JwtAuthenticationToken jwt = (JwtAuthenticationToken) c.getAuthentication();

                    String username = (String) jwt.getTokenAttributes().get("user");
                    if (Strings.isNullOrEmpty(username)) {
                        return Mono.error(
                                new AccessDeniedException("Invalid token. User is not present in token.")
                        );
                    }

                    ServerHttpRequest request = exchange.getRequest().mutate()
                            .header("X-Username", username).build();

                    return chain.filter(exchange.mutate().request(request).build());
                })
                .switchIfEmpty(chain.filter(exchange));
    }
}
