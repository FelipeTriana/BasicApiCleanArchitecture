package cleanarchitecture.jpa.auth;

import cleanarchitecture.domain.user.Role;
import cleanarchitecture.domain.user.TokenClaims;
import cleanarchitecture.domain.user.User;
import cleanarchitecture.domain.user.gateway.TokenGateway;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtAdapter implements TokenGateway {

    private final SecretKey secretKey;
    private final long expirationMs;

    public JwtAdapter(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-seconds}") long expirationSeconds) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationSeconds * 1000L;
    }

    @Override
    public String generateToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole().name())
                .claim("userId", user.getId())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    @Override
    public Mono<TokenClaims> validateToken(String token) {
        return Mono.fromCallable(() -> {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return TokenClaims.builder()
                    .email(claims.getSubject())
                    .role(Role.valueOf(claims.get("role", String.class)))
                    .build();
        });
    }
}
