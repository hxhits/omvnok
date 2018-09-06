package vn.com.omart.driver.service.implement;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import com.squareup.pollexor.Thumbor;
import vn.com.omart.driver.service.ThumborService;


public class ThumborServiceImpl implements ThumborService {

	private final RestTemplate restTemplate;
	private final Environment environment;
	private Thumbor thumbor;
	private String thumborHost;
	private String thumborProxy;
	private String thumborSecretKey;

	public ThumborServiceImpl(RestTemplate restTemplate, Environment environment, String thumborHost,
			String thumborProxy, String thumborSecretKey) {
		this.restTemplate = restTemplate;
		this.environment = environment;
		this.thumborHost = thumborHost;
		this.thumborProxy = thumborProxy;
		this.thumborSecretKey = thumborSecretKey;
		this.thumbor = Thumbor.create(thumborProxy, thumborSecretKey);
	}

	/**
	 * Upload image to thumbor.
	 */
	@Override
	public String uploadImage(String imgName, byte[] imageByteArray) {
		// TODO Auto-generated method stub
		UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(thumborHost + "/image").build();

		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setBufferRequestBody(false);
		restTemplate.setRequestFactory(requestFactory);

		HttpHeaders header = new HttpHeaders();
		header.set("Slug", imgName + ".jpg");

		ResponseEntity<String> e = restTemplate.exchange(uriComponents.toUri(), HttpMethod.POST,
				new HttpEntity<>(imageByteArray, header), String.class);
		if (e.getStatusCode() == HttpStatus.CREATED) {
			// log.debug("URL={}", e.getHeaders().getLocation());

			String originImageName = e.getHeaders().getLocation().toString().replaceAll("/image/", "");
			return thumbor.buildImage(originImageName).toUrl();
		}
		throw new RuntimeException("Cannot upload image to thumbor: response_code=" + e.getStatusCode());
	}

}
