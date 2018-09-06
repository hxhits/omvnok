package vn.com.omart.messenger.common.constant;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Messenger Response.
 * 
 * @author Win10
 *
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MessengerResponse {
	/** Generic Error. */
	UNEXPECTED(100_000_000, "generic.unexpectedError", HttpStatus.INTERNAL_SERVER_ERROR),
	/** validation error - bad request */
	ENTITY_INVALID(100_000_001, "generic.invalid", HttpStatus.BAD_REQUEST),
	/** poi not found */
	POI_NOT_FOUND(100_000_002, "poi.notFound", HttpStatus.NOT_FOUND),
	/** user not found */
	USER_NOT_FOUND(100_000_003, "user.notFound", HttpStatus.NOT_FOUND),
	/** room not found */
	ROOM_NOT_FOUND(100_000_004, "room.notFound", HttpStatus.NOT_FOUND),
	/** pushnotification not found */
	PUSH_NOTIFICATION_NOT_FOUND(100_000_005, "pushNotification.notFound", HttpStatus.NOT_FOUND),
	/** message channel name not found */
	MESSAGE_CHANNEL_NAME_NOT_FOUND(100_000_006, "messageChannel.nameNotFound", HttpStatus.NOT_FOUND),
	/** message channel sid not found */
	MESSAGE_CHANNEL_SID_NOT_FOUND(100_000_007, "messageChannel.sidNotFound", HttpStatus.NOT_FOUND);

	private int code;
	private String messageCode;
	private HttpStatus httpStatus;

	MessengerResponse(int code, String messageCode, HttpStatus httpStatus) {
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
