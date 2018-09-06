package vn.com.omart.auth.application;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import vn.com.omart.auth.application.request.AccountCmd;
import vn.com.omart.auth.application.response.UserDTO;
import vn.com.omart.auth.domain.ActivationCodeInvalidException;
import vn.com.omart.auth.domain.RoleRepository;
import vn.com.omart.auth.domain.User;
import vn.com.omart.auth.domain.UserNotActivatedException;
import vn.com.omart.auth.domain.UserRepository;
import vn.com.omart.auth.domain.UserTitleEnum;
import vn.com.omart.auth.domain.UsernameNotFoundException;
import vn.com.omart.auth.port.smsgw.SmsGatewayService;
import vn.com.omart.sharedkernel.application.model.error.BusinessViolationException;
import vn.com.omart.sharedkernel.application.model.error.InvalidInputException;
import vn.com.omart.utils.CommonUtils;
import vn.com.omart.utils.DateUtils;

@Service
@Slf4j
public class AccountService {

	@Value("${activation_code_time_to_live:300000}")
	private long activationCodeTimeToLive;

	@Value("${poi.user.profile.remote-create}")
	private String Create_User_Profile;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private CacheManager cacheManager;

	// TODO: Move to Notification gateway
	@Autowired
	private SmsGatewayService smsGatewayService;

	private static final String SMS_TEMPLATE = "Thong bao O Mart dang thuc hien xac thuc tai khoan cua ban! Ma xac thuc la: ";

	public void checkAccount(String account) {

		User userFromDatabase = getAccount(account);

		if (!userFromDatabase.isActivated()) {
			throw new UserNotActivatedException("");
		}

	}

	/**
	 * Checking user name is existing.
	 * 
	 * @param username
	 *            username
	 * @return {@link Boolean }
	 */
	public boolean checkUserByUserName(String username) {
		User user = userRepository.findByusernameIgnoreCase(username);
		return (user == null ? false : true);
	}

	@Transactional
	public void checkAndCreateAccountIfNotExist(String account) {

		User userFromDatabase = null;

		try {
			userFromDatabase = getAccount(account);

		} catch (UsernameNotFoundException e) {
			// Create new account if not exists and then throw UsernameNotFoundException
			AccountCmd.CreateOrUpdate payload = new AccountCmd.CreateOrUpdate();
			payload.setAccount(account);

			createAccount(payload);

			throw e;
		}

		if (!userFromDatabase.isActivated()) {
			throw new UserNotActivatedException("");
		}

	}

	public UserDTO adminCreateAccount(UserDTO userDTO) {
		if (userDTO == null) {
			throw new InvalidInputException("Account should not be null");
		}
		String account = userDTO.getPhoneNumber();
		if (account == null) {
			throw new InvalidInputException("Account should not be null");
		}
		User model = new User();
		String userId = UUID.randomUUID().toString().replaceAll("-", "");
		model.setId(userId);
		model.setFirstname(userDTO.getFirstname());
		model.setUsername(account);
		model.setPhoneNumber(account);
		model.setActivated(true);
		model.setCreatedBy(userDTO.getId());
		model.setCreatedAt(new Date());
		model.setUpdatedBy(userDTO.getId());
		model.setUpdatedAt(new Date());
		model.setTitle(UserTitleEnum.USER.name());
		model.setRoles(Collections.singleton(roleRepository.findOne(UserTitleEnum.USER.name())));
		model.setActivationCode(generateActivationCode());
		model.setVerified(true);
		String myPassword = CommonUtils.generatePassword();
		model.setAvatar(userDTO.getAvatar());
		model.setPassword(CommonUtils.md5(myPassword));
		model = userRepository.save(model);

		UserDTO userResponse = UserDTO.from(model);
		userResponse.setPassword(myPassword);
		return userResponse;
	}

