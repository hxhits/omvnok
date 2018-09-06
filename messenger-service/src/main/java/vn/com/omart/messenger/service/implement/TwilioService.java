package vn.com.omart.messenger.service.implement;

import java.util.Arrays;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.authy.AuthyApiClient;
import com.authy.AuthyException;
import com.authy.api.Params;
import com.authy.api.Verification;
import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.http.HttpMethod;
import com.twilio.jwt.accesstoken.AccessToken;
import com.twilio.jwt.accesstoken.ChatGrant;
import com.twilio.jwt.accesstoken.VideoGrant;
import com.twilio.jwt.accesstoken.VoiceGrant;
import com.twilio.rest.api.v2010.account.NewKey;
import com.twilio.rest.chat.v2.service.Channel;
import com.twilio.rest.chat.v2.service.Channel.ChannelType;
import com.twilio.rest.chat.v2.service.User;
import com.twilio.rest.chat.v2.service.channel.Member;
import com.twilio.rest.chat.v2.service.channel.Message;
import com.twilio.rest.video.v1.Room;
import com.twilio.rest.video.v1.Room.RoomStatus;
import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Client;
import com.twilio.twiml.voice.Client.Event;
import com.twilio.twiml.voice.Dial;
import com.twilio.twiml.voice.Say;

import vn.com.omart.messenger.common.constant.Device;

/**
 * Twilio Service.
 *
 * @author Win10
 *
 */
@Service
public class TwilioService {

	private final Logger logger = LoggerFactory.getLogger(TwilioService.class);

	@Autowired
	private AuthyApiClient authyApiClient;

	@Value("${messenger.twilio.voice.webhook.url}")
	private String Webhook_Url;

	@Value("${messenger.twilio.account-sid}")
	private String Account_Sid;

	@Value("${messenger.twilio.auth-token}")
	private String Auth_Token;

	@Value("${messenger.twilio.api-key-sid}")
	private String Api_Key_Sid;

	@Value("${messenger.twilio.api-key-secret}")
	private String Api_Key_Secret;

	@Value("${messenger.twilio.service-sid}")
	private String Service_Sid;

	@Value("${messenger.twilio.push-credential-sid.android}")
	private String Push_Credential_Sid_Android;

	@Value("${messenger.twilio.push-credential-sid.ios}")
	private String Push_Credential_Sid_Ios;

	@Value("${messenger.twilio.role-sid-client}")
	private String Role_Sid_Client;

	@Value("${messenger.twilio.application-sid}")
	private String Application_Sid;

	@Value("${messenger.twilio.voice.push-credential-sid.android}")
	private String Push_Voice_Credential_Sid_Android;

	@Value("${messenger.twilio.voice.push-credential-sid.ios}")
	private String Push_Voice_Credential_Sid_Ios;

	/**
	 * Create a room.
	 *
	 * @return Room
	 */
	public Room roomGenerator() {
		// Initialize the client
		Twilio.init(Account_Sid, Auth_Token);
		// Room name make sure unique.
		String roomName = UUID.randomUUID().toString();
		Room room = Room.creator().setUniqueName(roomName).create();
		return room;
	}

	/**
	 * Complete a room.
	 *
	 * @param roomSid
	 * @return RoomStatus
	 */
	public RoomStatus videoCompleted(String roomSid) {
		Twilio.init(Api_Key_Sid, Api_Key_Secret);
		Room room = Room.updater(roomSid, RoomStatus.COMPLETED).update();
		return room.getStatus();
	}

	/**
	 * Create token for video call.
	 *
	 * @param roomName
	 * @param userId
	 * @return token
	 */
	public String videoTokenGenerator(String roomName, String userId) {
		// Create a Video Grant
		final VideoGrant grant = new VideoGrant();
		grant.setRoom(roomName);
		// Create an Access Token
		final AccessToken token = new AccessToken.Builder(Account_Sid, Api_Key_Sid, Api_Key_Secret)
				// Set the Identity of this token
				.identity(userId)
				// Grant access to Video
				.grant(grant).build();
		// Serialize the token as a JWT
		final String jwt = token.toJwt();
		return jwt;
	}

