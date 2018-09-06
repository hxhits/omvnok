package vn.com.omart.auth.domain;

import vn.com.omart.sharedkernel.application.model.error.ApplicationException;

public class ActivationCodeExpireException extends ApplicationException {

    public ActivationCodeExpireException(String message) {
        super(message);
    }

}
