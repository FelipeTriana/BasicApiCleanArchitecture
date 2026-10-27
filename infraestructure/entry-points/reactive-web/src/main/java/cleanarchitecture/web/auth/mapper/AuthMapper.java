package cleanarchitecture.web.auth.mapper;

import cleanarchitecture.domain.user.User;
import cleanarchitecture.web.auth.dto.AuthResponse;
import cleanarchitecture.web.auth.dto.RegisterRequest;
import cleanarchitecture.web.user.dto.UserDto;

public class AuthMapper {

    private AuthMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static User toDomain(RegisterRequest request) {
        return User.builder()
                .name(request.getName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole() != null ? user.getRole().name() : null)
                .build();
    }

    public static AuthResponse toAuthResponse(String token, long expiresInSeconds) {
        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(expiresInSeconds)
                .build();
    }
}