	@Transactional
	public UserDTO createAccount(AccountCmd.CreateOrUpdate payload) {

		if (payload == null) {
			throw new InvalidInputException("Account should not be null");
		}

		String account = payload.getAccount();
		if (account == null) {
			throw new InvalidInputException("Account should not be null");
		}

		User model = new User();

		String userId = UUID.randomUUID().toString().replaceAll("-", "");
		model.setId(userId);
		model.setFirstname("New User");
		model.setUsername(account);
		model.setPhoneNumber(account);
		model.setActivated(false);
		model.setCreatedBy(userId);
		model.setCreatedAt(new Date());

		model.setTitle(UserTitleEnum.USER.name());
		model.setRoles(Collections.singleton(roleRepository.findOne(UserTitleEnum.USER.name())));
		// huonghoang
		model.setActivationCode(generateActivationCode());

		model = userRepository.save(model);

		return UserDTO.from(model);
	}

	@Transactional
	public UserDTO createAccountForDriver(AccountCmd.CreateOrUpdate payload) {

		if (payload == null) {
			throw new InvalidInputException("Account should not be null");
		}

		String account = payload.getAccount();
		if (account == null) {
			throw new InvalidInputException("Account should not be null");
		}

		User model = new User();
		try {
			// Check existed user
			model = getAccount(account);
			if (!model.isActivated() && !StringUtils.isBlank(model.getPassword())) {
				throw new ActivationCodeInvalidException("Ban chua xac thuc ma tai khoan");
			}
		} catch (UsernameNotFoundException e) {
			String userId = UUID.randomUUID().toString().replaceAll("-", "");
			model.setId(userId);
			model.setFirstname("New User");
			model.setUsername(account);
			model.setPhoneNumber(account);
			model.setActivated(false);
			model.setCreatedBy(userId);
			model.setCreatedAt(new Date());

			model.setTitle(UserTitleEnum.USER.name());
			model.setRoles(Collections.singleton(roleRepository.findOne(UserTitleEnum.USER.name())));

			model.setActivationCode(generateActivationCode());

			model = userRepository.save(model);
		}
		return UserDTO.from(model);
	}

	@Transactional
	public UserDTO createAccountv2(AccountCmd.CreateOrUpdate payload) {

		if (payload == null) {
			throw new InvalidInputException("Account should not be null");
		}

		String account = payload.getAccount();
		if (account == null) {
			throw new InvalidInputException("Account should not be null");
		}

		User model = new User();

		try {
			// Check existed user
			model = getAccount(account);

			if (model.isActivated() || model.isVerified()) {
				throw new ActivationCodeInvalidException("Ban chua xac thuc ma tai khoan");
			}
		} catch (UsernameNotFoundException e) {
			String userId = UUID.randomUUID().toString().replaceAll("-", "");
			model.setId(userId);
			model.setFirstname("New User");
			model.setUsername(account);
			model.setPhoneNumber(account);
			model.setActivated(false);
			model.setCreatedBy(userId);
			model.setCreatedAt(new Date());

			model.setTitle(UserTitleEnum.USER.name());
			model.setRoles(Collections.singleton(roleRepository.findOne(UserTitleEnum.USER.name())));

			model.setActivationCode(generateActivationCode());

			model = userRepository.save(model);
		}

		return UserDTO.from(model);
	}

	private Integer generateActivationCode() {

		Random r = new Random(System.currentTimeMillis());
		Integer activationCode = ((1 + r.nextInt(2)) * 100000 + r.nextInt(100000));

		return activationCode;
	}

	// public void sendActivationCode(String account, boolean isSendSMS) {
	//
	// User userFromDatabase = getAccount(account);
	//
	// if (!userFromDatabase.isActivated()) {
	//
	// RedisCache cache = (RedisCache)
	// cacheManager.getCache(CacheName.ACTIVATION_CODE);
	//
	// CodeValidationSession session = cache.get(account,
	// CodeValidationSession.class);
	//
	// if (null == session) {
	//
	// Random r = new Random(System.currentTimeMillis());
	// Integer code = ((1 + r.nextInt(2)) * 100000 + r.nextInt(100000));
	//
	// session = new CodeValidationSession(code);
	//
	// cache.put(account, session);
	//
	// log.info("CodeValidationSession={}", session);
	//
	// String template = "Thong bao O Mart dang thuc hien xac thuc tai khoan cua
	// ban! Ma xac thuc la:
	// ";
	// smsGatewayService.send(account, template + code);
	//
	// } else {
	// log.info("Code in cache -> waiting for expiration before resend");
	// }
	//
	// }
	// }

