package vn.com.omart.driver.common.exception;

import java.util.ArrayList;
import java.util.List;

import vn.com.omart.driver.common.constant.DriverResponse;

/**
 * Messenger Exception.
 * 
 * @author Win10
 *
 */
public class DriverException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private DriverResponse driverResponse;
	private List<Object> parameters = new ArrayList<Object>();

	public DriverException(DriverResponse driverResponse, Object... params) {
		this(null, driverResponse, params);
	}

	public DriverException(Throwable cause, DriverResponse driverResponse, Object... params) {
		super(driverResponse.getMessageCode(), cause);
		this.setDriverResponse(driverResponse);
		for (Object param : params) {
			this.parameters.add(param);
		}
	}
	
	public DriverResponse getDriverResponse() {
		return driverResponse;
	}

	public void setDriverResponse(DriverResponse driverResponse) {
		this.driverResponse = driverResponse;
	}

	public List<Object> getParameters() {
		return parameters;
	}

	public void setParameters(List<Object> parameters) {
		this.parameters = parameters;
	}

	public void addParameters(Object param) {
		this.parameters.add(param);
	}
}
