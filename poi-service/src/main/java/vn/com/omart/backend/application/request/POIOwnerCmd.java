package vn.com.omart.backend.application.request;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

@Data
@ToString
public class POIOwnerCmd {

    @Data
    @ToString
    public static class CreateOrUpdate {

        @NotBlank
        private String name;

        private String avatar;

        @NotBlank
        private String phoneNumber;

        //@NotBlank
        private String password;
        
        private boolean activated;
    }

    @Data
    @ToString
    public static class ChangePassword {

        @NotBlank
        private String password;
        
        @NotBlank
        private String currentPassword;
    }

    @Data
    @ToString
    public static class UpdateAvatar {

        @NotBlank
        private String image;

    }

}
