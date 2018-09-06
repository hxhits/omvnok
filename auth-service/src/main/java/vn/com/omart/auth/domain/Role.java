package vn.com.omart.auth.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;


@Entity
@Table(name = "oauth_role")
@Data
public class Role {

    @Id
    @NotNull
    @Size(min = 0, max = 500)
    @Column(name = "name", columnDefinition = "varchar")
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @ManyToMany
    @JoinTable(
        name = "oauth_role_permission",
        joinColumns = @JoinColumn(name = "role_name"),
        inverseJoinColumns = @JoinColumn(name = "permission_code"))
    private Set<Permission> permissions;
}
