package vn.com.omart.messenger.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import vn.com.omart.messenger.application.response.ErrorResponse;
import vn.com.omart.messenger.common.constant.MessengerResponse;
import vn.com.omart.messenger.exception.MessengerException;

/**
 * Advice if there is any exception occurs in the rest API. It will transform
 * exception into the http status code and API's error message.
 * 
 * @author Win10
 *
 */
@ControllerAdvice
public class ExceptionControllerAdvice {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

	@Autowired
	private MessageSourceAccessor messageSourceAccessor;

	/**
	 * Handle the java generic {@link Exception}.
	 * 
	 * @param ex
	 *            java generic {@link Exception}
	 * @return ResponseEntity with message "Un-expected error. Please contact your
	 *         administrator." and {@link HttpStatus.INTERNAL_SERVER_ERROR}
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> exceptionHandler(Exception ex) {
		LOGGER.error("Un-expected error.", ex);
		return new ResponseEntity<ErrorResponse>(
				new ErrorResponse(MessengerResponse.UNEXPECTED.getCode(),
						messageSourceAccessor.getMessage(MessengerResponse.UNEXPECTED.getMessageCode())),
				HttpStatus.PARTIAL_CONTENT);
	}

	/**
	 * Handle validation exception {@link MethodArgumentNotValidException}.
	 * 
	 * @param ex
	 *            {@link MethodArgumentNotValidException}
	 * @return ErrorResponse with list of validation message in format
	 *         "[object][field]: validation message"
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> processValidationError(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();
		StringBuilder description = new StringBuilder();
		for (int i = 0; i < fieldErrors.size(); i++) {
			FieldError fieldError = fieldErrors.get(i);
			if (i > 0) {
				description.append(";");
			}
			String defaultMessage = String.format("[%s][%s]: %s", fieldError.getObjectName(), fieldError.getField(),
					fieldError.getDefaultMessage());
			description.append(messageSourceAccessor.getMessage(fieldError.getCodes()[0], defaultMessage));
		}

		ErrorResponse errorResponse = new ErrorResponse(MessengerResponse.ENTITY_INVALID.getCode(),
				description.toString());
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.PARTIAL_CONTENT);
	}

	/**
	 * Handle messenger exception {@link MessengerException}.
	 * 
	 * @param ex
	 *            {@link MessengerException}
	 * @return ErrorResponse the specified error message and http status is
	 *         BAD_REQUEST
	 */
	@ExceptionHandler(MessengerException.class)
	public ResponseEntity<ErrorResponse> messengerException(MessengerException ex) {
		MessengerResponse messengerResponse = ex.getMessengerResponse();
		return new ResponseEntity<ErrorResponse>(
				new ErrorResponse(messengerResponse.getCode(), messageSourceAccessor
						.getMessage(messengerResponse.getMessageCode(), ex.getParameters().toArray())),
				messengerResponse.getHttpStatus());
	}
}