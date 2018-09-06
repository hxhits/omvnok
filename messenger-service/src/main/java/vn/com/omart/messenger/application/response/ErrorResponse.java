package vn.com.omart.messenger.application.response;

/**
 * Error Response Data Transfer Object.
 * 
 * @author Win10
 *
 */
public class ErrorResponse {
	private int code;
	private String description;

	public ErrorResponse(int code, String description) {
		this.setCode(code);
		this.setDescription(description);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
