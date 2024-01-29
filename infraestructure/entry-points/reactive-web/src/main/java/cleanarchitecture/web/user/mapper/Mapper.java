package cleanarchitecture.web.user.mapper;

import cleanarchitecture.domain.user.User;
import cleanarchitecture.web.user.dto.UserDto;

public class Mapper {

    private Mapper() {
        throw new IllegalStateException("Utility class");
    }

    public static User toDomain(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .lastName(userDto.getLastName())
                .build();
    }

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .build();
    }

}
