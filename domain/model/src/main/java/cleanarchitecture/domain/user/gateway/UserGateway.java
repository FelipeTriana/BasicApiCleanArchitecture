package cleanarchitecture.domain.user.gateway;

import cleanarchitecture.domain.user.User;
import reactor.core.publisher.Mono;

public interface UserGateway {
    Mono<User> findUserById(String id);
    Mono<User> saveUser(User user);
}
