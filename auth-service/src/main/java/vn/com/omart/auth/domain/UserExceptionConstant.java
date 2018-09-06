package vn.com.omart.auth.domain;

public enum UserExceptionConstant {
    UsernameNotFound(0),
    UserNotActivatedException(2);

    private int code;

    UserExceptionConstant(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }

}
