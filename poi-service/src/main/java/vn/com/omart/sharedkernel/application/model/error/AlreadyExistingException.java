package vn.com.omart.sharedkernel.application.model.error;

/**
 * A value already existing.
 * 
 * @author Win10
 *
 */
public class AlreadyExistingException extends ApplicationException {

  public AlreadyExistingException(String message) {
    super(message);
  }

  @Override
  public ErrorCode errorCode() {
    return ErrorCode.ALREADY_EXISTING;
  }
}
