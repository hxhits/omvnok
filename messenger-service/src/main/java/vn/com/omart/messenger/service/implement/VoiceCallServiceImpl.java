package vn.com.omart.messenger.service.implement;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.omart.messenger.application.response.CallStatusDTO;
import vn.com.omart.messenger.application.response.ChatDTO;
import vn.com.omart.messenger.common.constant.Device;
import vn.com.omart.messenger.common.constant.VoiceCall;
import vn.com.omart.messenger.domain.model.CallStatus;
import vn.com.omart.messenger.domain.model.CallStatusRepository;
import vn.com.omart.messenger.service.VoiceCallService;
import vn.com.omart.messenger.util.DateUtils;

@Service
public class VoiceCallServiceImpl implements VoiceCallService {

	@Autowired
	private TwilioService twilioService;

	@Autowired
	private CallStatusRepository callStatusRepository;

	/**
	 * Voice Call Token Generator.
	 */
	@Override
	public ChatDTO voiceTokenGenerator(String userId, Device device) {
		// TODO Auto-generated method stub
		String token = twilioService.voiceTokenGenerator(userId, device);
		ChatDTO chatDTO = new ChatDTO();
		chatDTO.setToken(token);
		return chatDTO;
	}

	@Override
	public String makeCall(String from, String to) {
		// TODO Auto-generated method stub
		String twiML = twilioService.call(from, to);
		return twiML;
	}

	@Override
	public String webhook(VoiceCall status, String callSid, String parentCallSid, String from, String to,
			String callDuration) {
		switch (status) {
		case INITIATED:
			CallStatus entity = new CallStatus();
			entity.setCallSid(callSid);
			entity.setParentCallSid(parentCallSid);
			entity.setFrom(from.split(":")[1]);
			entity.setTo(to.split(":")[1]);
			entity.setStatus(status.getId());
			entity.setCreatedAt(DateUtils.getCurrentDate());
			callStatusRepository.save(entity);
			break;
		case RINGING:
			break;
		case IN_PROGRESS:
			CallStatus callInPr = callStatusRepository.findByCallSidOrParentCallSid(callSid);
			if (callInPr != null) {
				callInPr.setStartedAt(DateUtils.getCurrentDate());
				callInPr.setStatus(status.getId());
				callStatusRepository.save(callInPr);
			}
			break;
		case FAILED:
		case NO_ANSWER:
			CallStatus call = callStatusRepository.findByCallSidOrParentCallSid(callSid);
			if (call != null) {
				call.setStatus(status.getId());
				callStatusRepository.save(call);
			}
			break;
		case COMPLETED:
			CallStatus callComp = callStatusRepository.findByCallSidOrParentCallSid(callSid);
			if (callComp != null) {
				if (callComp.getStatus() != VoiceCall.FAILED.getId()
						|| callComp.getStatus() != VoiceCall.NO_ANSWER.getId()) {
					callComp.setStatus(status.getId());
					callComp.setCompletedAt(DateUtils.getCurrentDate());
					if (!StringUtils.isBlank(callDuration)) {
						callComp.setCallDuration(Integer.parseInt(callDuration));
					}
					callStatusRepository.save(callComp);
				}
			}
			break;
		default:
			break;
		}
		return "";
	}

	@Override
	public CallStatusDTO getCallStatusByCallSid(String callSid) {
		// TODO Auto-generated method stub
		CallStatus entity = callStatusRepository.findByCallSidOrParentCallSid(callSid);
		if (entity != null) {
			CallStatusDTO dto = new CallStatusDTO();
			String status = VoiceCall.getById(entity.getStatus()).getLable();
			dto.setStatus(status);
			return dto;
		}
		return null;
	}

}
