package cleanarchitecture.web.user;

import cleanarchitecture.domain.user.User;
import cleanarchitecture.usecase.user.UserUseCase;
import cleanarchitecture.web.user.dto.UserDto;
import cleanarchitecture.web.user.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserService {

    private final UserUseCase userUseCase;

    @GetMapping(path = "user")
    public List<UserDto> findAll(){
        List<UserDto> userDtos = new ArrayList<>();
        userUseCase.findAll().forEach(user -> userDtos.add(Mapper.toDto(user)));
        return userDtos;
    }

}