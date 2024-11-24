package cleanarchitecture.web.cliente.mapper;


import cleanarchitecture.domain.cliente.Cliente;
import cleanarchitecture.domain.cliente.ContactoFamiliar;
import cleanarchitecture.web.cliente.dto.ClienteDto;

public class Mapper {

    private Mapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Cliente toDomain(ClienteDto clienteDto) {
        return Cliente.builder()
                .clienteDni(clienteDto.getClienteDni())
                .nombre(clienteDto.getNombre())
                .telefono(clienteDto.getTelefono())
                .correo(clienteDto.getCorreo())
                .estatura(clienteDto.getEstatura())
                .edad(clienteDto.getEdad())
                .contactoFamiliar(new ContactoFamiliar(
                        clienteDto.getContactoFamiliarDto().getNombreContacto(),
                        clienteDto.getContactoFamiliarDto().getTelefonoContacto()))
                .build();
    }

    public static ClienteDto toDto(Cliente cliente) {
        return ClienteDto.builder()
                .clienteDni(cliente.getClienteDni())
                .nombre(cliente.getNombre())
                .telefono(cliente.getTelefono())
                .correo(cliente.getCorreo())
                .estatura(cliente.getEstatura())
                .edad(cliente.getEdad())
                .contactoFamiliarDto(cleanarchitecture.web.cliente.dto.ContactoFamiliarDto.builder()
                        .nombreContacto(cliente.getContactoFamiliar().getNombreContacto())
                        .telefonoContacto(cliente.getContactoFamiliar().getTelefonoContacto())
                        .build())
                .build();
    }



}
