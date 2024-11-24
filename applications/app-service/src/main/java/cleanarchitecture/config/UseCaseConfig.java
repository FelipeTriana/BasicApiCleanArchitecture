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



}
