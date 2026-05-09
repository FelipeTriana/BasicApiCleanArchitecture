package cleanarchitecture.jpa.user;

import cleanarchitecture.domain.user.User;
import cleanarchitecture.domain.user.gateway.AuthGateway;
import cleanarchitecture.domain.user.gateway.UserGateway;
import com.reactive.repository.jpa.AdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Repository
public class UserRepositoryAdapter extends AdapterOperations<User, UserData, String, UserDataRepository>
        implements UserGateway, AuthGateway {

    public UserRepositoryAdapter(UserDataRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.mapBuilder(d, User.UserBuilder.class).build());
    }

    @Override
    public Mono<User> findUserById(String id) {
        return findById(id);
    }

    @Override
    public Mono<User> saveUser(User user) {
        return save(user);
    }

    @Override
    public Flux<User> findAll() {
        return doQueryMany(repository::getUsers);
    }

    @Override
    public Mono<Void> deleteUser(String id) {
        return deleteById(id).then();
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return Mono.fromCallable(() -> repository.findByEmail(email))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(Mono::justOrEmpty)
                .map(this::toEntity);
    }
}

