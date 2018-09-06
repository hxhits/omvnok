package vn.com.omart.backend.application;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import vn.com.omart.backend.application.request.POIOwnerCmd;
import vn.com.omart.backend.application.response.POIOwnerDTO;
import vn.com.omart.backend.application.response.UserDTO;
import vn.com.omart.backend.application.response.UserProfileDTO;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.domain.model.Province;
import vn.com.omart.backend.domain.model.ProvinceRepository;
import vn.com.omart.backend.domain.model.UserProfile;
import vn.com.omart.backend.domain.model.UserProfileRepository;
import vn.com.omart.backend.port.adapter.userprofile.UserService;
import vn.com.omart.sharedkernel.application.model.error.UnauthorizedAccessException;

@Service
public class UserProfileService {

	private final static Logger logger = LoggerFactory.getLogger(UserProfileService.class);

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private ProvinceRepository provinceRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private UserService userService;

	@Value("${auth.user.set-password}")
	private String Set_Password_API;

	@Autowired
	private MobileAdminService mobileAdminService;

	@Autowired
	private UserFriendRequestService userFriendRequestService;

	/**
	 * Get user profile.
	 * 
	 * @param userId
	 * @return
	 */
	public UserProfileDTO getProfile(String userId, String userIdClient) {
		UserProfileDTO userProfile = new UserProfileDTO();
		UserProfile entity = userProfileRepository.findByUserId(userIdClient);
		if (entity != null) {
			userProfile = UserProfileDTO.toDTO(entity);
			POIOwnerDTO poiOwner = mobileAdminService.getOwner_V1(userIdClient);
			if (poiOwner != null) {
				userProfile.setCategoriesSelectedStr(poiOwner.getCategoriesSelectedStr());
				userProfile.setProvinceDistrictSelectedStr(poiOwner.getProvinceDistrictSelectedStr());
				userProfile.setPoiId(poiOwner.getPoiId());
				userProfile.setRecruitmentPositionLevelsSelectedStr(poiOwner.getRecruitmentPositionLevelsSelectedStr());
				userProfile.setRecruitmentProvinceDistrictSelectedStr(
						poiOwner.getRecruitmentProvinceDistrictSelectedStr());
				userProfile.setRemainingTrialDays(poiOwner.getRemainingTrialDays());
				userProfile.setPaid(poiOwner.isPaid());
				userProfile.setFriendState(userFriendRequestService.getState(userId, userIdClient));
			}
		} else {
			// logger.error("UserProfile not found by id: " + userId);
		}
		return userProfile;
	}

	/**
	 * search user by phone
	 * @param phone
	 * @return UserProfileDTO
	 */
	public UserProfileDTO searchByPhone(String phone) {
		UserProfileDTO user = null;
		UserProfile entity = userProfileRepository.findByPhone(phone);
		if (entity != null) {
			user = UserProfileDTO.toBasicDTO(entity);
			return user;
		}
		return new UserProfileDTO();
	}

	/**
	 * Save
	 * 
	 * @param dto
	 */
	public void save(UserProfileDTO dto) {
		try {
			// connect to authentication service to set password.
			Map<String, String> params = new HashMap<String, String>();
			params.put("account", dto.getPhone());
			params.put("password", dto.getPassword());
			ResponseEntity<UserDTO> response = restTemplate.postForEntity(Set_Password_API, dto, UserDTO.class, params);
			HttpStatus status = response.getStatusCode();
			if (status == HttpStatus.OK) {
				UserDTO userDTO = response.getBody();
				dto.setUserId(userDTO.getId());
				UserProfile entity = UserProfileDTO.toEntity(dto);
				entity.setProvince(null);
				if (dto.getProvinceId() != null) {
					Province province = provinceRepository.findOne(dto.getProvinceId());
					if (province != null) {
						entity.setProvince(province);
					} else {
						logger.error(
								"province not found while creating an user profile with userId: " + dto.getUserId());
					}
				}
				userProfileRepository.save(entity);
			} else {
				logger.info("User profile can not set password");
			}
		} catch (Exception e) {
			logger.error("Save user profile error: " + e.getMessage());
		}
	}

