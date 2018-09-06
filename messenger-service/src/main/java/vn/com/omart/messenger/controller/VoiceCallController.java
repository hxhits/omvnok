package vn.com.omart.messenger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.messenger.application.response.CallStatusDTO;
import vn.com.omart.messenger.application.response.ChatDTO;
import vn.com.omart.messenger.common.constant.Device;
import vn.com.omart.messenger.common.constant.VoiceCall;
import vn.com.omart.messenger.service.VoiceCallService;

@RestController
@RequestMapping("v1/messenger")
public class VoiceCallController {

	@Autowired
	private VoiceCallService voiceCallService;

	/**
	 * Voice Call Token Generator.
	 * 
	 * @param userId
	 * @param device
	 * @return token
	 */
	@RequestMapping(value = "/voice/token", method = RequestMethod.GET)
	public ResponseEntity<ChatDTO> generateToken(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestHeader(value = "client", required = true) Device device) {
		ChatDTO chat = voiceCallService.voiceTokenGenerator(userId, device);
		return new ResponseEntity<ChatDTO>(chat, HttpStatus.OK);
	}

	/**
	 * 
	 * @param initiated
	 * @param ringing
	 * @param answered
	 * @param completed
	 * @return
	 */
	@RequestMapping(value = "/voice/webhook", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> webhook(@RequestParam(value = "CallStatus", required = false) String callStatus,
			@RequestParam(value = "CallSid", required = false) String callSid,
			@RequestParam(value = "ParentCallSid", required = false) String parentCallSid,
			@RequestParam(value = "From", required = false) String from,
			@RequestParam(value = "To", required = false) String to,
			@RequestParam(value = "CallDuration", required = false) String callDuration) {

		System.out.println("\n==============xml callStatus===========" + callStatus);

		/*
		 * System.out.println("\n==============xml CallSid===========" + callSid);
		 * System.out.println("\n==============xml ParentCallSid===========" +
		 * parentCallSid); System.out.println("\n==============xml from===========" +
		 * from); System.out.println("\n==============xml to===========" + to);
		 * System.out.println("\n==============xml callDuration===========" +
		 * callDuration);
		 * 
		 * System.out.println("\n=======************************==========");
		 */

		VoiceCall status = VoiceCall.getByLable(callStatus);
		if (status != null) {
			voiceCallService.webhook(status, callSid, parentCallSid, from, to, callDuration);
		}
		return new ResponseEntity<String>("omart viet nam", HttpStatus.OK);
	}

	@RequestMapping(value = "/voice/makeCall", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> makeCall(@RequestParam(value = "to", required = false) String to,
			@RequestParam(value = "from", required = false) String from) {
		String call = voiceCallService.makeCall(from, to);
		return new ResponseEntity<String>(call, HttpStatus.OK);
	}

	@RequestMapping(value = "/voice/status/{call-sid}", method = RequestMethod.GET)
	public ResponseEntity<CallStatusDTO> status(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "call-sid", required = true) String callSid) {
		CallStatusDTO dto = voiceCallService.getCallStatusByCallSid(callSid);
		return new ResponseEntity<CallStatusDTO>(dto, HttpStatus.OK);
	}
}
