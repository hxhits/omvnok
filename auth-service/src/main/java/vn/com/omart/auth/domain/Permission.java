package vn.com.omart.auth.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name = "oauth_permission")
@Data
public class Permission {

    @Id
    @NotNull
    @Size(min = 0, max = 50)
    @Column(name = "code", columnDefinition = "varchar")
    private String code;

    @Column(name = "name", columnDefinition = "varchar")
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;
}
