package vn.com.omart.messenger.exception;

import java.util.ArrayList;
import java.util.List;

import vn.com.omart.messenger.common.constant.MessengerResponse;

/**
 * Messenger Exception.
 * 
 * @author Win10
 *
 */
public class MessengerException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private MessengerResponse messengerResponse;
	private List<Object> parameters = new ArrayList<Object>();

	public MessengerException(MessengerResponse messengerResponse, Object... params) {
		this(null, messengerResponse, params);
	}

	public MessengerException(Throwable cause, MessengerResponse messengerResponse, Object... params) {
		super(messengerResponse.getMessageCode(), cause);
		this.setMessengerResponse(messengerResponse);
		for (Object param : params) {
			this.parameters.add(param);
		}
	}

	public MessengerResponse getMessengerResponse() {
		return messengerResponse;
	}

	public List<Object> getParameters() {
		return parameters;
	}

	public void setMessengerResponse(MessengerResponse messengerResponse) {
		this.messengerResponse = messengerResponse;
	}

	public void setParameters(List<Object> parameters) {
		this.parameters = parameters;
	}

	public void addParameters(Object param) {
		this.parameters.add(param);
	}
}
