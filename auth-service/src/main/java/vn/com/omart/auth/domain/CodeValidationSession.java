package vn.com.omart.auth.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CodeValidationSession {
    private Integer validationCode;
    private boolean isValidate;

    public CodeValidationSession() {
    }

    public CodeValidationSession(Integer validationCode) {
        this.validationCode = validationCode;
        this.isValidate = false;
    }
}
