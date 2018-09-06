package vn.com.omart.backend.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.com.omart.backend.application.response.PoiFollowDTO;
import vn.com.omart.backend.application.util.DateUtils;
import vn.com.omart.backend.domain.model.PoiFollow;
import vn.com.omart.backend.domain.model.PoiFollowRepository;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.domain.model.PointOfInterestRepository;
import vn.com.omart.backend.domain.model.UserProfile;
import vn.com.omart.backend.domain.model.UserProfileRepository;

@Service
public class PoiFollowService {

	@Autowired
	private PoiFollowRepository poiFollowRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private PointOfInterestRepository pointOfInterestRepository;

	public void save(String userId, Long poiId) {
		PoiFollow poiFollow = poiFollowRepository.findByUserUserIdAndPoiId(userId, poiId);
		if (poiFollow == null) {
			UserProfile userProfile = userProfileRepository.findByUserId(userId);
			if (userProfile != null) {
				PointOfInterest poi = pointOfInterestRepository.findOne(poiId);
				if (poi != null) {
					PoiFollow entity = new PoiFollow();
					entity.setPoi(poi);
					entity.setUser(userProfile);
					entity.setCreatedAt(DateUtils.getCurrentDate());
					poiFollowRepository.save(entity);
				}
			}
		}
	}

	public List<PoiFollowDTO> getByUserId(String userId, Pageable pageable) {
		List<PoiFollow> entities = poiFollowRepository.findByUserUserId(userId, pageable);
		if (entities != null) {
			List<PoiFollowDTO> results = entities.stream().map(PoiFollowDTO::toBasicDTO).collect(Collectors.toList());
			return results;
		}
		return new ArrayList<>();
	}
}
