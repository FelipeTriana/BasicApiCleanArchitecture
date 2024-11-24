package cleanarchitecture.domain.cliente;

import jakarta.persistence.Embedded;
import lombok.*;


@Getter
@EqualsAndHashCode
@Builder(toBuilder = true)
public class Cliente {
    private final String clienteDni;
    private final String nombre;
    private final String telefono;
    private final String correo;
    private final Double estatura;
    private final Integer edad;

    @Embedded
    private final ContactoFamiliar contactoFamiliar;
}