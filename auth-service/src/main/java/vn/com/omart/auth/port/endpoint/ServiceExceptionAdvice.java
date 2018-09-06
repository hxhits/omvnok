
package vn.com.omart.auth.port.endpoint;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.hibernate4.HibernateOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import vn.com.omart.auth.domain.*;
import vn.com.omart.sharedkernel.application.model.error.*;
import vn.com.omart.sharedkernel.application.response.error.RestError;

import java.lang.reflect.Field;
import java.sql.SQLException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Exception handler used when exception is thrown
 */
@ControllerAdvice
public class ServiceExceptionAdvice {

    private static final Logger LOG = getLogger(ServiceExceptionAdvice.class);

    @Autowired(required = false)
    private SQLExceptionHandler sqlExceptionHandler;

    @Autowired
    private MessageSource validationMessageSource;

    // ========================================================================
    // Client error (4xx)
    // ========================================================================

    @ResponseBody
    @ExceptionHandler(ActivationCodeExpireException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<RestError> onActivationCodeExpireException(ActivationCodeExpireException ex) {
        return responseEntityFromRestError(
            new RestError.Builder(401)
                .message(ex.getMessage())
                .error("ActivationCodeExpire", ex.getMessage())
                .build()
        );
    }

    @ResponseBody
    @ExceptionHandler(ActivationCodeInvalidException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<RestError> onActivationCodeNotValidException(ActivationCodeInvalidException ex) {
        return responseEntityFromRestError(
            new RestError.Builder(401)
                .message(ex.getMessage())
                .error("ActivationCodeInvalid", ex.getMessage())
                .build()
        );
    }

    @ResponseBody
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<RestError> onUsernameNotFoundException(UsernameNotFoundException ex) {
        return responseEntityFromRestError(
            new RestError.Builder(401)
                .message(UserExceptionConstant.UsernameNotFound.name())
                .error(UserExceptionConstant.UsernameNotFound.name(),
                    String.valueOf(UserExceptionConstant.UsernameNotFound.code()))
                .build()
        );
    }

    @ResponseBody
    @ExceptionHandler(UserNotActivatedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<RestError> onUserNotActivatedException(UserNotActivatedException ex) {
        return responseEntityFromRestError(
            new RestError.Builder(401)
                .message(UserExceptionConstant.UserNotActivatedException.name())
                .error(UserExceptionConstant.UserNotActivatedException.name(),
                    String.valueOf(UserExceptionConstant.UserNotActivatedException.code()))
                .build()
        );
    }

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<RestError> onNotFoundException(NotFoundException ex) {
        return responseEntityFromRestError(
            new RestError.Builder(404)
                .message(ex.getMessage())
                .error("notFound", ex.getMessage())
                .build()
        );
    }

    @ResponseBody
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<RestError> onAccessDeniedException(AccessDeniedException ex) {
        return responseEntityFromRestError(
            new RestError.Builder(403)
                .message(ex.getMessage())
                .error("notFound", ex.getMessage())
                .build()
        );
    }

    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<RestError> onMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        return responseEntityFromRestError(
            new RestError.Builder(405)
                .message(e.getMessage())
                .error("methodNotAllowed", e.getMessage(), "method", ErrorLocation.HEADER)
                .build()
        );
    }

    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RestError> onInvalidJsonException(HttpMessageNotReadableException e) {
        return responseEntityFromRestError(
            new RestError.Builder(HttpStatus.BAD_REQUEST.value())
                .message("Could not parse the request")
                .error("malformedPayload", "Malformed payload", "payload", ErrorLocation.REQUEST)
                .build()
        );
    }

//    @ResponseBody
//    @ExceptionHandler(ExceedRateLimitException.class)
//    public ResponseEntity<RestError> onRateLimitException(ExceedRateLimitException e) {
//        RestError body = new RestError.Builder(TOO_MANY_REQUESTS.value())
//                .message("Too many request")
//                .error("exceedRateLimit", "Exceed rate limit, please slow down", "request", REQUEST)
//                .build();
//
//        HttpHeaders headers = new HttpHeaders();
//        Date date = new Date();
//        headers.add("X-RateLimit-Window", e.getTimeWindow() + "");
//        headers.add("X-RateLimit-Limit", e.getMax() + "");
//        headers.add("X-RateLimit-Total", e.getTotal() + "");
//        headers.add("X-RateLimit-Reset", ((date.getTime() / 1000) + e.getSecondToReset()) + "");
//
//        return new ResponseEntity<>(body, headers, TOO_MANY_REQUESTS);
//    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RestError> onIllegalArgumentException(IllegalArgumentException ex) {
        // LOG.error("Illegal argument exception: {}", ex.getMessage());
        return responseEntityFromRestError(
            new RestError.Builder(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .message(ex.getMessage())
                .error("invalidParameter", ex.getMessage())
                .build()
        );
    }

    @ResponseBody
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<RestError> onIllegalStateException(IllegalStateException ex) {
        return responseEntityFromRestError(
            new RestError.Builder(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .message(ex.getMessage())
                .error("invalidParameter", ex.getMessage())
                .build());
    }

    @ResponseBody
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<RestError> onInvalidInputException(InvalidInputException ex) {
        return responseEntityFromRestError(
            new RestError.Builder(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .message(ex.getMessage())
                .error("invalidParameter", ex.getMessage())
                .build());
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestError> MethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        if (!bindingResult.hasErrors()) {
            throw new RuntimeException("Validation failure is reported but no error is found");
        }

        // Build details error message
        RestError.Builder errorBuilder = new RestError.Builder(HttpStatus.UNPROCESSABLE_ENTITY.value());
        errorBuilder.message("Invalid parameter value");
        for (FieldError error : bindingResult.getFieldErrors()) {
            errorBuilder.error(
                "invalidParameter",
                this.validationMessageSource.getMessage(error, null),
                resolveFieldName(error.getField(), ex.getParameter()),
                ErrorLocation.BODY);
        }

        return responseEntityFromRestError(errorBuilder.build());
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<RestError> ConversionFailedException(MethodArgumentTypeMismatchException exception) {
        RestError.Builder errorBuilder = new RestError.Builder(HttpStatus.UNPROCESSABLE_ENTITY.value());
        errorBuilder.message("Invalid parameter value");

        MethodParameter methodParameter = exception.getParameter();
        String location = methodParameter.getParameterName();
        String errorDetailMessage = exception.getRootCause().getMessage();
        ErrorLocation locationType = null;
        if (methodParameter.hasParameterAnnotation(RequestParam.class)) {
            RequestParam annotation = methodParameter.getParameterAnnotation(RequestParam.class);
            locationType = ErrorLocation.QUERY_STRING;
            // Use parameter name declared in annotation instead of parameter name if any
            if (!StringUtils.isEmpty(annotation.value())) {
                location = annotation.value();
            } else if (!StringUtils.isEmpty(annotation.name())) {
                location = annotation.name();
            }
        } else if (methodParameter.hasParameterAnnotation(PathVariable.class)) {
            PathVariable annotation = methodParameter.getMethodAnnotation(PathVariable.class);
            locationType = ErrorLocation.PATH_VARIABLE;
            // Use parameter name declared in annotation instead of parameter name if any
            if (!StringUtils.isEmpty(annotation.value())) {
                location = annotation.value();
            }
        }

        errorBuilder.error("invalidParameter", errorDetailMessage, location, locationType);
        return responseEntityFromRestError(errorBuilder.build());
    }

    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<RestError> missingRequireParam(MissingServletRequestParameterException exception) {
        String location = exception.getParameterName();
        String errorDetailMessage = "Parameter '" + location + "' is required";
        RestError.Builder errorBuilder = new RestError.Builder(HttpStatus.UNPROCESSABLE_ENTITY.value());
        errorBuilder.message("Invalid parameter value");
        errorBuilder.error("invalidParameter", errorDetailMessage, location, ErrorLocation.QUERY_STRING);
        return responseEntityFromRestError(errorBuilder.build());
    }

    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<RestError> onBusinessException(BusinessException businessException) {
        return responseEntityFromRestError(
            new RestError.Builder(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .message("Violate business rule")
                .error(businessException.reason(), businessException.getMessage())
                .build());
    }

    @ResponseBody
    @ExceptionHandler(TranslatableBusinessException.class)
    public ResponseEntity<RestError> onBusinessException(TranslatableBusinessException businessException) {
        return responseEntityFromRestError(
            new RestError.Builder(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .message("Violate business rule")
                .translatableError(businessException.reason(), businessException.getMessage(), businessException.messageVariables())
                .build());
    }

    @ResponseBody
    @ExceptionHandler(HibernateOptimisticLockingFailureException.class)
    public ResponseEntity<RestError> onOptimisticLockingException(HibernateOptimisticLockingFailureException e) {
        String message = "Object is being modified concurrently: '" +
            e.getPersistentClassName() + "'. " + "Please try to submit request later";

        return responseEntityFromRestError(
            new RestError.Builder(HttpStatus.CONFLICT.value())
                .message(message)
                .error("conflict", message)
                .build());
    }

    @ResponseBody
    @ExceptionHandler(org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException.class)
    public ResponseEntity<RestError> onOptimisticLockingException(org.springframework.orm.hibernate5.HibernateOptimisticLockingFailureException e) {
        String message = "Object is being modified concurrently: '" +
            e.getPersistentClassName() + "'. " + "Please try to submit request later";

        return responseEntityFromRestError(
            new RestError.Builder(HttpStatus.CONFLICT.value())
                .message(message)
                .error("conflict", message)
                .build());
    }

    @ResponseBody
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<RestError> onDataIntegrityException(DataIntegrityViolationException ex) {

        // Exception thrown by Spring's JdbcTemplate
        if (ex.getCause() instanceof MySQLIntegrityConstraintViolationException) {
            return translateSQLException((MySQLIntegrityConstraintViolationException) ex.getCause());
        }
        // Exception thrown by hibernate
        else if (ex.getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException hibernateException = (ConstraintViolationException) ex.getCause();
            return translateSQLException(hibernateException.getSQLException());
        }

        return responseEntityFromRestError(
            new RestError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Oops, something wrong with the server")
                .error("dataIntegrity", ex.getMessage())
                .build());
    }

    private ResponseEntity<RestError> translateSQLException(SQLException sqlException) {
        // Use custom sql exception handler if any
        if (this.sqlExceptionHandler != null) {
            return this.sqlExceptionHandler.handle(sqlException);
        }

        // Return default error response for sql exception
        return responseEntityFromRestError(
            new RestError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Oops, something wrong with the server")
                .error("dataAccess", sqlException.getMessage())
                .build());
    }


    private String resolveFieldName(String field, MethodParameter object) {

        try {
            // Check if field has an alternative json name
            Field refField = object.getClass().getDeclaredField(field);
            JsonProperty annotation = refField.getAnnotation(JsonProperty.class);

            if (annotation != null) {
                if (!StringUtils.isEmpty(annotation.value())) {
                    return annotation.value();
                }
            }

        } catch (NoSuchFieldException | SecurityException ignored) {
        }

        // Default return input field name
        return field;
    }

    // ========================================================================
    // Server error (5xx)
    // ========================================================================

    @ResponseBody
    @ExceptionHandler(BusinessViolationException.class)
    public ResponseEntity<RestError> onBusinessException(BusinessViolationException ex) {
        return responseEntityFromRestError(
            new RestError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Oops, something wrong with the server")
                .error("serverError", ex.getMessage())
                .build());
    }

    /**
     * Catch HttpClientErrorException and forward response to client
     */
    @ResponseBody
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity onHttpClientErrorException(HttpClientErrorException ex) {
        return new ResponseEntity<>(
            ex.getResponseBodyAsString(),
            ex.getResponseHeaders(),
            ex.getStatusCode());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestError> onUnknownException(Exception ex) {
        LOG.error("Unknown exception", ex);
        return responseEntityFromRestError(
            new RestError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Oops, something wrong with the server")
                .error("serverError", ex.getMessage())
                .build());
    }

    private ResponseEntity<RestError> responseEntityFromRestError(RestError error) {
        return new ResponseEntity<>(error, HttpStatus.valueOf(error.getCode()));
    }

}
