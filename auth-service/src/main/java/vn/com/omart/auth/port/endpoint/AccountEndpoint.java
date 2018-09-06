package vn.com.omart.auth.port.endpoint;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import lombok.extern.slf4j.Slf4j;
import vn.com.omart.auth.application.AccountService;
import vn.com.omart.auth.application.request.AccountCmd;
import vn.com.omart.auth.application.response.UserDTO;
import vn.com.omart.auth.domain.EmptyJsonResponse;
import vn.com.omart.auth.domain.User;

@FrameworkEndpoint
@Slf4j
public class AccountEndpoint {

  @Autowired
  private AccountService accountService;
  
  /**
   * Create account be called from driver application.
   * @param payload
   * @return UserDTO
   */
  @PostMapping(value = "/accounts",headers= {"X-API-Version=1.1"})
  @ResponseBody
  public UserDTO createAccountForDriver(@RequestBody @Valid AccountCmd.CreateOrUpdate payload) {

    log.debug("createAccount={}", payload);

    UserDTO user = null;
    if (payload.isCheck_existed()) {
    	user = accountService.createAccountForDriver(payload);
    } else {
    	user = accountService.createAccount(payload);
    }
    return user;
  }

  @PostMapping(value = "/accounts/create")
  @ResponseBody
  public UserDTO adminCreateAccount(@RequestBody @Valid UserDTO dto) {
    log.debug("createAccount={}", dto);
    UserDTO user =  accountService.adminCreateAccount(dto);;
    return user;
  }
  
  @PostMapping(value = "/accounts")
  @ResponseBody
  public UserDTO createAccount(@RequestBody @Valid AccountCmd.CreateOrUpdate payload) {

    log.debug("createAccount={}", payload);

    UserDTO user = null;
    if (payload.isCheck_existed()) {
    	user = accountService.createAccountv2(payload);
    } else {
    	user = accountService.createAccount(payload);
    }
    return user;
  }

  @PostMapping(value = "/accountsv2")
  @ResponseBody
  public UserDTO createAccountv2(@RequestBody @Valid AccountCmd.CreateOrUpdate payload) {

    log.debug("createAccount={}", payload);

    UserDTO user = accountService.createAccountv2(payload);
    return user;
  }

  @GetMapping(value = "/accounts/{account}/check")
  @ResponseBody
  public void checkAndCreateAccountIfNotExist(@PathVariable(value = "account") String account) {
    log.debug("checkAccount={}", account);
    accountService.checkAndCreateAccountIfNotExist(account);
  }

  @GetMapping(value = "/accounts/{account}/checkv2")
  @ResponseBody
  public void checkAccount(@PathVariable(value = "account") String account) {
    log.debug("checkAccount={}", account);
    accountService.checkAccount(account);
  }

  /**
   * Checking user name is existing and send activation code SMS.
   *
   * @param username username
   * @return {@link EmptyJsonResponse}
   */
  @RequestMapping(value = "/accounts/{username}/forget-password", method = RequestMethod.GET)
  public ResponseEntity<EmptyJsonResponse> checkUserByUserName(@PathVariable(value = "username") String userName) {
    boolean isUserNameExiting = accountService.checkUserByUserName(userName);
    if (isUserNameExiting) {
      accountService.resendActivationCode(userName, false);
      return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
    }
    return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
  }

  /**
   * Verify activation code.
   *
   * @param username phone number
   * @param code code
   * @return {@link EmptyJsonResponse}
   */
  @RequestMapping(value = "/accounts/{username}/verify-forget-password-code/{code}", method = RequestMethod.GET)
  public ResponseEntity<EmptyJsonResponse> verifyForgetPasswordCode(@PathVariable(value = "username") String username, @PathVariable(value = "code") String code) {
    accountService.verifyForgetPasswodActivationCode(username, code);
    return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
  }

  /**
   * Resend Activation Code.
   *
   * @param username username
   * @return {@link EmptyJsonResponse}
   */
  @RequestMapping(value = "/accounts/{username}/resend-activation-code", method = RequestMethod.GET)
  public ResponseEntity<EmptyJsonResponse> resendActivationCode(@PathVariable(value = "username") String userName) {
    boolean isUserNameExiting = accountService.checkUserByUserName(userName);
    if (isUserNameExiting) {
      accountService.resendActivationCode(userName, true);
    }
    return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
  }

  /**
   * Reset Password.
   *
   * @param username username
   * @param user password
   * @return {@link EmptyJsonResponse}
   */
  @RequestMapping(value = "/accounts/{username}/reset-password", method = RequestMethod.PUT)
  public ResponseEntity<EmptyJsonResponse> resetPassword(@PathVariable(value = "username") String username, @RequestBody User user) {
    if (user == null) {
      return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.NO_CONTENT);
    }
    String password = user.getPassword();
    accountService.resetPassword(username, password);
    return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
  }

  @PostMapping(value = "/accounts/{account}/sendActivationCode")
  @ResponseBody
  public void sendActivationCode(@PathVariable(value = "account") String account,
      @RequestParam(value = "isSendSMS", required = false, defaultValue = "true") String isSendSMS) {
    log.debug("sendActivationCode={} isSendSMS={}", account, isSendSMS);
    accountService.sendActivationCode(account, Boolean.parseBoolean(isSendSMS));
  }

  @PostMapping(value = "/accounts/{account}/verifyActivationCode/{code}")
  @ResponseBody
  public void activateCode(@PathVariable(value = "account") String account, @PathVariable(value = "code") String code) {
    log.debug("verifyActivationCode={} code={}", account, code);
    accountService.verifyActivationCode(account, code);
  }
  
  /**
   * Driver send activation code.
   * @param account
   * @param isSendSMS
   */
  @PostMapping(value = "/accounts/{account}/driverSendActivationCode")
  @ResponseBody
  public void sendDriverActivationCode(@PathVariable(value = "account") String account) {
    log.debug("sendActivationCode={} ", account);
    accountService.resendActivationCode(account, true);
  }
  
  /**
   * Driver verify code.
   * @param account
   * @param code
   */
  @PostMapping(value = "/accounts/{account}/driverVerifyActivationCode/{code}")
  @ResponseBody
  public void driverActivateCode(@PathVariable(value = "account") String account, @PathVariable(value = "code") String code) {
    log.debug("verifyActivationCode={} code={}", account, code);
    accountService.driverVerifyActivationCode(account, code);
  }

  @PostMapping(value = "/accounts/{account}/password/{password}")
  @ResponseBody
  public void setPassword(@PathVariable(value = "account") String account, @PathVariable(value = "password") String password) {
    log.debug("setPassword={}", account);
    accountService.setPasswordAfterActivationCode(account, password);
  }
  
  @PostMapping(value = "/accounts/{account}/password/{password}/profile")//,headers= {"X-API-Version=1.1"})
  @ResponseBody
  public UserDTO setPasswordV1(@PathVariable(value = "account") String account, @PathVariable(value = "password") String password,@RequestBody(required=true) UserDTO dto) {
    log.debug("setPasswordV1={}", account);
    return  accountService.setPasswordAfterActivationCodeV1(account, password,dto);
  }

  @DeleteMapping(value = "/accounts/{account}")
  @ResponseBody
  public UserDTO deactivateCode(@PathVariable(value = "account") String account) {
    log.debug("deactivate={}", account);
    return accountService.deactiveAccount(account);
  }

  @PutMapping(value = "/accounts/reactivate/{account}")
  @ResponseBody
  public UserDTO reActivateCode(@PathVariable(value = "account") String account) {
    log.debug("reactiveAccount={}", account);
    return accountService.reactiveAccount(account);
  }

}
