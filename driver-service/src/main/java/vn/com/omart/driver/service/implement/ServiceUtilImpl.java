package vn.com.omart.driver.service.implement;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import vn.com.omart.driver.dto.UserDTO;
import vn.com.omart.driver.service.ServiceUtil;

@Service
public class ServiceUtilImpl implements ServiceUtil {

	@Value("${auth.user.endpoint-url}")
	private String userEndpointUrl;

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * Get user profile.
	 */
	@Override
	public UserDTO getUserProfile(String userId) {
		// TODO Auto-generated method stub
		RequestEntity<Void> request = RequestEntity.get(URI.create(userEndpointUrl + "/owners/me"))
				.header("X-User-Id", userId).build();
		ResponseEntity<UserDTO> userProfile = this.restTemplate.exchange(request, UserDTO.class);
		return userProfile.getBody();
	}

}
