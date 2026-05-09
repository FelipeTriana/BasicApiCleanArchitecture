package cleanarchitecture.usecase.auth;

import cleanarchitecture.domain.common.ex.BusinessException;
import cleanarchitecture.domain.user.Role;
import cleanarchitecture.domain.user.User;
import cleanarchitecture.domain.user.gateway.AuthGateway;
import cleanarchitecture.domain.user.gateway.PasswordGateway;
import cleanarchitecture.domain.user.gateway.TokenGateway;
import cleanarchitecture.domain.user.gateway.UserGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class AuthUseCase {

    private final AuthGateway authGateway;
    private final UserGateway userGateway;
    private final TokenGateway tokenGateway;
    private final PasswordGateway passwordGateway;

    /**
     * Registers a new user with USER role.
     * The raw password is encoded here; it never enters the domain object as plain text.
     *
     * @param user        domain object with name, lastName and email already set
     * @param rawPassword plain-text password provided by the client
     * @return saved User (without password hash in public surface)
     */
    public Mono<User> register(User user, String rawPassword) {
        return authGateway.findByEmail(user.getEmail())
                .flatMap(existing -> Mono.<User>error(
                        BusinessException.Type.USER_ALREADY_EXISTS.defer().get()))
                .switchIfEmpty(Mono.defer(() ->
                        Mono.fromCallable(() -> passwordGateway.encode(rawPassword))
                                .subscribeOn(Schedulers.boundedElastic())
                                .flatMap(passwordHash -> {
                                    User newUser = user.toBuilder()
                                            .id(UUID.randomUUID().toString())
                                            .passwordHash(passwordHash)
                                            .role(Role.USER)
                                            .enabled(true)
                                            .createdAt(LocalDateTime.now())
                                            .build();
                                    return userGateway.saveUser(newUser);
                                })
                ));
    }

    /**
     * Authenticates a user and returns a signed JWT on success.
     * Credentials are verified with the PasswordGateway so BCrypt runs
     * on a bounded-elastic thread and never blocks the event loop.
     *
     * @param email       the user's email
     * @param rawPassword the plain-text password to verify
     * @return JWT token string
     */
    public Mono<String> login(String email, String rawPassword) {
        return authGateway.findByEmail(email)
                .switchIfEmpty(Mono.error(
                        BusinessException.Type.INVALID_CREDENTIALS.defer().get()))
                .flatMap(user -> {
                    if (!user.isEnabled()) {
                        return Mono.error(
                                BusinessException.Type.USER_DISABLED.defer().get());
                    }
                    return Mono.fromCallable(
                                    () -> passwordGateway.matches(rawPassword, user.getPasswordHash()))
                            .subscribeOn(Schedulers.boundedElastic())
                            .flatMap(matches -> matches
                                    ? Mono.fromCallable(() -> tokenGateway.generateToken(user))
                                            .subscribeOn(Schedulers.boundedElastic())
                                    : Mono.error(
                                    BusinessException.Type.INVALID_CREDENTIALS.defer().get()));
                });
    }
}
