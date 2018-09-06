package vn.com.omart.messenger.port.adapter.rest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import vn.com.omart.messenger.application.request.PushNotificationCMD;
import vn.com.omart.messenger.common.constant.Device;
import vn.com.omart.messenger.domain.model.PushNotificationToken;
import vn.com.omart.messenger.domain.model.PushNotificationTokenRepository;
import vn.com.omart.messenger.service.PushNotificationTokenNewService;

@RestController
@RequestMapping("/v1/push/notification")
@Slf4j
public class PushNotificationResource {

	private final PushNotificationTokenRepository pushNotificationTokenRepository;

	public PushNotificationResource(PushNotificationTokenRepository pushNotificationTokenRepository) {
		this.pushNotificationTokenRepository = pushNotificationTokenRepository;
	}
	
	@Autowired
	private PushNotificationTokenNewService pushNotificationTokenNewService;
	
	@PutMapping
	public void register(
			@RequestHeader(value="X-User-Id", required = true ) String userId,
			@RequestHeader(value = "client", required = false, defaultValue = "undefine") Device client,
			@RequestBody(required = true) PushNotificationCMD.Register payload) {
		log.info("Register user={}  client={}  token={}", userId, client.getId() ,payload.getToken());
		pushNotificationTokenNewService.save(userId, client, payload.getToken());
	}
	
	@DeleteMapping
	public void unregister(@RequestHeader(value="X-User-Id", required = true) String userId,
			@RequestBody(required = false) PushNotificationCMD.Register payload) {
		log.info("Unregister user={}", userId);
		//support 2 case: 1-user, 2-user and token
		if(payload == null) {
			pushNotificationTokenNewService.deleteByUserId(userId);
		} else {
			if(StringUtils.isNotBlank(payload.getToken())) {
				pushNotificationTokenNewService.deleteByUserIdAndToken(userId, payload.getToken());
			}
		}
	}
	
//	@PutMapping
//	public void register(@RequestHeader("X-User-Id") String userId,
//			@RequestHeader(value = "client", required = false) Device client,
//			@RequestBody PushNotificationCMD.Register payload) {
//		if (StringUtils.isBlank(userId)) {
//			throw new AccessDeniedException("You are missing User-ID");
//		}
//		log.info("Register user={} token={}", userId, payload.getToken());
//		PushNotificationToken pushNotiEnity = new PushNotificationToken(userId, payload.getToken());
//		if (client != null) {
//			pushNotiEnity.setClient(client.getId());
//		}
//		pushNotificationTokenRepository.save(pushNotiEnity);
//	}
//
//	@DeleteMapping
//	public void unregister(@RequestHeader("X-User-Id") String userId) {
//		if (StringUtils.isBlank(userId)) {
//			throw new AccessDeniedException("You are missing User-ID");
//		}
//		log.info("Unregister user={}", userId);
//		PushNotificationToken one = pushNotificationTokenRepository.findByUserId(userId);
//		if (null != one) {
//			pushNotificationTokenRepository.delete(one);
//		}
//	}

}
