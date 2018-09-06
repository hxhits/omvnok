package vn.com.omart.sharedkernel.application.model.error;

/**
 * Error location which helps client to locate the location of errors
 */
public enum ErrorLocation {

    REQUEST("request"),

    PARAMETER("parameter"),

    QUERY_STRING("queryString"),

    PATH_VARIABLE("pathVariable"),

    HEADER("header"),

    BODY("body"),

    REQUEST_ENTITY("requestEntity");

    private String camelCaseName;

    ErrorLocation(String camelCaseName) {
        this.camelCaseName = camelCaseName;
    }

    public String camelCaseName() {
        return this.camelCaseName;
    }

}
