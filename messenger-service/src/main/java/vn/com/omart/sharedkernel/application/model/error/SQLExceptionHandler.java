package vn.com.omart.sharedkernel.application.model.error;


import org.springframework.http.ResponseEntity;
import vn.com.omart.sharedkernel.application.response.error.RestError;

import java.sql.SQLException;

/**
 * Spring MVC error handler for {@link SQLException}
 */
public interface SQLExceptionHandler {
    ResponseEntity<RestError> handle(SQLException sqlException);
}
