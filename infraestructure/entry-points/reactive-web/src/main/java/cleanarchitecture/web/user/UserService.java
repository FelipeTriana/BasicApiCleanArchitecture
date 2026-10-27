package cleanarchitecture.web.user;

import cleanarchitecture.domain.user.Role;
import cleanarchitecture.usecase.user.UserUseCase;
import cleanarchitecture.web.user.dto.ChangeRoleRequest;
import cleanarchitecture.web.user.dto.UserDto;
import cleanarchitecture.web.user.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
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

    @GetMapping(path = "user")
    public Flux<UserDto> findAll(){
        return userUseCase.findAll()
                .map(Mapper::toDto);
    }

    @PostMapping(path = "user")
    public Mono<UserDto> saveUser(@RequestBody UserDto user){
        return userUseCase.saveUser(Mapper.toDomain(user))
                .map(Mapper::toDto);
    }

    @DeleteMapping(path = "user/{id}")
    public Mono<Void> deleteUser(@PathVariable("id") String id){
        return userUseCase.deleteUser(id);
    }

    /**
     * PATCH /user/{id}/role
     * Solo ADMIN puede invocar este endpoint (ver SecurityConfig).
     * Permite cambiar el rol de cualquier usuario (USER ↔ ADMIN).
     */
    @PatchMapping(path = "user/{id}/role")
    public Mono<ResponseEntity<UserDto>> changeRole(
            @PathVariable("id") String id,
            @RequestBody ChangeRoleRequest request) {
        Role newRole = Role.valueOf(request.role().toUpperCase());
        return userUseCase.changeRole(id, newRole)
                .map(user -> ResponseEntity.ok(Mapper.toDto(user)));
    }
}
