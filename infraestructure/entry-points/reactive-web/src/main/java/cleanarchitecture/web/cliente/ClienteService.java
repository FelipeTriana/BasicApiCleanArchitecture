package cleanarchitecture.web.cliente;

import cleanarchitecture.domain.cliente.Cliente;
import cleanarchitecture.usecase.cliente.ClienteUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteUseCase clienteUseCase;

    @GetMapping(path = "cliente")
    public List<Cliente> findAll(){
        List<Cliente> cliente = new ArrayList<>();
        clienteUseCase.findAll().forEach(c -> cliente.add(c));
        return cliente;
    }
}
