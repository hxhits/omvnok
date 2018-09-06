package vn.com.omart.messenger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.messenger.service.implement.TwilioService;

@RestController
@RequestMapping("/v1/messenger/remote")
public class RemoteController {

	@Autowired
	private TwilioService twilioService;

	/**
	 * Send SMS code verify.
	 * 
	 * @param phoneNumber
	 * @return Boolean
	 */
	@GetMapping(value = "/sendsms/phone/{phone-number}")
	public ResponseEntity<Boolean> sendSMS(@PathVariable(value = "phone-number", required = true) String phoneNumber) {
		// boolean isSent = twilioService.isStartSMSVerification("84", phoneNumber,
		// "sms", "vi");
		boolean isSent = twilioService.isStartSMSVerification("001", phoneNumber, "sms", "us");
		return new ResponseEntity<Boolean>(isSent, HttpStatus.OK);
	}

	/**
	 * Verfidy code.
	 * 
	 * @param phoneNumber
	 * @param code
	 * @return Boolean
	 */
	@GetMapping(value = "/verify/{phone-number}/{code}")
	public ResponseEntity<Boolean> verify(@PathVariable(value = "phone-number", required = true) String phoneNumber,
			@PathVariable(value = "code", required = true) String code) {
		// boolean result = twilioService.isSMSVerify("84", phoneNumber, code);
		boolean result = twilioService.isSMSVerify("001", phoneNumber, code);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}
}
