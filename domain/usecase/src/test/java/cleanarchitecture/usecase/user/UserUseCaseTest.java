package cleanarchitecture.usecase.user;

import cleanarchitecture.domain.common.ex.BusinessException;
import cleanarchitecture.domain.user.User;
import cleanarchitecture.domain.user.gateway.UserGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserUseCaseTest {
    private UserGateway userGateway;
    private UserUseCase userUseCase;

    @BeforeEach
    public void setup() {
        userGateway = Mockito.mock(UserGateway.class);
        userUseCase = new UserUseCase(userGateway);
    }

    @Test
    public void testFindUser() {
        User user = User.builder().id("testId").name("testName").lastName("testLastName").build();
        when(userGateway.findUserById(any(String.class))).thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.findUser("testId"))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    public void testSaveUser() {
        User user = User.builder().id("testId").name("testName").lastName("testLastName").build();
        when(userGateway.saveUser(any(User.class))).thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.saveUser(user))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    public void testFindAll() {
        User user = User.builder().id("testId").name("testName").lastName("testLastName").build();
        when(userGateway.findAll()).thenReturn(Flux.just(user));

        StepVerifier.create(userUseCase.findAll())
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    public void testDeleteUser() {
        when(userGateway.deleteUser(any(String.class))).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.deleteUser("testId"))
                .verifyComplete();
    }

    @Test
    public void testFindUserNotFound() {
        when(userGateway.findUserById(any(String.class))).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.findUser("testId"))
                .expectError(BusinessException.class)
                .verify();
    }
}