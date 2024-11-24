package cleanarchitecture.web.cliente.mapper;


import cleanarchitecture.domain.cliente.Cliente;
import cleanarchitecture.domain.cliente.ContactoFamiliar;
import cleanarchitecture.web.cliente.dto.ClienteDto;

public class Mapper {

    private Mapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Cliente toDomain(ClienteDto clienteData) {
        return Cliente.builder()
                .clienteDni(clienteData.getClienteDni())
                .nombre(clienteData.getNombre())
                .telefono(clienteData.getTelefono())
                .correo(clienteData.getCorreo())
                .estatura(clienteData.getEstatura())
                .edad(clienteData.getEdad())
                .contactoFamiliar(new ContactoFamiliar(
                        clienteData.getContactoFamiliarDto().getNombreContacto(),
                        clienteData.getContactoFamiliarDto().getTelefonoContacto()))
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
