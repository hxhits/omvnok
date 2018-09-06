package vn.com.omart.sharedkernel.application.model.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import vn.com.omart.sharedkernel.application.response.error.RestError;

import java.sql.SQLException;

/**
 * MySQL implementation of {@link SQLExceptionHandler}
 */
public class MySQLExceptionHandler implements SQLExceptionHandler {

    @Override
    public ResponseEntity<RestError> handle(SQLException sqlException) {
        // String sqlState = sqlException.getSQLState();
        int errorCode = sqlException.getErrorCode();

        String message;
        switch (errorCode) {
            case 1062:
                message = "Duplicated request, please submit another request";
                return new ResponseEntity<>(
                    new RestError.Builder(HttpStatus.CONFLICT.value())
                        .message(message)
                        .error("duplicate", message, "payload", ErrorLocation.REQUEST)
                        .build(),
                    HttpStatus.CONFLICT);
            default:
                return new ResponseEntity<>(
                    new RestError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("Unknown error occurs while processing request")
                        .error("serverError", "Server Error")
                        .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