	/**
	 * Create token for video call.
	 *
	 * @param roomName
	 * @return token
	 */
	public String videoTokenGenerator(String roomName) {
		// Create a Video Grant
		final VideoGrant grant = new VideoGrant();
		grant.setRoom(roomName);
		// Create an Access Token
		final AccessToken token = new AccessToken.Builder(Account_Sid, Api_Key_Sid, Api_Key_Secret)
				// Set the Identity of this token
				.identity(UUID.randomUUID().toString())
				// Grant access to Video
				.grant(grant).build();
		// Serialize the token as a JWT
		final String jwt = token.toJwt();
		return jwt;
	}

	/**
	 * auto generate api key.
	 */
	public void keyGenerator() {
		NewKey key = NewKey.creator().setFriendlyName("omart-vn").create();
		System.out.println("\n=============================== " + key.getSid());
		System.out.println("\n=============================== " + key.getSecret());
	}

	/**
	 * Chat Token Generator.
	 *
	 * @param identity
	 * @param deviceId
	 * @param device
	 * @return token
	 */
	public String chatTokenGenerator(String identity, String deviceId, Device device) {
		// String endpointId = appName + ":" + identity + ":"+deviceId;
		String endpointId = "Omartvn" + ":" + identity;
		ChatGrant grant = new ChatGrant();
		grant.setEndpointId(endpointId);
		grant.setServiceSid(Service_Sid);
		if (Device.android == device) {
			// android.
			grant.setPushCredentialSid(Push_Credential_Sid_Android);
		} else if (Device.ios == device) {
			// ios.
			grant.setPushCredentialSid(Push_Credential_Sid_Ios);
		}
		AccessToken token = new AccessToken.Builder(Account_Sid, Api_Key_Sid, Api_Key_Secret).identity(identity)
				.ttl(86400) // expire access token
				.grant(grant).build();
		return token.toJwt();
	}

	/**
	 * Channel Generator.
	 *
	 * @return Channel
	 */
	public String channelGenerator(String friendlyName, String uniqueName) {
		// Initialize the client
		Twilio.init(Account_Sid, Auth_Token);
		// Create the channel
		Channel channel = Channel.creator(Service_Sid).setFriendlyName(friendlyName).setUniqueName(uniqueName)
				.setType(ChannelType.PRIVATE).create();
		String channelSid = channel.getSid();
		Channel channel1 = Channel.fetcher(Service_Sid, "").fetch();
		return channel.getUniqueName();
	}

	/**
	 * Add User To Channel.
	 *
	 * @param channelSid
	 * @param userId
	 */
	public void addUserToChannel(String channelSid, String userId) {
		// Initialize the client
		Twilio.init(Account_Sid, Auth_Token);
		// Add a Member to the channel
		Member member = Member.creator(Service_Sid, channelSid, userId).create();
	}

	/**
	 * TODO Get List All Messages.
	 *
	 * @param channelSid
	 */

	public void getListAllMessagesByChannelSid(String channelSid) {
		// Initialize the client
		Twilio.init(Account_Sid, Auth_Token);
		// List the messages in the channel
		ResourceSet<Message> messages = Message.reader(Service_Sid, channelSid).read();
		for (Message message : messages) {
			System.out.println(message.getBody());
		}
		ResourceSet<com.twilio.rest.chat.v2.Service> services = com.twilio.rest.chat.v2.Service.reader().read();
	}

	/**
	 * Create user.
	 *
	 * @param userId
	 * @param userName
	 */
	public void createUser(String userId, String userName) {
		// Initialize the client
		Twilio.init(Account_Sid, Auth_Token);
		// Create the user
		User user = User.creator(Service_Sid, userId).setFriendlyName(userName).setRoleSid(Role_Sid_Client).create();
	}

