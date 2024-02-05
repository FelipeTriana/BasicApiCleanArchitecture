package cleanarchitecture.usecase.user;

import cleanarchitecture.domain.common.ex.BusinessException;
import cleanarchitecture.domain.user.User;
import cleanarchitecture.domain.user.gateway.UserGateway;
import lombok.RequiredArgsConstructor;
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

}
