package vn.com.omart.driver.common.constant;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Messenger Response.
 * 
 * @author Win10
 *
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DriverResponse {
	/** Generic Error. */
	UNEXPECTED(100_000_000, "generic.unexpectedError", HttpStatus.INTERNAL_SERVER_ERROR),
	/** validation error - bad request */
	ENTITY_INVALID(100_000_001, "generic.invalid", HttpStatus.BAD_REQUEST),
	/** car type not found */
	CAR_TYPE_EMPTY(100_000_002, "cartype.empty", HttpStatus.NOT_FOUND),
	
	BOOK_CAR_NOT_FOUND(100_000_003, "bookcar.notfound", HttpStatus.NOT_FOUND);

	private int code;
	private String messageCode;
	private HttpStatus httpStatus;

	DriverResponse(int code, String messageCode, HttpStatus httpStatus) {
		this.code = code;
		this.setMessageCode(messageCode);
		this.httpStatus = httpStatus;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}
}
