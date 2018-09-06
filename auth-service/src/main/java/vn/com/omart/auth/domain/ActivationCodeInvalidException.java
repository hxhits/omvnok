package vn.com.omart.auth.domain;

import vn.com.omart.sharedkernel.application.model.error.ApplicationException;

public class ActivationCodeInvalidException extends ApplicationException {

    public ActivationCodeInvalidException(String message) {
        super(message);
    }

}
