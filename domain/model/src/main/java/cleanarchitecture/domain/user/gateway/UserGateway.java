package cleanarchitecture.domain.user.gateway;

import cleanarchitecture.domain.user.User;

import java.util.List;


public interface UserGateway {
    User findUserById(String id);
    User saveUser(User user);
    List<User> findAll();
    void deleteUser(String id);

}
