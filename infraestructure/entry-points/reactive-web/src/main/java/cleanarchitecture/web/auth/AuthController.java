package cleanarchitecture.web.auth;

import cleanarchitecture.usecase.auth.AuthUseCase;
import cleanarchitecture.web.auth.dto.AuthResponse;
import cleanarchitecture.web.auth.dto.LoginRequest;
import cleanarchitecture.web.auth.dto.RegisterRequest;
import cleanarchitecture.web.auth.mapper.AuthMapper;
import cleanarchitecture.web.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUseCase authUseCase;

    @Value("${jwt.expiration-seconds:86400}")
    private long expirationSeconds;

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserDto> register(@RequestBody RegisterRequest request) {
        return authUseCase.register(AuthMapper.toDomain(request), request.getPassword())
                .map(AuthMapper::toUserDto);
    }

    @PostMapping("login")
    public Mono<AuthResponse> login(@RequestBody LoginRequest request) {
        return authUseCase.login(request.getEmail(), request.getPassword())
                .map(token -> AuthMapper.toAuthResponse(token, expirationSeconds));
    }
}