	/**
	 * Resend Activation Code.
	 * 
	 * @param username
	 *            username
	 * @param isResend
	 *            isResend
	 */
	public void resendActivationCode(String username, boolean isResend) {
		User user = getAccount(username);
		// Check time to live
		if (!isResend) {
			Long lastSent = user.getLastSent();
			if (lastSent != null && lastSent > 0) {
				long interval = DateUtils.getCurrentDate().getTime() - lastSent;
				if (interval < activationCodeTimeToLive) {
					// between two message is 300000 miliseconds
					log.info("between two message is " + activationCodeTimeToLive + " miliseconds");
					return;
				}
			}
		}

		Integer activationCode = user.getActivationCode();
		// If activation code is null, then generate new activation code.
		if (activationCode == null || activationCode == 0) {
			activationCode = generateActivationCode();
			user.setActivationCode(activationCode);
		}
		user.setLastSent(DateUtils.getCurrentDate().getTime());
		userRepository.save(user);
		log.info("Sending activation code to {0}", username);
		// Send SMS
		smsGatewayService.send(username, SMS_TEMPLATE + activationCode);
	}

	@Value("${messenger.phone.verification.start}")
	private String Phone_Verification_Start;

	@Value("${messenger.phone.verification.verify}")
	private String Phone_Verification_Verify;

	public void sendActivationCode(String account, boolean isSendSMS) {
		User userFromDatabase = getAccount(account);
		if (userFromDatabase.isActivated()) {
			return;
		}

		// Check time to live
		Long lastSent = userFromDatabase.getLastSent();

		if (lastSent != null && lastSent > 0) {
			long interval = DateUtils.getCurrentDate().getTime() - lastSent;

			if (interval < activationCodeTimeToLive) {
				String msg = "Khoang cach giua 2 tin nhan phai la " + activationCodeTimeToLive + " miliseconds.";
				log.info(msg);
				// throw new ApplicationException();
				return;
			}
		}

		Integer activationCode = userFromDatabase.getActivationCode();

		// If activation code is null, then generate new activation code.
		if (activationCode == null || activationCode == 0) {
			activationCode = generateActivationCode();
			userFromDatabase.setActivationCode(activationCode);
		}

		userFromDatabase.setLastSent(DateUtils.getCurrentDate().getTime());
		userRepository.save(userFromDatabase);
		log.info("Sending activation code to {}", account);
		/*
		 * Send SMS by messenger/gateway service.
		 */
		String phoneNumber = userFromDatabase.getUsername();
		System.out.println("\n====phoneNumber======"+phoneNumber);
		//if (CommonUtils.isUSPhoneNumber(phoneNumber)) {
		if (!isVNPhoneNumber(phoneNumber)) {
			// send sms to US phone.
			Map<String, String> params = new HashMap<String, String>();
			params.put("phone-number", phoneNumber);
			ResponseEntity<Boolean> response = new RestTemplate().getForEntity(Phone_Verification_Start, Boolean.class,
					params);
			if (response.getBody() == true) {
				System.out.println("\n===sent SMS to US success======"+phoneNumber);
			}
		} else {
			// send sms to VN phone.
			smsGatewayService.send(account, SMS_TEMPLATE + activationCode);
		}
	}
	private boolean isVNPhoneNumber(String phone) {
		String []phones = "012,016,086,096,097,098,090,093,089,091,094,088,018,092,0199,099".split(",");
		for(String v: phones) {
			if(phone.startsWith(v)) {
				return true;
			}
		}
		return false;
	}

