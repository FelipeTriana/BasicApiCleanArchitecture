package cleanarchitecture.domain.user.gateway;

import cleanarchitecture.domain.user.TokenClaims;
import cleanarchitecture.domain.user.User;
import reactor.core.publisher.Mono;

public interface TokenGateway {
    String generateToken(User user);
    Mono<TokenClaims> validateToken(String token);
}
