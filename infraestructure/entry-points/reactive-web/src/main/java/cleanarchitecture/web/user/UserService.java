package cleanarchitecture.web.user;

import cleanarchitecture.usecase.user.UserUseCase;
import cleanarchitecture.web.user.dto.UserDto;
import cleanarchitecture.web.user.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserService {

    private final UserUseCase userUseCase;

    @GetMapping(path = "user/{id}")
    public Mono<UserDto> findUser(@PathVariable("id") String id){
        return userUseCase.findUser(id)
                .map(Mapper::toDto);
    }

}
