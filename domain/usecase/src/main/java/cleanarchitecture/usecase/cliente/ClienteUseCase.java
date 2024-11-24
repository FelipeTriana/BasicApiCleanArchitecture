package cleanarchitecture.usecase.cliente;

import cleanarchitecture.domain.cliente.Cliente;
import cleanarchitecture.domain.cliente.gateway.ClienteGateway;

import java.util.List;

public class ClienteUseCase {

    private final ClienteGateway clientes;


    public ClienteUseCase(ClienteGateway cientes) {
        this.clientes = cientes;
    }

    public List<Cliente> findAll(){
        return clientes.findAll();
    }
}
