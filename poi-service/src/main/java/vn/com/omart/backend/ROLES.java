package vn.com.omart.backend;

import java.util.Arrays;

public enum ROLES {

    FUNC_CATEGORY_GET_DETAIL,
    FUNC_CATEGORY_UPDATE,
    ROLE_FUNC_READ_BANNER,
    ROLE_SALES_GET_POI,
    ROLE_SALES_CREATE_POI,
    ROLE_SALES_UPDATE_POI,
    ROLE_SALES_DELETE_POI;

    public String role() {
        return "ROLE_" + this.name();
    }

    public static ROLES from(String text) {
        try {
            return ROLES.valueOf(text.toUpperCase());
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new IllegalArgumentException("'" + (text == null ? "null" : text) +
                "' is not a valid ROLES. Valid values are " + Arrays.asList(ROLES.values()));
        }
    }
}
