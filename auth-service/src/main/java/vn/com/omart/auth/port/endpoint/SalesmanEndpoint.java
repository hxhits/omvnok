package vn.com.omart.auth.port.endpoint;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import vn.com.omart.auth.application.request.UserCmd;
import vn.com.omart.auth.application.response.UserDTO;
import vn.com.omart.auth.domain.RoleRepository;
import vn.com.omart.auth.domain.User;
import vn.com.omart.auth.domain.UserRepository;
import vn.com.omart.auth.domain.UserTitleEnum;
import vn.com.omart.sharedkernel.application.model.error.InvalidInputException;
import vn.com.omart.sharedkernel.application.model.error.NotFoundException;
import vn.com.omart.sharedkernel.application.model.error.UnauthorizedAccessException;

@FrameworkEndpoint
@Slf4j
public class SalesmanEndpoint {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Value("${default_password:3141a4d52c8f69749a61da90243bb52f}")
	private String defaultPassword;

    @Autowired
    public SalesmanEndpoint(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping(value = "/users/salesman")
    @ResponseBody
    public List<UserDTO> getSalesmanManagedBy(
        @RequestHeader(value = "X-User-Id", required = false) String managerId
    ) {

        if (StringUtils.isBlank(managerId)) {
            throw new UnauthorizedAccessException("Missing user-id");
        }

        return this.userRepository.findAllByManageByAndTitle(managerId, UserTitleEnum.SALESMAN.name())
            .stream().map(UserDTO::from).collect(Collectors.toList());
    }

    @GetMapping(value = "/users/managedby")
    @ResponseBody
    public List<UserDTO> getUsersManagedBy(
        @RequestHeader(value = "X-User-Id", required = false) String managerId
    ) {

        if (StringUtils.isBlank(managerId)) {
            throw new UnauthorizedAccessException("Missing user-id");
        }

        List<UserDTO> list = this.userRepository.findAllByManageBy(managerId)
            .stream().map(UserDTO::from).collect(Collectors.toList());

        return list;
    }

    @GetMapping(value = "/users/salesman/{salerId}")
    @ResponseBody
    public UserDTO getUser(
        @PathVariable(value = "salerId") String id,
        @RequestHeader(value = "X-User-Id", required = false) String managerId
    ) {

        if (StringUtils.isBlank(managerId)) {
            throw new UnauthorizedAccessException("Missing user-id");
        }

        User model = this.userRepository.findOne(id);

        if (!model.getManageBy().equalsIgnoreCase(managerId)) {
            throw new UnauthorizedAccessException("This saler " + model.getUsername() + " is not manage by you!");
        }

        return UserDTO.from(model);
    }

    @PutMapping(value = "/users/salesman")
    @ResponseBody
    public UserDTO updateUser(
        @RequestHeader(value = "X-User-Id", required = false) String managerId,
        @RequestBody @Valid UserCmd.CreateOrUpdateSalesman payload
    ) {
        User model = this.userRepository.findByUsernameCaseInsensitive(payload.getPhoneNumber());

        // Create new
        if (null == model) {
            model = createNewUser(managerId, payload);

        } else {

            if (model.getId().equalsIgnoreCase(managerId)) {
                throw new UnauthorizedAccessException("Cannot add yourself!");
            }

            boolean isSalesMan = UserTitleEnum.SALESMAN.name().equalsIgnoreCase(model.getTitle());

            // If not a salesman
            if (!isSalesMan) {
                // Only User can be become to salesman
                if (!UserTitleEnum.USER.name().equalsIgnoreCase(model.getTitle())) {
                    throw new UnauthorizedAccessException("User " + model.getUsername() + " is not allow to be Salesman!");
                }

                model.setManageBy(managerId);
                //model.setActivated(false);

            } else {
                // update info
                if (!model.getManageBy().equalsIgnoreCase(managerId)) {
                    throw new UnauthorizedAccessException("This salesman is not manage by you!");
                }
            }

            model.setUpdatedBy(managerId);
            model.setUpdatedAt(new Date());
        }

        model.setFirstname(payload.getName());
//        model.setLastname(null);
        model.setAvatar(payload.getAvatar());
        model.setEmail(payload.getEmail());

        model.setTitle(UserTitleEnum.SALESMAN.name());
        model.setRoles(Collections.singleton(roleRepository.findOne("SALESMAN")));

        model = userRepository.save(model);

        return UserDTO.from(model);
    }

	private User createNewUser(String managerId, UserCmd.CreateOrUpdateSalesman payload) {
		User model;
		model = new User();
		model.setId(UUID.randomUUID().toString().replaceAll("-", ""));
		model.setUsername(payload.getPhoneNumber());
		model.setPhoneNumber(payload.getPhoneNumber());
		model.setManageBy(managerId);
		model.setActivated(false);
		model.setCreatedBy(managerId);
		model.setCreatedAt(new Date());
        if (payload.isAutoActivated()) {
            model.setPasswordAndActive(this.defaultPassword);
        }
		return model;
	}

    @DeleteMapping(value = "/users/salesman/{id}")
    @ResponseBody
    public UserDTO deroleSalesman(
        @PathVariable(value = "id") String id,
        @RequestHeader(value = "X-User-Id", required = false) String managerId
    ) {
    	//TODO: We need to discuss more details about this case. For now, just deactivate it.
//        User model = this.userRepository.findOne(id);
//
//        if (!model.getManageBy().equalsIgnoreCase(managerId)) {
//            throw new UnauthorizedAccessException("This saler is not manage by you!");
//        }
//
//        model.setTitle(UserTitleEnum.USER.name());
//        model.setRoles(Collections.singleton(roleRepository.findOne("USER")));
//
//        model.setManageBy(null);
//
//        model.setUpdatedBy(managerId);
//        model.setUpdatedAt(new Date());
//
//        userRepository.save(model);
//
//        return UserDTO.from(model);

    	 User model = this.userRepository.findOne(id);
         userRepository.save(model.deactivate());

         return UserDTO.from(model);
    }

	@PutMapping(value = "/users/changePassword/{password}")
	public @ResponseBody UserDTO changePassword(
			@RequestHeader(value = "X-User-Id", required = false) String userId,
			@PathVariable("password") String password,
			@RequestBody @Valid UserCmd.ChangePassword payload) throws Exception {

		if (payload == null) {
			throw new InvalidInputException("Current password should not be null");
		}

		String currentPassword = payload.getCurrentPassword();

		log.debug("changePassword={}", currentPassword);

		if (currentPassword == null) {
			throw new InvalidInputException("Current password should not be null");
		}

		User model = this.userRepository.findOne(userId);

		if (model == null) {
			throw new InvalidInputException("User does not exist or has just been deleted");
		}

		if (!currentPassword.equals(model.getPassword())) {
			throw new InvalidInputException("Current password does not match");
		}

		model = userRepository.save(model.changePassword(password));

		return UserDTO.from(model);
	}

    @PutMapping(value = "/users/profile")
    @ResponseBody
    public UserDTO updateProfile(
        @RequestHeader(value = "X-User-Id", required = false) String userId,
        @RequestBody @Valid UserCmd.UpdateProfile payload
    ) {
        if (StringUtils.isBlank(userId)) {
            throw new UnauthorizedAccessException("Missing user-id");
        }
        User model = this.userRepository.findOne(userId);
        if (null == model) {
            throw new NotFoundException("User not found");
        }

        String name = payload.getName();
		if (StringUtils.isNotEmpty(name)) {
        	model.updateFirstname(name);
        }

        String avatar = payload.getAvatar();
		if (StringUtils.isNotEmpty(avatar)) {
        	model.updateAvatar(avatar);
        }

        model = userRepository.save(model);

        return UserDTO.from(model);
    }

//    @PutMapping(value = "/users")
//    @ResponseBody
//    public UserDTO userRegister(
//        @RequestHeader(value = "X-User-Id", required = false) String managerId,
//        @RequestBody @Valid UserCmd.CreateOrUpdateSalesman payload
//    ) {
//        User model = this.userRepository.findByUsernameCaseInsensitive(payload.getPhoneNumber());
//
//        // Create new
//        if (null == model) {
//            model = new User();
//            String userId = UUID.randomUUID().toString().replaceAll("-", "");
//            model.setId(userId);
//            model.setUsername(payload.getPhoneNumber());
//            model.setPhoneNumber(payload.getPhoneNumber());
//            model.setActivated(false);
//            model.setCreatedBy(userId);
//            model.setCreatedAt(new Date());
//        }
//
//        model.setTitle(UserTitleEnum.USER.name());
//        model.setRoles(Collections.singleton(roleRepository.findOne(UserTitleEnum.USER.name())));
//
//        userRepository.save(model);
//
//        return UserDTO.from(model);
//    }

}
