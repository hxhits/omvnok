package vn.com.omart.auth.application.request;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

@Data
@ToString
public class UserCmd {

    @Data
    @ToString
    public static class CreateOrUpdate {

        @NotBlank
        private String name;

        private String avatar;

        @NotBlank
        private String phoneNumber;

        //        @NotBlank
        private String password;

        private boolean activated;

        private boolean autoActivated;
    }

    @Data
    @ToString
    public static class CreateOrUpdateSalesman {

        @NotBlank
        private String name;

        private String avatar;

        @NotBlank
        private String phoneNumber;

        private String email;

        private boolean autoActivated;

//        @NotBlank
//        private String password;

//        private boolean actived;
    }

    @Data
    @ToString
    public static class CreateOrUpdate1 {

        private String manageBy;

        @NotBlank
        private String name;

        private String avatar;

        private String email;

        private String type;

        @NotBlank
        private String phoneNumber;

        //        @NotBlank
        private String password;

        private String actionBy;
    }

    @Data
    @ToString
    public static class UpdateProfile {

        private String name;
        private String avatar;
    }

    @Data
    @ToString
    public static class ChangePassword {
        @NotBlank
        private String currentPassword;
    }
}
