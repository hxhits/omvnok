package vn.com.omart.backend.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.com.omart.backend.application.response.HomeBannerDTO;
import vn.com.omart.backend.constants.Device;
import vn.com.omart.backend.domain.model.HomeBanner;
import vn.com.omart.backend.domain.model.HomeBannerRepository;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.domain.model.PointOfInterestRepository;
import vn.com.omart.sharedkernel.application.model.error.NotFoundException;

@Service
public class HomeBannerService {

	@Autowired
	private HomeBannerRepository homeBannerRepository;

	@Autowired
	private PointOfInterestRepository pointOfInterestRepository;

	public List<HomeBannerDTO> getByApprovalAndBannerType(Device client, Pageable pageable) {
		int bannerType = 1;
		if (client == Device.android || client == Device.ios) {
			bannerType = 0;
		}
		List<HomeBanner> entities = homeBannerRepository.findByBannerTypeAndIsApprovedOrderByUpdatedAtDesc(bannerType,
				true, pageable);
		if (entities != null) {
			List<HomeBannerDTO> dtos = entities.stream().map(HomeBannerDTO::toBasicDTO).collect(Collectors.toList());
			return dtos;
		}
		return new ArrayList<>();
	}

	public List<HomeBannerDTO> getHomeBannerByPoi(Long poiId) {
		PointOfInterest poi = pointOfInterestRepository.findOne(poiId);
		if (poi != null) {
			List<HomeBanner> homeBanners = homeBannerRepository.findByPoi(poi);
			List<HomeBannerDTO> dtos = homeBanners.stream().map(HomeBannerDTO::toFullDTO).collect(Collectors.toList());
			return dtos;
		}
		return new ArrayList<>();
	}

	public HomeBannerDTO createOrUpdateHomeBanner(String userId, HomeBannerDTO dto) {
		HomeBanner entity = null;
		PointOfInterest poi = pointOfInterestRepository.findOne(dto.getPoiId());
		if (poi != null) {
			if (dto.getId() == null || dto.getId() == 0) {
				List<HomeBanner> homeBanners = homeBannerRepository.findByPoi(poi);
				if (homeBanners.size() < 2) {
					entity = HomeBannerDTO.toEntity(dto);
					entity.setCreatedBy(userId);
					entity.setUpdatedBy(userId);
					entity.setPoi(poi);
					entity = homeBannerRepository.save(entity);
					return HomeBannerDTO.toFullDTO(entity);
				}
			} else {
				entity = homeBannerRepository.findByIdAndPoiAndBannerType(dto.getId(), poi, dto.getBannerType());
				if (entity != null) {
					entity = HomeBannerDTO.toEntity(entity, dto);
					entity.setUpdatedBy(userId);
					entity = homeBannerRepository.save(entity);
					return HomeBannerDTO.toFullDTO(entity);
				}
			}
		} else {
			throw new NotFoundException("poi with id " + dto.getPoiId() + " not found");
		}
		return null;
	}

	public List<HomeBannerDTO> getAll(Pageable pageable) {
		List<HomeBanner> entities = homeBannerRepository.findAll(pageable).getContent();
		if (entities != null) {
			List<HomeBannerDTO> dtos = entities.stream().map(HomeBannerDTO::toFullDTO).collect(Collectors.toList());
			return dtos;
		}
		return new ArrayList<>();
	}
	
	public void approval(Long id,boolean isApproved) {
		HomeBanner entity = homeBannerRepository.findOne(id);
		if(entity!=null) {
			entity.setApproved(isApproved);
			homeBannerRepository.save(entity);
		}
	}
}
