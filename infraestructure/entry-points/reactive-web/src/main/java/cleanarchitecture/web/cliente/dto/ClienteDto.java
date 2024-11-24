package cleanarchitecture.web.cliente.dto;

import jakarta.persistence.Embedded;
import lombok.*;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class ClienteDto {
    private final String clienteDni;
    private final String nombre;
    private final String telefono;
    private final String correo;
    private final Double estatura;
    private final Integer edad;

    @Embedded
    private final ContactoFamiliarDto contactoFamiliarDto;
}