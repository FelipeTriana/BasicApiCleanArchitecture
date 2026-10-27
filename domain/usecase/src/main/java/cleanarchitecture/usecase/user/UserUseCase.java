package cleanarchitecture.usecase.user;

import cleanarchitecture.domain.common.ex.BusinessException;
import cleanarchitecture.domain.user.Role;
import cleanarchitecture.domain.user.User;
import cleanarchitecture.domain.user.gateway.UserGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserGateway users;

    public Mono<User> findUser(String id){
        return users.findUserById(id).switchIfEmpty(Mono.error(BusinessException.Type.USER_NOT_EXIST.defer()));
    }

    public Mono<User> saveUser(User user){
        return users.saveUser(user);
    }

    public Flux<User> findAll(){
        return users.findAll();
    }

    public Mono<Void> deleteUser(String id){
        return users.deleteUser(id);
    }

    /**
     * Cambia el rol de un usuario existente.
     * Solo puede asignarse USER → ADMIN o ADMIN → USER.
     * La validación de que el llamante es ADMIN se hace en SecurityConfig.
     */
    public Mono<User> changeRole(String id, Role newRole) {
        return users.findUserById(id)
                .switchIfEmpty(Mono.error(BusinessException.Type.USER_NOT_EXIST.defer()))
                .flatMap(user -> users.updateRole(id, newRole));
    }
}