	/**
	 * Update profile.
	 * 
	 * @param dto
	 */
	public void update(Long id, UserProfileDTO dto) {
		UserProfile entity = userProfileRepository.findOne(id);
		if (entity != null && dto != null) {
			try {
				if (StringUtils.isNotBlank(dto.getAvatar())) {
					entity.setAvatar(dto.getAvatar());
				}
				if (StringUtils.isNotBlank(dto.getCover())) {
					entity.setCover(dto.getCover());
				}
				if (dto.getDateOfBirth() != null) {
					entity.setDateOfBirth(new Date(dto.getDateOfBirth()));
				}
				entity.setProvince(null);
				if (dto.getProvinceId() != null) {
					Province province = provinceRepository.findOne(dto.getProvinceId());
					if (province != null) {
						entity.setProvince(province);
					} else {
						logger.error(
								"province not found while updating an user profile with userId: " + dto.getUserId());
					}
				}
				entity.setSex(dto.getSex());
				entity.setName(dto.getName());
				entity.setPhone(dto.getPhone());
				userProfileRepository.save(entity);

				// Update AUTH_DB from OMART_DB
				if (null == userService.getOwner(dto.getUserId())) {
					throw new UnauthorizedAccessException("User is Unauthorized");
				}
				POIOwnerCmd.CreateOrUpdate payload = new POIOwnerCmd.CreateOrUpdate();
				payload.setName(dto.getName());
				payload.setAvatar(dto.getAvatar());
				payload.setPhoneNumber(dto.getPhone());
				userService.updateOwner(dto.getUserId(), dto.getUserId(), payload);
			} catch (Exception e) {
				logger.error("[UPDATE USER PROFILE] Error when update AUTH_DB: ", e);
			}
		}
	}

	/**
	 * Fast create an user profile.
	 * 
	 * @param userDTO
	 * @param poi
	 * @return
	 */
	public UserProfile fastCreate(UserDTO userDTO, PointOfInterest poi) {
		UserProfile entity = new UserProfile();
		entity.setUserId(userDTO.getId());
		if (StringUtils.isNotBlank(userDTO.getAvatar())) {
			entity.setAvatar(userDTO.getAvatar());
		}
		if (poi.getCoverImage() != null) {
			if (!poi.getCoverImage().isEmpty()) {
				entity.setCover(poi.getCoverImage().get(0).getUrl());
			}
		}
		entity.setName(userDTO.getFirstname());
		entity.setPhone(userDTO.getPhoneNumber());
		entity.setSex(0);
		if (poi.province() != null) {
			entity.setProvince(poi.province());
		} else {
			entity.setProvince(null);
		}
		entity = userProfileRepository.save(entity);
		return entity;
	}

	/**
	 * Be used from auth service.
	 * 
	 * @param userDTO
	 * @return UserProfile
	 */
	public UserProfile remoteCreate(UserDTO userDTO) {
		UserProfile entity = userProfileRepository.findByUserId(userDTO.getId());
		if (entity == null) {
			entity = new UserProfile();
			entity.setUserId(userDTO.getId());
		}
		entity.setAvatar(userDTO.getAvatar());
		entity.setCover(userDTO.getAvatar());
		entity.setName(userDTO.getFirstname());
		entity.setPhone(userDTO.getPhoneNumber());
		entity.setSex(0);
		entity.setProvince(null);
		entity = userProfileRepository.save(entity);
		return entity;
	}

	/*
	 * ONLY FOR DEVELOPER.
	 */
	public void copyUserToUserProfile() {
		String userEndpointUrl = "https://api.omartvietnam.com/v1/_internal/users/active/all";
		RequestEntity<Void> request = RequestEntity.get(URI.create(userEndpointUrl)).build();
		ResponseEntity<List<UserDTO>> userProfile = this.restTemplate.exchange(request,
				new ParameterizedTypeReference<List<UserDTO>>() {
				});
		List<UserDTO> userDtos = userProfile.getBody();
		List<UserProfile> users = new ArrayList<UserProfile>();
		for (UserDTO item : userDtos) {
			if (!StringUtils.isBlank(item.getId())) {
				UserProfile user = new UserProfile();
				user.setUserId(item.getId());
				user.setAvatar(item.getAvatar());
				user.setName(item.getFirstname());
				user.setPhone(item.getUsername());
				users.add(user);
			}
		}
		userProfileRepository.save(users);
	}

}
