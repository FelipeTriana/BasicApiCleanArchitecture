package cleanarchitecture.jpa.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface UserDataRepository extends CrudRepository<UserData, String>, QueryByExampleExecutor<UserData> {

}
