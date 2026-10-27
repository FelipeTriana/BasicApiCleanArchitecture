package cleanarchitecture.domain.user;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@Builder(toBuilder = true)
public class User {
    private final String id;
    private final String name;
    private final String lastName;
    private final String email;
    private final String passwordHash;
    private final Role role;
    private final boolean enabled;
    private final LocalDateTime createdAt;
}
