package vn.com.omart.auth.application.request;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

@Data
@ToString
public class AccountCmd {

    @Data
    @ToString
    public static class CreateOrUpdate {
        @NotBlank
        private String account;
        private boolean check_existed = false;
    }
}