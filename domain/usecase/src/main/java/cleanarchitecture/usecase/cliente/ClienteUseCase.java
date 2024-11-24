package cleanarchitecture.usecase.cliente;

import cleanarchitecture.domain.cliente.Cliente;
import cleanarchitecture.domain.cliente.gateway.ClienteGateway;

import java.util.List;

public class ClienteUseCase {

    private final ClienteGateway clientesGateway;

    public ClienteUseCase(ClienteGateway clientesGateway) {
        this.clientesGateway = clientesGateway;
    }


    public List<Cliente> findAll(){
        return clientesGateway.findAll();
    }
}
