package vn.com.omart.backend.port.adapter.rest;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import vn.com.omart.backend.application.MobileAdminService;
import vn.com.omart.backend.application.request.POIOwnerCmd;
import vn.com.omart.backend.application.response.POIOwnerDTO;
import vn.com.omart.backend.application.util.CommonUtils;
import vn.com.omart.backend.constants.UserTitleEnum;
import vn.com.omart.backend.domain.model.OwnerRepository;
import vn.com.omart.backend.port.adapter.userprofile.UserResponse;
import vn.com.omart.backend.port.adapter.userprofile.UserService;
import vn.com.omart.sharedkernel.application.model.error.NotFoundException;
import vn.com.omart.sharedkernel.application.model.error.UnauthorizedAccessException;

@RestController
@RequestMapping("/v1/_admin")
@Slf4j
public class OwnerPOIResource {
	
	@Value("${default_password}")
	private String defaultPassword;

	private final MobileAdminService service;
	private final UserService userService;
	private final OwnerRepository ownerRepository;

	@Autowired
	public OwnerPOIResource(MobileAdminService service, UserService userService, OwnerRepository ownerRepository) {
		this.service = service;
		this.userService = userService;
		this.ownerRepository = ownerRepository;
	}

	@PutMapping(value = { "/upgradetoowner" })
	public POIOwnerDTO upgradeOwner(@RequestHeader(value = "X-User-Id", required = false) String username) {

		if (StringUtils.isBlank(username)) {
			throw new UnauthorizedAccessException("Missing user-id");
		}

		UserResponse user = this.userService.getOwner(username);

		if (user == null) {
			throw new NotFoundException("user does not exist or has just been deleted");
		}

		POIOwnerCmd.CreateOrUpdate payload = new POIOwnerCmd.CreateOrUpdate();
		payload.setName(CommonUtils.getFullname(user.getFirstname(), user.getLastname()));
		payload.setAvatar(user.getAvatar());
		payload.setPhoneNumber(user.getPhoneNumber());
		payload.setPassword(defaultPassword);
		payload.setActivated(true);

		return this.service.createOwner(username, payload, true);
	}

	@PostMapping(value = { "/owners" })
	public ResponseEntity<POIOwnerDTO> createOwner(@RequestHeader(value = "X-User-Id", required = false) String username,
			@RequestBody @Valid POIOwnerCmd.CreateOrUpdate payload) {

		if (StringUtils.isBlank(username)) {
			throw new UnauthorizedAccessException("Missing user-id");
		}

		UserResponse user = this.userService.getOwner(username);

		if (user == null) {
			throw new NotFoundException("user does not exist or has just been deleted");
		}

		String userTitle = user.getTitle();

		boolean isSalesMan = UserTitleEnum.SALESMAN.name().equalsIgnoreCase(userTitle);
		boolean isSupervisor = UserTitleEnum.SUPERVISOR.name().equalsIgnoreCase(userTitle);

		if (!isSalesMan && !isSupervisor) {
			throw new AccessDeniedException(userTitle + " does not have permission to create owner");
		}
		
		POIOwnerDTO dto = this.service.createOwner(username, payload, false);
		
		HttpStatus status;
		
		if (dto.isNew()) {
			status = HttpStatus.CREATED;
			
		} else {
			status = HttpStatus.OK;
		}
		
		return new ResponseEntity<>(dto, status);
	}

	@PutMapping(value = { "/owners/{ownerId}" })
	public POIOwnerDTO updateOwner(@RequestHeader(value = "X-User-Id", required = false) String username,
			@PathVariable(value = "ownerId") String ownerId, @RequestBody @Valid POIOwnerCmd.CreateOrUpdate payload) {

		if (StringUtils.isBlank(username)) {
			throw new UnauthorizedAccessException("Missing user-id");
		}

		UserResponse user = this.userService.getOwner(username);

		if (!user.getTitle().equalsIgnoreCase(UserTitleEnum.SALESMAN.name())
				&& !user.getTitle().equalsIgnoreCase(UserTitleEnum.SUPERVISOR.name())) {
			throw new AccessDeniedException("You have no permission");
		}

		try {
			return this.service.updateOwner(ownerId, username, payload);

		} catch (NumberFormatException e) {
			throw new NotFoundException("POI Owner not exists");
		}
	}
}
