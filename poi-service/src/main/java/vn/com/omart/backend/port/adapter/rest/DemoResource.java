package vn.com.omart.backend.port.adapter.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.backend.domain.model.OmartCoin;
import vn.com.omart.backend.domain.model.OmartCoinRepository;
import vn.com.omart.backend.domain.model.TimelineRepository;
import vn.com.omart.backend.domain.model.UserProfile;
import vn.com.omart.backend.domain.model.UserProfileRepository;


@RestController
@RequestMapping("/v1/demo")
public class DemoResource {
	
	@Autowired
	private UserProfileRepository userProfileRepository;
	
//	@Autowired
//	private OmartCoinRepository coinRepository;
	
	private UserProfile getUser(String userId) {
		UserProfile userProfile =  userProfileRepository.findByUserId(userId);
		if(userProfile!=null) {
			return userProfile;
		}
		return null;
	}
	
	@GetMapping(value="/finances")
	public List<JSONObject> getFinances(@RequestHeader(value = "X-User-Id", required = true) String userId) throws JSONException {
		List<JSONObject> finances = new ArrayList<>();
		List<String> currencies = Arrays.asList(
				"Tiền mặt,2.500.000 VND,VND",
				"ATM,278.000 VND,VND",
				"Martcoin,18 MTC,MTC",
				"Bitcoin,0.12 BTC,BTC",
				"Bitcoin Cash,0.12 BCH,BCH",
				"Litecoin,16 LTC,LTC",
				"Ethereum,0.5 ETH,ETH",
				"Ripple,112 XRP,XRP",
				"Master Card,89 USD,USD",
				"Visa,125 USD,USD",
				"Factom,28 FCT,FCT",
				"Monero,13 XMR,XMR",
				"Vietcombank,200.000 VND,VND",
				"BIDV,96.000 VND,VND",
				"VietinBank,778.000 VND,VND");
		String userName = "Nguyễn Ngọc Trinh";
		String omartCoin = "0 MTC";
		String coin = "0 MTC";
		UserProfile user = this.getUser(userId);
	//	OmartCoin coin =  coinRepository.findByUserProfile(user);
		if( user !=null) {
			userName = user.getName();
			coin = user.getCoin() + " Coin";
			if( user.getOmartCoin()!=null) {
				omartCoin = user.getOmartCoin().getMartCoin()+" MTC";
			}
		}
	
		for (String item : currencies){
			JSONObject jsOb = new JSONObject();
			JSONObject detail = new JSONObject();
			String []values = item.split(",");
			
			detail.put("accountHolder", userName);
			detail.put("accountNumber", "150230440305");
			detail.put("freezeMoney", "0 "+values[2]);
			detail.put("currencyCode", ""+values[2]);

			if(values[0].equalsIgnoreCase("Martcoin")) {
				jsOb.put("balance",omartCoin);
				detail.put("currentBalance",coin);
				detail.put("availableBalance",omartCoin);
			}else {
				jsOb.put("balance",values[1]);
				detail.put("currentBalance", "5000 "+values[2]);
				detail.put("availableBalance", "780 "+values[2]);
			}
			jsOb.put("currency",values[0]);
			jsOb.put("detail",detail);
			finances.add(jsOb);
		}
		return finances;
	}
	
	@Autowired
	private TimelineRepository timelineRepository;
	
	@GetMapping(value="/user/{user-id}")
	public Object ggg(@PathVariable(value="user-id",required=true) String userId) {
		Object y = timelineRepository.getUserActive(userId);
		Object x = y;
		return x;
	}
	
}
