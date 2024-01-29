package cleanarchitecture.domain.user.gateway;

import reactor.core.publisher.Mono;

/**
 * Interfaz usada para abstraer el envío de un comando específico a otro subdominio (Score de usuarios "empleados")
 */
public interface UserScoreGateway {

    Mono<Void> addPointsToUserScore(String userId, int points);

}
