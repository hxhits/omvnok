package vn.com.omart.messenger.application.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

public class PushNotificationCMD {

    @Data
    public static class Register{

        @NotBlank
        private String token;

    }
}
