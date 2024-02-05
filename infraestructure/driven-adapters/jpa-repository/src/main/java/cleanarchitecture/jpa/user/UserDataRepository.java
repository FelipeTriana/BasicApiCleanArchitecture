package cleanarchitecture.jpa.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;

public interface UserDataRepository extends CrudRepository<UserData, String>, QueryByExampleExecutor<UserData> {

    public static final String QUERY = """
            select * from user_data""";

    //Cachear esta conexion y buscar por id sobre el resultado de la cache
    @Query(value = QUERY,
            nativeQuery = true)
    List<UserData> getUsers();
}
