package vn.com.omart.backend.port.adapter.userprofile;

import lombok.Data;

import java.util.Date;

@Data
public class UserResponse {

    private String id;
    private String firstname;
    private String lastname;
    private String avatar;
    private String phoneNumber;
    private String username;
    private String password;
    private String email;
    private String type;
    private String title;
    private boolean activated;
    private String createdBy;
    private Date createdAt;
    private String updatedBy;
    private Date updatedAt;
    private String managedBy;
    private boolean isNew;

}
