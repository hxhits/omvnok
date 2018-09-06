package vn.com.omart.backend.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.omart.backend.application.response.HomeBannerDTO;
import vn.com.omart.backend.application.response.HomeFeatureDTO;
import vn.com.omart.backend.application.util.DateUtils;
import vn.com.omart.backend.domain.model.HomeBanner;
import vn.com.omart.backend.domain.model.HomeBannerRepository;
import vn.com.omart.backend.domain.model.HomeFeature;
import vn.com.omart.backend.domain.model.HomeFeatureRepository;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.domain.model.PointOfInterestRepository;

@Service
public class AdminToolResourceService {
	
	@Autowired
	private HomeFeatureRepository homeFeatureRepository;

	@Autowired
	private HomeBannerRepository homeBannerRepository;

	@Autowired
	private PointOfInterestRepository pointOfInterestRepository;

	
	
	
}
