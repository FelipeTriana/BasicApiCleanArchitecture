package cleanarchitecture.web.user;

import cleanarchitecture.usecase.user.UserUseCase;
import cleanarchitecture.domain.user.User;
import cleanarchitecture.web.user.dto.UserDto;
import cleanarchitecture.web.user.mapper.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    private UserUseCase userUseCase;
    private UserService userService;
    private WebTestClient webTestClient;

    @BeforeEach
    public void setup() {
        userUseCase = Mockito.mock(UserUseCase.class);
        userService = new UserService(userUseCase);
        webTestClient = WebTestClient.bindToController(userService).build();
    }

    @Test
    public void testFindUser() {
        User user = User.builder().id("testId").name("testName").lastName("testLastName").build();
        when(userUseCase.findUser(any(String.class))).thenReturn(Mono.just(user));

        webTestClient.get()
                .uri("/user/{id}", "testId")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDto.class)
                .isEqualTo(Mapper.toDto(user));
    }

    @Test
    public void testFindAll() {
        User user = User.builder().id("testId").name("testName").lastName("testLastName").build();
        when(userUseCase.findAll()).thenReturn(Flux.just(user));

        webTestClient.get()
                .uri("/user")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserDto.class)
                .hasSize(1)
                .contains(Mapper.toDto(user));
    }

    @Test
    public void testSaveUser() {
        User user = User.builder().id("testId").name("testName").lastName("testLastName").build();
        when(userUseCase.saveUser(any(User.class))).thenReturn(Mono.just(user));

        webTestClient.post()
                .uri("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Mapper.toDto(user))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDto.class)
                .isEqualTo(Mapper.toDto(user));
    }

    @Test
    public void testDeleteUser() {
        when(userUseCase.deleteUser(any(String.class))).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/user/{id}", "testId")
                .exchange()
                .expectStatus().isOk();
    }
}