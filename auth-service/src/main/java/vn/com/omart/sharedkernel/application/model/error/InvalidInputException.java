package vn.com.omart.sharedkernel.application.model.error;

/**
 * Exception thrown when input arguments are not valid.
 */
public class InvalidInputException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    public InvalidInputException(String message) {
        super(message);
    }

    @Override
    public ErrorCode errorCode() {
        return ErrorCode.INPUT_VIOLATE_BUSINESS_RULE;
    }

}
