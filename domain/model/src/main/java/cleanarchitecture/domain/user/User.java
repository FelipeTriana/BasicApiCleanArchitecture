package cleanarchitecture.domain.user;

import lombok.*;

@Getter
@EqualsAndHashCode
@Builder(toBuilder = true)
public class User {
    private final String id;
    private final String name;
    private final String lastName;
}
