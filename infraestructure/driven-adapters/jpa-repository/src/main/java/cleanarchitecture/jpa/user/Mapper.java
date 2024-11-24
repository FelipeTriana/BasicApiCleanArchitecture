package cleanarchitecture.jpa.user;

import cleanarchitecture.domain.user.User;

public class Mapper {

    private Mapper() {
        throw new IllegalStateException("Utility class");
    }

    public static User mapToDomain(UserData userData) {
        return User.builder()
                .id(userData.getId())
                .name(userData.getName())
                .lastName(userData.getLastName())
                .build();
    }


}
