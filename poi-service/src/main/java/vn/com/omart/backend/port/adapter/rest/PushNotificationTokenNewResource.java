package vn.com.omart.backend.port.adapter.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.backend.application.PushNotificationTokenNewService;

@RestController
@RequestMapping("/v1/notification-token-new")
public class PushNotificationTokenNewResource {

	@Autowired
	private PushNotificationTokenNewService pushNotificationTokenNewService;

	@GetMapping(value = "/migration")
	public void dataMigration() {
		pushNotificationTokenNewService.dataMigration();
	}

}
