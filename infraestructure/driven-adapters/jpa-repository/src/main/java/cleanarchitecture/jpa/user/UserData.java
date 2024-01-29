package cleanarchitecture.jpa.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class UserData {
    @Id
    private String id;
    private String name;
    private String lastName;

}