	public void verifyActivationCode(String account, String code) {

		User userFromDatabase = getAccount(account);
		if (userFromDatabase.isActivated()) {
			return;
		}
		String phoneNumber = userFromDatabase.getUsername();
		//if (CommonUtils.isUSPhoneNumber(phoneNumber)) {
		if (!isVNPhoneNumber(phoneNumber)) {
			
			/*
			 * Using messenger service to verify (US PHONE)code. by twilio.
			 */
			Map<String, String> params = new HashMap<String, String>();
			params.put("phone-number", phoneNumber);
			params.put("code", code);
			ResponseEntity<Boolean> response = new RestTemplate().getForEntity(Phone_Verification_Verify, Boolean.class,
					params);
			if (response.getBody() == true) {
				System.out.println("\n===verify code success======"+phoneNumber);
				userFromDatabase.setVerified(true);
				userRepository.save(userFromDatabase);
			} else {
				System.out.println("\n===verify code not success======"+phoneNumber);
				throw new ActivationCodeInvalidException("Ma xac thuc tai khoan khong dung");
			}
		} else {
			/*
			 * Using auth service to verify (VN PHONE)code.
			 */
			Integer activationCode = userFromDatabase.getActivationCode();

			if (activationCode == null || activationCode == 0) {
				throw new ActivationCodeInvalidException("Ban chua co ma xac thuc tai khoan");
			}

			if (Integer.parseInt(code) == activationCode) {
				userFromDatabase.setVerified(true);
				userRepository.save(userFromDatabase);

			} else {
				throw new ActivationCodeInvalidException("Ma xac thuc tai khoan khong dung");
			}
		}
	}

	/**
	 * Driver verify.
	 * 
	 * @param account
	 * @param code
	 */
	public void driverVerifyActivationCode(String account, String code) {
		User userFromDatabase = getAccount(account);
		if (Integer.parseInt(code) == userFromDatabase.getActivationCode()) {
			userFromDatabase.setVerified(true);
			userRepository.save(userFromDatabase);
		} else {
			throw new ActivationCodeInvalidException("Ma xac thuc tai khoan khong dung");
		}
	}

	/**
	 * Verify Forget Passwod Activation Code.
	 * 
	 * @param account
	 *            username
	 * @param code
	 *            activationcode
	 */
	public void verifyForgetPasswodActivationCode(String username, String code) {
		User userFromDatabase = getAccount(username);
		Integer activationCode = userFromDatabase.getActivationCode();
		if (activationCode == null || activationCode == 0) {
			throw new ActivationCodeInvalidException("Ban chua co ma xac thuc tai khoan");
		}
		if (Integer.parseInt(code) == activationCode) {
			userFromDatabase.setVerified(true);
			userRepository.save(userFromDatabase);

		} else {
			throw new ActivationCodeInvalidException("Ma xac thuc tai khoan khong dung");
		}
	}

	// public void setPasswordAfterActivationCode(String account, String password) {
	// User userFromDatabase = getAccount(account);
	// if (!userFromDatabase.isActivated()) {
	//
	// RedisCache cache = (RedisCache)
	// cacheManager.getCache(CacheName.ACTIVATION_CODE);
	//
	// CodeValidationSession session = cache.get(account,
	// CodeValidationSession.class);
	// if (null != session) {
	// if (session.isValidate()) {
	// userRepository.save(userFromDatabase.setPasswordAndActive(password));
	// } else {
	// throw new ActivationCodeInvalidException("You have to verify your activation
	// code before.");
	// }
	//
	// } else {
	// throw new ActivationCodeExpireException("Your code is expired, please request
	// again");
	// }
	// }
	// }

	/**
	 * Reset password.
	 * 
	 * @param username
	 *            username
	 * @param password
	 *            password
	 */
	public void resetPassword(String username, String password) {
		User user = getAccount(username);
		Integer activationCode = user.getActivationCode();
		if (activationCode == null || activationCode == 0) {
			throw new ActivationCodeInvalidException("Ban chua co ma xac thuc tai khoan");
		}
		boolean isCodeVerified = user.isVerified();
		if (!isCodeVerified) {
			throw new ActivationCodeInvalidException("Ban chua xac thuc ma tai khoan");
		}
		user.setPassword(password);
		userRepository.save(user);
	}

