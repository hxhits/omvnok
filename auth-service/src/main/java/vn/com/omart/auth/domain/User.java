package vn.com.omart.auth.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "oauth_user")
@Data
public class User {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @Size(min = 0, max = 500)
    private String id;

    @Column(name = "manage_by", columnDefinition = "varchar")
    @Size(min = 0, max = 500)
    private String manageBy;

    @Size(min = 0, max = 255)
    @Column(name = "firstname", columnDefinition = "varchar")
    private String firstname;

    @Size(min = 0, max = 255)
    @Column(name = "lastname", columnDefinition = "varchar")
    private String lastname;

    @Size(min = 0, max = 1000)
    @Column(name = "avatar", columnDefinition = "text")
    private String avatar;

    @Size(min = 0, max = 20)
    @Column(name = "phone_number", columnDefinition = "varchar", unique = true)
    private String phoneNumber;

    @Size(min = 0, max = 50)
    @Column(name = "username", columnDefinition = "varchar", unique = true)
    private String username;

    @Size(min = 0, max = 500)
    @Column(name = "password", columnDefinition = "text")
    private String password;

    @Email
    @Size(min = 0, max = 50)
    @Column(name = "email", columnDefinition = "varchar")
    private String email;

    @Column(name = "title", columnDefinition = "varchar")
    private String title;

    @Column(name = "activated", columnDefinition = "bit")
    private boolean activated;

    @Column(name = "created_by", columnDefinition = "varchar")
    private String createdBy;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private Date createdAt;

    @Column(name = "updated_by", columnDefinition = "varchar")
    private String updatedBy;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    private Date updatedAt;
    
    @Column(name = "activation_code", columnDefinition = "int")
    private Integer activationCode;

    @Column(name = "last_sent", columnDefinition = "LONG")
    private Long lastSent;

    @Column(name = "is_verified", columnDefinition = "bit")
    private boolean isVerified;

//    @Size(min = 0, max = 45)
//    @Column(name = "activation_code", columnDefinition = "varchar")
//    private String activationCode;

//    @Size(min = 0, max = 100)
//    @Column(name = "resetpasswordkey")
//    private String resetPasswordKey;

//    @ManyToMany
//    @JoinTable(
//        name = "oauth_user_authority",
//        joinColumns = @JoinColumn(name = "user_id"),
//        inverseJoinColumns = @JoinColumn(name = "authority"))
//    private Set<Authority> authorities;

    @ManyToMany
    @JoinTable(
        name = "oauth_user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_name"))
    private Set<Role> roles;

    public User deactivate() {
        this.activated = false;
        return this;
    }
    
    public User reactivate() {
        this.activated = true;
        return this;
    }

    public User setPasswordAndActive(String password) {
        this.activated = true;
        this.password = password;
        return this;
    }

    public User changePassword(String password) {
        this.password = password;
        return this;
    }

    public User updateFirstname(String name) {
        this.firstname = name;
        return this;
    }
    
    public User updateAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }
}
