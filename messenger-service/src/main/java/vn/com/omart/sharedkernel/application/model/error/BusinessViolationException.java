package vn.com.omart.sharedkernel.application.model.error;

public class BusinessViolationException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    public BusinessViolationException(String message) {
        super(message);
    }

    @Override
    public ErrorCode errorCode() {
        return ErrorCode.BUSINESS_RULE_VIOLATION;
    }

}
