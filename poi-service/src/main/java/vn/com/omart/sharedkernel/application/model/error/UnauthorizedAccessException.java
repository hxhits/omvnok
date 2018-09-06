package vn.com.omart.sharedkernel.application.model.error;

public class UnauthorizedAccessException extends ApplicationException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
