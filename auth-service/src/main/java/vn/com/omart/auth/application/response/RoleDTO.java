package vn.com.omart.auth.application.response;

import lombok.Data;
import vn.com.omart.auth.domain.Role;

@Data
public class RoleDTO {
    private String name;

    public RoleDTO(String name) {
        this.name = name;
    }

    public static RoleDTO from(Role model) {
        return new RoleDTO(model.getName());
    }
}
