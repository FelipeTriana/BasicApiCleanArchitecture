package cleanarchitecture.jpa.cliente;

import cleanarchitecture.domain.cliente.Cliente;
import cleanarchitecture.domain.cliente.ContactoFamiliar;
import cleanarchitecture.domain.cliente.gateway.ClienteGateway;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class ClienteRepositoryAdapter implements ClienteGateway {

    @Autowired
    private final ClienteDataRepository repository;

    @Override
    public List<Cliente> findAll() {
        Iterable<ClienteData> clienteDataIterable = repository.findAll();
        List<Cliente> clientes = new ArrayList<>();
        clienteDataIterable.forEach(clienteData -> clientes.add(mapToDomain(clienteData)));
        return clientes;
    }

    public static Cliente mapToDomain(ClienteData clienteData) {
        return Cliente.builder()
                .clienteDni(clienteData.getClienteDni())
                .nombre(clienteData.getNombre())
                .telefono(clienteData.getTelefono())
                .correo(clienteData.getCorreo())
                .estatura(clienteData.getEstatura())
                .edad(clienteData.getEdad())
                .contactoFamiliar(new ContactoFamiliar(
                        clienteData.getContactoFamiliar().getNombreContacto(),
                        clienteData.getContactoFamiliar().getTelefonoContacto()))
                .build();
    }
}
