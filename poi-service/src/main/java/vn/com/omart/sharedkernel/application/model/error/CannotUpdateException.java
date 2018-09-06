package vn.com.omart.sharedkernel.application.model.error;

/**
 * Cannot update value.
 * 
 * @author Win10
 *
 */
public class CannotUpdateException extends ApplicationException {

  public CannotUpdateException(String message) {
    super(message);
  }

  @Override
  public ErrorCode errorCode() {
    return ErrorCode.CANNOT_UPDATE;
  }
}
