package vn.com.omart.backend.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import vn.com.omart.backend.application.response.HomeFeatureDTO;
import vn.com.omart.backend.domain.model.HomeFeature;
import vn.com.omart.backend.domain.model.HomeFeatureRepository;

@Service
public class HomeFeatureService {

	@Autowired
	private HomeFeatureRepository homeFeatureRepository;

	public List<HomeFeatureDTO> getByApproval() {
		PageRequest pageable = new PageRequest(0, 3);
		List<HomeFeature> entities = homeFeatureRepository.findByIsApproved(true, pageable);
		if (entities != null) {
			List<HomeFeatureDTO> dtos = entities.stream().map(HomeFeatureDTO::toBasicDTO).collect(Collectors.toList());
			return dtos;
		}
		return new ArrayList<>();
	}

	public void createOrUpdateHomeFeature(String userId, HomeFeatureDTO dto) {
		HomeFeature entity = null;
		if (dto.getId() == null || dto.getId() == 0) {
			entity = HomeFeatureDTO.toEntity(dto);
			entity.setCreatedBy(userId);
			entity.setUpdatedBy(userId);
			homeFeatureRepository.save(entity);
		} else {
			entity = homeFeatureRepository.findOne(dto.getId());
			if (entity != null) {
				entity = HomeFeatureDTO.toEntity(entity, dto);
				entity.setUpdatedBy(userId);
				homeFeatureRepository.save(entity);
			}
		}
	}
	
	public List<HomeFeatureDTO> getAll() {
		PageRequest pageable = new PageRequest(0, 3);
		List<HomeFeature> entities = homeFeatureRepository.findAll(pageable).getContent();
		if (entities != null) {
			List<HomeFeatureDTO> dtos = entities.stream().map(HomeFeatureDTO::toFullDTO).collect(Collectors.toList());
			return dtos;
		}
		return new ArrayList<>();
	}

}
