package vn.com.omart.auth.port.endpoint;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import lombok.extern.slf4j.Slf4j;
import vn.com.omart.auth.application.request.UserCmd;
import vn.com.omart.auth.application.response.UserDTO;
import vn.com.omart.auth.domain.User;
import vn.com.omart.auth.domain.UserRepository;
import vn.com.omart.auth.domain.UserTitleEnum;
import vn.com.omart.auth.domain.UsernameNotFoundException;
import vn.com.omart.sharedkernel.application.model.error.UnauthorizedAccessException;

@FrameworkEndpoint
@Slf4j
public class UserEndpoint {

  @Autowired
  private UserRepository userRepository;

  @GetMapping(value = "/_internal/users/owners")
  @ResponseBody
  public List<UserDTO> getOwnersByCreator(
      @RequestHeader(value = "X-User-Id", required = false) String userId) {
    return this.userRepository.findAllByCreatedBy(userId).stream().map(UserDTO::from)
        .collect(Collectors.toList());
  }

  @GetMapping(value = "/_internal/users/owners/me")
  @ResponseBody
  public UserDTO getOwner(@RequestHeader(value = "X-User-Id", required = false) String ownerId) {
    User one = this.userRepository.findOne(ownerId);

    if (one == null) {
      log.error(ownerId + " does not exists");
      throw new UsernameNotFoundException(ownerId + " does not exists");
    }

    return UserDTO.from(one);
  }

  @PutMapping(value = "/_internal/users/owners")
  @ResponseBody
  public UserDTO createOrUpdateOwner(
      @RequestHeader(value = "X-User-Id", required = false) String userId,
      @RequestBody @Valid UserCmd.CreateOrUpdate payload) {
    log.debug("createOrUpdateOwner UserId={} Payload={}", userId, payload);

    User user = userRepository.findByUsernameCaseInsensitive(payload.getPhoneNumber());
    boolean isNew = false;

    if (null == user) {
      user = createNewUser(userId, payload);
      isNew = true;

    } else {
      // if (userId.equalsIgnoreCase(user.getId())) {
      // throw new UnauthorizedAccessException("Cannot add yourself!");
      // }

      boolean isOwner = UserTitleEnum.OWNER.name().equalsIgnoreCase(user.getTitle());

      // If not a owner
      if (!isOwner) {
        // Only User can be become to owner
        if (!UserTitleEnum.USER.name().equalsIgnoreCase(user.getTitle())) {
          throw new UnauthorizedAccessException("Your Title is not allow to be Owner!");
        }
      }

      // If user is not activated and password is null, then set default password
      String currentPassword = user.getPassword();
      boolean isPasswordEmpty = StringUtils.isBlank(currentPassword);

      if (isPasswordEmpty && !user.isActivated()) {
        String password = payload.getPassword();
        if (StringUtils.isNotEmpty(password)) {
          user.setPassword(password);
        }
      }
    }

    user.setTitle(UserTitleEnum.OWNER.name());
    user.setUpdatedBy(userId);
    user.setUpdatedAt(new Date());

    userRepository.save(user);

    UserDTO dto = UserDTO.from(user);
    dto.setNew(isNew);

    return dto;
  }

  private User createNewUser(String userId, UserCmd.CreateOrUpdate payload) {

    User user = new User();

    user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
    user.setPhoneNumber(payload.getPhoneNumber());
    user.setUsername(payload.getPhoneNumber());
    user.setFirstname(payload.getName());
    user.setCreatedBy(userId);
    user.setCreatedAt(new Date());

    String password = payload.getPassword();
    if (StringUtils.isNotEmpty(password)) {
      user.setPassword(password);
    }

    user.setActivated(payload.isActivated());

    return user;
  }

  @PostMapping(value = "/_internal/users/owners")
  @ResponseBody
  public UserDTO createOwner(@RequestHeader(value = "X-User-Id", required = false) String userId,
      @RequestBody @Valid UserCmd.CreateOrUpdate payload) {
    log.debug("UserId={} Payload={}", userId, payload);
    User model = new User();

    model.setId(UUID.randomUUID().toString().replaceAll("-", ""));
    model.setFirstname(payload.getName());
    // model.setLastname(null);
    model.setAvatar(payload.getAvatar());
    model.setPhoneNumber(payload.getPhoneNumber());
    model.setUsername(payload.getPhoneNumber());
    model.setPassword(payload.getPassword());
    // model.setEmail(null);
    model.setTitle(UserTitleEnum.OWNER.name());
    model.setActivated(true);
    model.setCreatedBy(userId);
    model.setCreatedAt(new Date());

    userRepository.save(model);

    return UserDTO.from(model);
  }

