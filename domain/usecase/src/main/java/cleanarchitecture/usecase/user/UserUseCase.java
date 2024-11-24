package cleanarchitecture.usecase.user;

import cleanarchitecture.domain.user.User;
import cleanarchitecture.domain.user.gateway.UserGateway;
import lombok.RequiredArgsConstructor;

import java.util.List;



public class UserUseCase {

    private final UserGateway users;

    public UserUseCase(UserGateway users) {
        this.users = users;
    }

    public User findUser(String id){
        return users.findUserById(id);
    }

    public User saveUser(User user){
        return users.saveUser(user);
    }

    public List<User> findAll(){
        return users.findAll();
    }

    public void deleteUser(String id){
        users.deleteUser(id);
    }

}
