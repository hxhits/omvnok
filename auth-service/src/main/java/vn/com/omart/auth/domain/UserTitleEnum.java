package vn.com.omart.auth.domain;

import java.util.Arrays;

public enum UserTitleEnum {

    SUPERVISOR,
    SALESMAN,
    OWNER,
    USER,
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