	public void setPasswordAfterActivationCode(String account, String password) {

		User userFromDatabase = getAccount(account);

		if (userFromDatabase.isActivated()) {
			return;
		}

		if (StringUtils.isNotBlank(userFromDatabase.getPassword())) {
			return;
		}

		Integer activationCode = userFromDatabase.getActivationCode();

		if (activationCode == null || activationCode == 0) {
			throw new ActivationCodeInvalidException("Ban chua co ma xac thuc tai khoan");
		}

		boolean isCodeVerified = userFromDatabase.isVerified();

		if (isCodeVerified) {
			userRepository.save(userFromDatabase.setPasswordAndActive(password));
			// set user profile here
			UserDTO dto = new UserDTO();
			dto.setId(userFromDatabase.getId());
			if (StringUtils.isNotBlank(userFromDatabase.getAvatar())) {
				dto.setAvatar(userFromDatabase.getAvatar());
			}
			dto.setFirstname(userFromDatabase.getFirstname());
			dto.setPhoneNumber(userFromDatabase.getPhoneNumber());
			ResponseEntity<Void> response = new RestTemplate().postForEntity(Create_User_Profile, dto, Void.class);
			if (response.getStatusCode() == HttpStatus.CREATED) {
				// TODO
			} else {
				// TODO
			}
		} else {
			throw new ActivationCodeInvalidException("Ban chua xac thuc ma tai khoan");
		}
	}

	public UserDTO setPasswordAfterActivationCodeV1(String account, String password, UserDTO requestBody) {
		User userFromDatabase = getAccount(account);
		if (userFromDatabase.isActivated()) {
			throw new ActivationCodeInvalidException("Ban chua activated");
		}

		if (StringUtils.isNotBlank(userFromDatabase.getPassword())) {
			throw new ActivationCodeInvalidException("Ban chua co password");
		}

		Integer activationCode = userFromDatabase.getActivationCode();

		if (activationCode == null || activationCode == 0) {
			throw new ActivationCodeInvalidException("Ban chua co ma xac thuc tai khoan");
		}

		boolean isCodeVerified = userFromDatabase.isVerified();

		if (isCodeVerified) {
			userFromDatabase.setFirstname(requestBody.getName());
			userFromDatabase.setAvatar(requestBody.getAvatar());
			userFromDatabase = userRepository.save(userFromDatabase.setPasswordAndActive(password));
			UserDTO dto = new UserDTO();
			dto = UserDTO.from(userFromDatabase);
			return dto;
		} else {
			throw new ActivationCodeInvalidException("Ban chua xac thuc ma tai khoan");
		}
	}

	public UserDTO deactiveAccount(String account) {

		User userFromDatabase = getAccount(account);

		boolean isSalesMan = UserTitleEnum.SALESMAN.name().equalsIgnoreCase(userFromDatabase.getTitle());
		if (!isSalesMan) {
			throw new BusinessViolationException("Only Salesman can be deactivated/reactivated");
		}

		User user = userRepository.save(userFromDatabase.deactivate());

		return UserDTO.from(user);
	}

	public UserDTO reactiveAccount(String account) {

		User userFromDatabase = getAccount(account);
		boolean isSalesMan = UserTitleEnum.SALESMAN.name().equalsIgnoreCase(userFromDatabase.getTitle());
		if (!isSalesMan) {
			throw new BusinessViolationException("Only Salesman can be deactivated/reactivated");
		}

		User user = userRepository.save(userFromDatabase.reactivate());

		return UserDTO.from(user);
	}

	private User getAccount(String account) {

		String lowercaseLogin = account.toLowerCase();
		User userFromDatabase;

		if (lowercaseLogin.contains("@")) {
			userFromDatabase = userRepository.findByEmail(lowercaseLogin);

		} else {
			userFromDatabase = userRepository.findByUsernameCaseInsensitive(lowercaseLogin);
		}

		if (userFromDatabase == null) {
			throw new UsernameNotFoundException("Account " + account + " does not exists.");
		}

		return userFromDatabase;
	}

}
