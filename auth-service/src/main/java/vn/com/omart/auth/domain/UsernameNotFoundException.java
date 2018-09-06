package vn.com.omart.auth.domain;

import org.springframework.security.core.AuthenticationException;
import vn.com.omart.sharedkernel.application.model.error.ApplicationException;

public class UsernameNotFoundException extends ApplicationException {

//    public UsernameNotFoundException(String msg, Throwable t) {
//        super(msg, t);
//    }

    public UsernameNotFoundException(String msg) {
        super(msg);
    }
}
