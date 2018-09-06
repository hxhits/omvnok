package vn.com.omart.auth.port.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.web.bind.annotation.*;
import vn.com.omart.auth.application.request.UserCmd;
import vn.com.omart.auth.application.response.UserDTO;
import vn.com.omart.auth.domain.RoleRepository;
import vn.com.omart.auth.domain.User;
import vn.com.omart.auth.domain.UserRepository;
import vn.com.omart.auth.domain.UserTitleEnum;
import vn.com.omart.sharedkernel.application.model.error.UnauthorizedAccessException;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@FrameworkEndpoint
@Slf4j
public class SupervisorEndpoint {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public SupervisorEndpoint(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping(value = "/users/supervisors")
    @ResponseBody
    public List<UserDTO> getSupervisors(
        @RequestHeader(value = "X-User-Id", required = false) String managerId
    ) {

        if (StringUtils.isBlank(managerId)) {
            throw new UnauthorizedAccessException("Missing user-id");
        }

        return this.userRepository.findAllByManageByAndTitle(managerId, UserTitleEnum.SUPERVISOR.name())
            .stream()
            .map(UserDTO::from)
            .collect(Collectors.toList());
    }

//    @GetMapping(value = "/users/supervisors/{userId}")
//    @ResponseBody
//    public UserDTO getUser(
//        @PathVariable(value = "userId") String id,
//        @RequestHeader(value = "X-User-Id", required = false) String managerId
//    ) {
//
//        if (StringUtils.isBlank(managerId)) {
//            throw new UnauthorizedAccessException("Missing user-id");
//        }
//
//        User model = this.userRepository.findOne(id);
//
//        if (!model.getManageBy().equalsIgnoreCase(managerId)) {
//            throw new UnauthorizedAccessException("This saler is not manage by you!");
//        }
//
//        return UserDTO.from(model);
//    }

    @PutMapping(value = "/users/supervisors")
    @ResponseBody
    public UserDTO updateSupervisors(
        @RequestHeader(value = "X-User-Id", required = false) String managerId,
        @RequestBody @Valid UserCmd.CreateOrUpdateSalesman payload
    ) {
        User model = this.userRepository.findByUsernameCaseInsensitive(payload.getPhoneNumber());

        // Create new
        if (null == model) {

            model = new User();
            model.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            model.setUsername(payload.getPhoneNumber());
            model.setPhoneNumber(payload.getPhoneNumber());

//            model.setActivated(false);
            model.setCreatedBy(managerId);
            model.setCreatedAt(new Date());

        }
//        else {

        // Update
        if (model.getId().equalsIgnoreCase(managerId)) {
            throw new UnauthorizedAccessException("Cannot add yourself!");
        }

        // Not Supervisor
//            if (null == model.getManageBy()) {
        // Not User can not be a Supervisor
//                if (!model.getTitle().equalsIgnoreCase(UserTitleEnum.USER.name())) {
//                    throw new UnauthorizedAccessException("Your Title is not allow to be Supervisor!");
//                }
//                model.setManageBy(managerId);
//                model.setActivated(false);
//            }
//            else {
        // update info
//                if (!model.getManageBy().equalsIgnoreCase(managerId)) {
//                    throw new UnauthorizedAccessException("This Supervisor is not manage by you!");
//                }
//            }

        model.setUpdatedBy(managerId);
        model.setUpdatedAt(new Date());
//        }

        // TODO: Hard code supervisor to MR Hien
        model.setManageBy("b789dcd31f764054afb16da4a0037e53");

        model.setFirstname(payload.getName());
//        model.setLastname(null);
        model.setAvatar(payload.getAvatar());
        model.setEmail(payload.getEmail());

        model.setTitle(UserTitleEnum.SUPERVISOR.name());
        model.setRoles(Collections.singleton(roleRepository.findOne("SUPERVISOR")));

        userRepository.save(model);

        return UserDTO.from(model);
    }


}
