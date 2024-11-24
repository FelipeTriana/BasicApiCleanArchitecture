package cleanarchitecture.jpa.user;

import cleanarchitecture.domain.user.User;
import cleanarchitecture.domain.user.gateway.UserGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static cleanarchitecture.jpa.user.Mapper.mapToDomain;

@Repository
public class UserRepositoryAdapter implements UserGateway {

    @Autowired
    private final UserDataRepository repository;


    public UserRepositoryAdapter(UserDataRepository repository) {
        this.repository = repository;
    }


    @Override
    public User findUserById(String id) {
        return User.builder().name("test").lastName("test").build();
    }

    @Override
    public User saveUser(User user) {
        return User.builder().name("test").lastName("test").build();
    }

    @Override
    public List<User> findAll() {
        Iterable<UserData> userDataIterable = repository.findAll();
        List<User> users = new ArrayList<>();
        userDataIterable.forEach(userData -> users.add(mapToDomain(userData)));
        return users;
    }


    @Override
    public void deleteUser(String id) {

    }


}
