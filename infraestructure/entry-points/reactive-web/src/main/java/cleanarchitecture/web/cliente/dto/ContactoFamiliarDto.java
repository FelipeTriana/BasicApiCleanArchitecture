package cleanarchitecture.web.cliente.dto;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ContactoFamiliarDto {
    private String nombreContacto;
    private String telefonoContacto;
}
