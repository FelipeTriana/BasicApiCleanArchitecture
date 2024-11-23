package cleanarchitecture.config;


import cleanarchitecture.domain.user.User;
import cleanarchitecture.domain.user.gateway.UserGateway;
import org.reactivecommons.utils.ObjectMapper;
import org.reactivecommons.utils.ObjectMapperImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

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
            public User findUserById(String id) {
                // Fake implementation
                return createFakeUser("fakeId", "fakeName", "fakeLastName");
            }

            @Override
            public User saveUser(User user) {
                // Fake implementation
                return user;
            }

            @Override
            public List<User> findAll() {
                // Fake implementation
                return Arrays.asList(
                        createFakeUser("fakeId1", "fakeName1", "fakeLastName1"),
                        createFakeUser("fakeId2", "fakeName2", "fakeLastName2")
                );
            }

            @Override
            public void deleteUser(String id) {
                // Fake implementation
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
