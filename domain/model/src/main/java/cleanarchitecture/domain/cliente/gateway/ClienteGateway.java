package cleanarchitecture.domain.cliente.gateway;

import cleanarchitecture.domain.cliente.Cliente;

import java.util.List;

public interface ClienteGateway {
    List<Cliente> findAll();

}
