package cleanarchitecture.web.cliente;

import cleanarchitecture.domain.cliente.Cliente;
import cleanarchitecture.usecase.cliente.ClienteUseCase;
import cleanarchitecture.web.cliente.dto.ClienteDto;
import cleanarchitecture.web.cliente.mapper.Mapper;
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
    public List<ClienteDto> findAll(){
        List<ClienteDto> clienteDto = new ArrayList<>();
        clienteUseCase.findAll().forEach(c -> clienteDto.add(Mapper.toDto(c)));
        return clienteDto;
    }
}
