package vn.com.omart.sharedkernel.application.model.error;

public class BusinessException extends IllegalStateException {
    private String reason;

    public BusinessException(String reason, String message) {
        super(message);
        this.reason = reason;
    }

    public String reason() {
        return this.reason;
    }

    @Override
    public String toString() {
        return "[" + reason() + "] " + super.toString();
    }
}
