package cleanarchitecture.config;


import cleanarchitecture.domain.user.User;
import cleanarchitecture.domain.user.gateway.UserGateway;
import org.reactivecommons.utils.ObjectMapper;
import org.reactivecommons.utils.ObjectMapperImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Constructor;

@Configuration
@ComponentScan(basePackages = "cleanarchitecture.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCaseConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapperImp();
    }

    @Bean
    public UserGateway userGateway() {
        return new UserGateway() {
            @Override
            public Mono<User> findUserById(String id) {
                // Fake implementation
                return Mono.just(createFakeUser("fakeId", "fakeName", "fakeLastName"));
            }

            @Override
            public Mono<User> saveUser(User user) {
                // Fake implementation
                return Mono.just(user);
            }

            @Override
            public Flux<User> findAll() {
                // Fake implementation
                return Flux.just(createFakeUser("fakeId1", "fakeName1", "fakeLastName1"),
                        createFakeUser("fakeId2", "fakeName2", "fakeLastName2"));
            }

            @Override
            public Mono<Void> deleteUser(String id) {
                // Fake implementation
                return Mono.empty();
            }

            private User createFakeUser(String id, String name, String lastName) {
                try {
                    Constructor<User> constructor = User.class.getDeclaredConstructor(String.class, String.class, String.class);
                    constructor.setAccessible(true);
                    return constructor.newInstance(id, name, lastName);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to create User instance", e);
                }
            }
        };
    }

}
