package cleanarchitecture.web.user.dto;

/** Body del PATCH /user/{id}/role — el admin envía el nuevo rol. */
public record ChangeRoleRequest(String role) {}
