package vn.com.omart.messenger.service;

import vn.com.omart.messenger.application.response.CallStatusDTO;
import vn.com.omart.messenger.application.response.ChatDTO;
import vn.com.omart.messenger.common.constant.Device;
import vn.com.omart.messenger.common.constant.VoiceCall;

/**
 * Voice Call Service Interface.
 * 
 * @author Win10
 *
 */
public interface VoiceCallService {
	/**
	 * Generator token.
	 * 
	 * @param userId
	 * @param device
	 * @return token
	 */
	public ChatDTO voiceTokenGenerator(String userId, Device device);

	/**
	 * Make Voice Call.
	 * 
	 * @param from
	 * @param to
	 * @return TwiML Application.
	 */
	public String makeCall(String from, String to);

	/**
	 * Web Hook.
	 * 
	 * @param status
	 * @param callSid
	 * @param from
	 * @param to
	 * @param callDuration
	 * @return webhook response
	 */
	public String webhook(VoiceCall status, String callSid, String parentCallSid, String from, String to,
			String callDuration);

	/**
	 * 
	 * @param callSid
	 * @return
	 */
	public CallStatusDTO getCallStatusByCallSid(String callSid);
}
