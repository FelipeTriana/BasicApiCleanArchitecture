package cleanarchitecture.domain.cliente;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ContactoFamiliar {
    private String nombreContacto;
    private String telefonoContacto;
}
