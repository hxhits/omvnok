package vn.com.omart.auth.application.response;

import lombok.Data;
import vn.com.omart.auth.domain.User;

import java.util.Date;

@Data
public class UserDTO {

    private String id;
    private String firstname;
    //private String lastname;
    private String name;
    private String avatar;
    private String phoneNumber;
    private String username;
    private String password;
    private String email;
    //private String type;
    private String title;
    private boolean activated;
    private String createdBy;
    private Date createdAt;
    private String updatedBy;
    private Date updatedAt;
    private String managedBy;
    private boolean isNew;

    public static UserDTO from(User model) {
        UserDTO dto = new UserDTO();

        dto.id = model.getId();
        dto.firstname = model.getFirstname();
        dto.name = model.getFirstname();
//        dto.lastname = model.getLastname();
        dto.avatar = model.getAvatar();
        dto.phoneNumber = model.getPhoneNumber();
        dto.username = model.getUsername();
        dto.password= model.getPassword();
        dto.email = model.getEmail();
        dto.title = model.getTitle();
        dto.activated = model.isActivated();
        dto.createdBy = model.getCreatedBy();
        dto.createdAt = model.getCreatedAt();
        dto.updatedBy = model.getUpdatedBy();
        dto.updatedAt = model.getUpdatedAt();
        dto.managedBy = model.getManageBy();

        return dto;
    }
}
