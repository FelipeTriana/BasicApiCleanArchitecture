package cleanarchitecture.domain.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenClaims {
    private final String email;
    private final Role role;
}
