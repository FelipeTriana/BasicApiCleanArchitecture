package cleanarchitecture.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Spring Security configuration for a WebFlux (reactive) API.
 *
 * Design decisions:
 *  - CSRF disabled  → the API is stateless (JWT), so CSRF tokens are unnecessary.
 *  - Sessions       → STATELESS; no server-side session is created or used.
 *  - HTTP Basic / Form Login disabled → authentication is JWT-only.
 *  - CORS           → configured explicitly; never rely on the default open policy.
 *  - Security headers → Spring Security adds HSTS, X-Frame-Options,
 *                       X-Content-Type-Options automatically.
 *
 * Authorization matrix:
 *  POST  /auth/**       → public (register / login)
 *  GET   /user/{id}     → any authenticated user (USER or ADMIN)
 *  GET   /user          → ADMIN only
 *  POST  /user          → ADMIN only
 *  DELETE /user/**      → ADMIN only
 *  any other            → authenticated
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http,
            JwtAuthWebFilter jwtAuthWebFilter) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                // Stateless: never persist the SecurityContext in the session
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(auth -> auth
                        // Public endpoints
                        .pathMatchers("/auth/**").permitAll()
                        // Authenticated users can read their own profile
                        .pathMatchers(HttpMethod.GET, "/user/{id}").authenticated()
                        // Admin-only operations
                        .pathMatchers(HttpMethod.GET, "/user").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.POST, "/user").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/user/**").hasRole("ADMIN")
                        // Anything else requires authentication
                        .anyExchange().authenticated()
                )
                // Place the JWT filter before Spring's default authentication processing
                .addFilterAt(jwtAuthWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    /**
     * CORS configuration.
     * In production, replace allowedOriginPatterns with the exact frontend origin(s).
     */
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
