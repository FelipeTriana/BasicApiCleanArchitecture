package cleanarchitecture.domain.user.gateway;

public interface PasswordGateway {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
