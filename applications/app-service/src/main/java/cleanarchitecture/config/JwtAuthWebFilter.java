package cleanarchitecture.config;

import cleanarchitecture.domain.user.gateway.TokenGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Stateless JWT authentication filter.
 *
 * For every request it:
 *  1. Extracts the Bearer token from the Authorization header.
 *  2. Delegates validation to TokenGateway (domain port → JwtAdapter impl).
 *  3. Builds a Spring Security Authentication with the correct GrantedAuthority.
 *  4. Writes it into ReactiveSecurityContextHolder so downstream filters/controllers
 *     can access the authenticated principal.
 *
 * If the header is absent or the token is invalid the filter simply lets the request
 * continue unauthenticated; the SecurityWebFilterChain decides whether to reject it.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthWebFilter implements WebFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenGateway tokenGateway;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            return chain.filter(exchange);
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        return tokenGateway.validateToken(token)
                .flatMap(claims -> {
                    List<SimpleGrantedAuthority> authorities = List.of(
                            new SimpleGrantedAuthority("ROLE_" + claims.getRole().name())
                    );
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    claims.getEmail(), null, authorities);

                    return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
                })
                .onErrorResume(e -> chain.filter(exchange));
    }
}
