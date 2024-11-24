package cleanarchitecture.jpa.cliente;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
public class ContactoFamiliarData {
    private String nombreContacto;
    private String telefonoContacto;
}