	/**
	 * Voice call generator token.
	 * 
	 * @param userId
	 * @param device
	 * @return token
	 */
	public String voiceTokenGenerator(String userId, Device device) {
		// Create Voice grant.
		VoiceGrant grant = new VoiceGrant();
		grant.setOutgoingApplicationSid(Application_Sid);
		if (Device.android == device) {
			// android.
			grant.setPushCredentialSid(Push_Voice_Credential_Sid_Android);
		} else if (Device.ios == device) {
			// ios.
			grant.setPushCredentialSid(Push_Voice_Credential_Sid_Ios);
		}
		// Create access token
		AccessToken token = new AccessToken.Builder(Account_Sid, Api_Key_Sid, Api_Key_Secret).identity(userId)
				.grant(grant).build();

		return token.toJwt();
	}

	/**
	 * Make a voice call.
	 * 
	 * @param from
	 * @param to
	 * @return twilio app xml
	 */
	public String call(String from, String to) {
		String CALLER_ID = "client:" + from;
		VoiceResponse voiceResponse;
		String toXml = null;
		if (to == null || to.isEmpty()) {
			Say say = new Say.Builder("Congratulations! You have made your first call! Good bye.").build();
			voiceResponse = new VoiceResponse.Builder().say(say).build();
		} else {
			Client client = new Client.Builder(to).statusCallback(Webhook_Url).statusCallbackMethod(HttpMethod.GET)
					.statusCallbackEvents(
							Arrays.asList(Event.INITIATED, Event.RINGING, Event.ANSWERED, Event.COMPLETED))
					.build();
			Dial dial = new Dial.Builder().callerId(CALLER_ID).client(client).build();
			voiceResponse = new VoiceResponse.Builder().dial(dial).build();
		}
		try {
			toXml = voiceResponse.toXml();
		} catch (TwiMLException e) {
			e.printStackTrace();
		}
		return toXml;
	}

	@Deprecated
	public String call(String to) {
		String CALLER_ID = "client:quick_start";
		VoiceResponse voiceResponse;
		String toXml = null;
		if (to == null || to.isEmpty()) {
			Say say = new Say.Builder("Congratulations! You have made your first call! Good bye.").build();
			voiceResponse = new VoiceResponse.Builder().say(say).build();
		} else {

			Client client = new Client.Builder(to).statusCallback(Webhook_Url).statusCallbackMethod(HttpMethod.GET)
					.statusCallbackEvents(
							Arrays.asList(Event.INITIATED, Event.RINGING, Event.ANSWERED, Event.COMPLETED))
					.build();
			Dial dial = new Dial.Builder().callerId(CALLER_ID).client(client).build();
			voiceResponse = new VoiceResponse.Builder().dial(dial).build();
		}
		try {
			toXml = voiceResponse.toXml();
		} catch (TwiMLException e) {
			e.printStackTrace();
		}
		return toXml.toString();
	}

	public boolean isStartSMSVerification(String countryCode, String phoneNumber, String via, String locale) {
		Params params = new Params();
		params.setAttribute("code_length", "6");
		params.setAttribute("locale", locale);
		// params.setAttribute("custom_code", customCode);
		try {
			Verification verification = authyApiClient.getPhoneVerification().start(phoneNumber, countryCode, via,
					params);
			if (!verification.isOk()) {
				logger.error("Error requesting phone verification: " + verification.getMessage());
			} else {
				return true;
			}
		} catch (AuthyException e) {
			// TODO Auto-generated catch block
			logger.error("AuthyException requesting phone verification: " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public boolean isSMSVerify(String countryCode, String phoneNumber, String token) {
		try {
			Verification verification = authyApiClient.getPhoneVerification().check(phoneNumber, countryCode, token);
			if (!verification.isOk()) {
				logger.error("Error verifying token. " + verification.getMessage());
			} else {
				return true;
			}
		} catch (AuthyException e) {
			// TODO Auto-generated catch block
			logger.error("AuthyException verifying token. " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

}
