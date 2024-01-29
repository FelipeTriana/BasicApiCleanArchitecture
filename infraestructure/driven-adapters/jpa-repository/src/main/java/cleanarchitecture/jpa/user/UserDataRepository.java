package cleanarchitecture.jpa.user;


import cleanarchitecture.domain.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface UserDataRepository extends CrudRepository<UserData, String>, QueryByExampleExecutor<UserData> {

    User findUserById(String id);

}
