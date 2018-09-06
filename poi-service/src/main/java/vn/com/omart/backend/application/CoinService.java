package vn.com.omart.backend.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.omart.backend.application.util.DateUtils;
import vn.com.omart.backend.domain.model.OmartCoin;
import vn.com.omart.backend.domain.model.OmartCoinRepository;
import vn.com.omart.backend.domain.model.UserProfile;
import vn.com.omart.backend.domain.model.UserProfileRepository;

@Service
public class CoinService {

	@Autowired
	private OmartCoinRepository omartCoinRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Transactional(readOnly = false)
	public void calculateMartCoin(UserProfile userProfile, int coin) {
		if (coin >= 100) {
			OmartCoin entity = omartCoinRepository.findByUserProfile(userProfile);
			if (entity != null) {
				int martCoin = coin / 100;
				martCoin = martCoin + entity.getMartCoin();
				entity.setMartCoin(martCoin);
				entity.setUpdatedAt(DateUtils.getCurrentDate());
			} else {
				int martCoin = coin / 100;
				entity = new OmartCoin();
				entity.setMartCoin(martCoin);
				entity.setUserProfile(userProfile);
				entity.setUserId(userProfile.getUserId());
				entity.setCreatedAt(DateUtils.getCurrentDate());
			}
			omartCoinRepository.save(entity);
			// update coin.
			userProfile.setCoin((coin % 100));
			userProfileRepository.save(userProfile);
		} else {
			userProfile.setCoin(coin);
			userProfileRepository.save(userProfile);
		}
	}
}
