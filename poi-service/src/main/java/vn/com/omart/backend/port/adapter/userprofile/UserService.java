package vn.com.omart.backend.port.adapter.userprofile;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import vn.com.omart.backend.application.request.POIOwnerCmd;
import vn.com.omart.backend.application.response.UserDTO;
import vn.com.omart.sharedkernel.application.model.error.ApplicationException;

@Service
@Slf4j
public class UserService {
	
	private final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Value("${auth.user.create-account}")
    private String Create_Account_API;
	
    @Value("${auth.userEndpointUrl}")
    private String userEndpointUrl;
    
    private RestTemplate restTemplate = new RestTemplate();
    
    public UserDTO createAccount(UserDTO dto) {
    	try {
			// connect to authentication service to create account.
			ResponseEntity<UserDTO> response = restTemplate.postForEntity(Create_Account_API, dto, UserDTO.class);
			HttpStatus status = response.getStatusCode();
			if (status == HttpStatus.OK) {
				UserDTO userDTO = response.getBody();
				return userDTO;
				
			} else {
				logger.info("User profile can not set password");
			}
		} catch (Exception e) {
			logger.error("Save user profile error: " + e.getMessage());
		}
    	return null;
    }

    public UserResponse createOwner(String userCreator, POIOwnerCmd.CreateOrUpdate payload) {

        RequestEntity<POIOwnerCmd.CreateOrUpdate> request = RequestEntity
            .post(URI.create(userEndpointUrl + "/owners"))
            .header("X-User-Id", userCreator)
            .body(payload);

        ResponseEntity<UserResponse> exchange = this.restTemplate.exchange(request, UserResponse.class);

        return exchange.getBody();
    }

    public UserResponse updateOwner(String ownerId, String userUpdated, POIOwnerCmd.CreateOrUpdate payload) {

        RequestEntity<POIOwnerCmd.CreateOrUpdate> request = RequestEntity
            .put(URI.create(userEndpointUrl + "/owners/" + ownerId))
            .header("X-User-Id", userUpdated)
            .body(payload);

        ResponseEntity<UserResponse> exchange = this.restTemplate.exchange(request, UserResponse.class);

        return exchange.getBody();
    }

    public UserResponse getOwner(String ownerId) {
    	
        RequestEntity<Void> request = RequestEntity
            .get(URI.create(userEndpointUrl + "/owners/me"))
            .header("X-User-Id", ownerId).build();

        try {
            ResponseEntity<UserResponse> exchange = this.restTemplate.exchange(request, UserResponse.class);

            return exchange.getBody();
        } catch (Exception e) {
        	log.error("Error in getting " + userEndpointUrl + "/owners/me", e);
        	
        	if (e instanceof ApplicationException) {
        		throw e;
        		
        	} else {
        		throw new ApplicationException(e);
        	}
        }
    }

    public UserResponse createOrUpdateOwner(String userCreator, POIOwnerCmd.CreateOrUpdate payload) {
    	
        RequestEntity<POIOwnerCmd.CreateOrUpdate> request = RequestEntity
            .put(URI.create(userEndpointUrl + "/owners"))
            .header("X-User-Id", userCreator)
            .body(payload);

        ResponseEntity<UserResponse> exchange = this.restTemplate.exchange(request, UserResponse.class);

        return exchange.getBody();
    }

}
