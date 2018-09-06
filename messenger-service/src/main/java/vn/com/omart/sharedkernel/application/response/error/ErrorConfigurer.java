package vn.com.omart.sharedkernel.application.response.error;

import vn.com.omart.sharedkernel.application.model.error.ErrorLocation;

public class ErrorConfigurer {

    private String reason;
    private String message;
    private String location;
    private ErrorLocation locationType;

    public ErrorConfigurer reason(String reason) {
//        Asserts.notEmpty(reason, "Reason must not be empty");
        this.reason = reason;
        return this;
    }

    protected String reason() {
        return this.reason;
    }

    protected String message() {
        return this.message;
    }

    protected String location() {
        return this.location;
    }

    protected ErrorLocation locationType() {
        return this.locationType;
    }

    public ErrorConfigurer message(String message) {
        this.message = message;
        return this;
    }

    public ErrorConfigurer locationType(ErrorLocation locationType) {
        this.locationType = locationType;
        return this;
    }

    public ErrorConfigurer location(String location) {
        this.location = location;
        return this;
    }
}
