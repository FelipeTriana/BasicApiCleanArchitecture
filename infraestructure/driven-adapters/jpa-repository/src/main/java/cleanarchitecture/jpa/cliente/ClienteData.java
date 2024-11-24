package cleanarchitecture.jpa.cliente;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class ClienteData {
    @Id
    private String clienteDni;
    private String nombre;
    private String telefono;
    private String correo;
    private Double estatura;
    private Integer edad;

    @Embedded
    private ContactoFamiliarData contactoFamiliar;
}
