package vn.com.omart.auth.domain;

import org.springframework.security.core.AuthenticationException;
import vn.com.omart.sharedkernel.application.model.error.ApplicationException;

public class UserNotActivatedException extends ApplicationException {

//    public UserNotActivatedException(String msg, Throwable t) {
//        super(msg, t);
//    }

    public UserNotActivatedException(String msg) {
        super(msg);
    }
}
