package vn.com.omart.backend.constants;

import java.util.Arrays;

public enum UserTitleEnum {

    SUPERVISOR,
    SALESMAN,
    OWNER,
    USER,
    SHIPPER,
    DIRECTOR,
    ADMINISTRATOR;

    public static UserTitleEnum from(String text) {
        try {
            return UserTitleEnum.valueOf(text.toUpperCase());
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new IllegalArgumentException("'" + (text == null ? "null" : text) +
                "' is not a valid User type. Valid values are " + Arrays.asList(UserTitleEnum.values()));
        }
    }
}