  @PutMapping(value = "/_internal/users/owners/{ownerId}")
  @ResponseBody
  public UserDTO updateOwner(@RequestHeader(value = "X-User-Id", required = false) String userId,
      @PathVariable(value = "ownerId") String ownerId,
      @RequestBody @Valid UserCmd.CreateOrUpdate payload) {
    log.debug("UserId={} OwnerId={} Payload={}", userId, ownerId, payload);
    User model = this.userRepository.findOne(ownerId);
    // if (null == model) {
    // throw new NotFoundException("POI Owner not exists");
    // }

    // User model = new User();

    // model.setId(UUID.randomUUID().toString().replaceAll("-", ""));
    model.setFirstname(payload.getName());
    // model.setLastname(null);
    model.setAvatar(payload.getAvatar());
    // model.setPhoneNumber(payload.getPhoneNumber());
    // model.setUsername(payload.getPhoneNumber());
    if(StringUtils.isNotBlank(payload.getPassword())) {
    	 model.setPassword(payload.getPassword());
    }
    // model.setEmail(null);
    // model.setType(UserType.ONWER.name());
    model.setActivated(true);
    // model.setCreatedBy(userId);
    // model.setCreatedAt(new Date());
    model.setUpdatedBy(userId);
    model.setUpdatedAt(new Date());

    userRepository.save(model);

    return UserDTO.from(model);
  }

  @PostMapping(value = "/_internal/users")
  @ResponseBody
  public UserDTO createUser(@RequestBody @Valid UserCmd.CreateOrUpdate1 payload) {
    User model = new User();

    model.setId(UUID.randomUUID().toString().replaceAll("-", ""));
    model.setManageBy(payload.getManageBy());
    model.setFirstname(payload.getName());
    // model.setLastname(null);
    model.setAvatar(payload.getAvatar());
    model.setPhoneNumber(payload.getPhoneNumber());
    model.setUsername(payload.getPhoneNumber());
    model.setPassword(payload.getPassword());
    model.setEmail(payload.getEmail());
    // model.setType(UserType.ONWER.name());
    model.setActivated(true);
    model.setCreatedBy(payload.getActionBy());
    model.setCreatedAt(new Date());

    userRepository.save(model);

    return UserDTO.from(model);
  }

  @GetMapping(value = "/_internal/users/{userId}")
  @ResponseBody
  public UserDTO getUser(@PathVariable(value = "userId") String id) {
    User model = this.userRepository.findOne(id);
    return UserDTO.from(model);
  }

  @PutMapping(value = "/_internal/users/{userId}")
  @ResponseBody
  public UserDTO updateUser(@PathVariable(value = "userId") String id,
      @RequestBody @Valid UserCmd.CreateOrUpdate1 payload) {
    // User model = new User();
    User model = this.userRepository.findOne(id);

    model.setManageBy(payload.getManageBy());
    model.setFirstname(payload.getName());
    // model.setLastname(null);
    model.setAvatar(payload.getAvatar());
    model.setPhoneNumber(payload.getPhoneNumber());
    model.setUsername(payload.getPhoneNumber());
    model.setPassword(payload.getPassword());
    model.setEmail(payload.getEmail());
    model.setTitle(payload.getType());
    model.setActivated(true);
    // model.setCreatedBy(payload.getCreatedBy());
    // model.setCreatedAt(new Date());
    model.setUpdatedBy(payload.getActionBy());
    model.setUpdatedAt(new Date());

    userRepository.save(model);

    return UserDTO.from(model);
  }
  
  @GetMapping(value="_internal/users/active/all")
  @ResponseBody
  public List<UserDTO> getAllUser() {
	  List<User> entities = userRepository.findAllByActivated(true);
	  List<UserDTO> dtos = entities.stream().map(UserDTO::from).collect(Collectors.toList());
	  return dtos;
  }

}
