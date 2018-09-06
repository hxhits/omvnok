package vn.com.omart.sharedkernel.application.model.error;

public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ApplicationException(String message) {
        super(message);
    }

    public ErrorCode errorCode() {
        return ErrorCode.UNKNOWN;
    }
}
