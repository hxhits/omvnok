package vn.com.omart.driver;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import de.bytefish.fcmjava.client.FcmClient;
import de.bytefish.fcmjava.constants.Constants;
import de.bytefish.fcmjava.http.options.IFcmClientSettings;
import vn.com.omart.driver.service.implement.ThumborServiceImpl;

@SpringBootApplication
@EnableCaching(proxyTargetClass = true)
public class Main {
	
	@Bean
	@Qualifier("driver")
	public FcmClient fcmClientDriver(@Value("${push.notification.fcm.secretkey.driver}") String fcmSecretKey) {
		return new FcmClient(new IFcmClientSettings() {
			@Override
			public String getFcmUrl() {
				return Constants.FCM_URL;
			}

			@Override
			public String getApiKey() {
				return fcmSecretKey;
			}
		});
	}

	@Bean
	ThumborServiceImpl thumborService(RestTemplate restTemplate, Environment environment,
			@Value("${thumbor.host}") String thumborHost, @Value("${thumbor.proxy}") String thumborProxy,
			@Value("${thumbor.secretKey}") String thumborSecretKey) {
		return new ThumborServiceImpl(restTemplate, environment, thumborHost, thumborProxy, thumborSecretKey);
	}

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
