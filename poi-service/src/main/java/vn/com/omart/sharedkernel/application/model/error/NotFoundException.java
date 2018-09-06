package vn.com.omart.sharedkernel.application.model.error;

public class NotFoundException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    public NotFoundException(String message) {
        super(message);
    }

    @Override
    public ErrorCode errorCode() {
        return ErrorCode.NOT_FOUND;
    }

}
