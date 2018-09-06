package vn.com.omart.backend.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.omart.backend.application.response.PoiItemGroupDTO;
import vn.com.omart.backend.domain.model.PoiItemGroup;
import vn.com.omart.backend.domain.model.PoiItemGroupRepository;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.domain.model.PointOfInterestRepository;

@Service
public class PoiItemGroupService {

	@Autowired
	private PoiItemGroupRepository poiItemGroupRepository;

	@Autowired
	private PointOfInterestRepository pointOfInterestRepository;

	@Autowired
	private ItemGroupService itemGroupService;

	public void save(PoiItemGroupDTO dto) {
		PointOfInterest poi = pointOfInterestRepository.findOne(dto.getPoiId());
		if (poi != null) {
			PoiItemGroup entity = PoiItemGroupDTO.toEntity(dto);
			entity.setPoi(poi);
			poiItemGroupRepository.save(entity);
		}
	}

	public List<PoiItemGroupDTO> getGroupsByPoiId(Long poiId, Pageable pageable) {
		List<PoiItemGroup> entities = poiItemGroupRepository.findByPoiId(poiId, pageable);
		if (entities != null) {
			List<PoiItemGroupDTO> results = entities.stream().map(PoiItemGroupDTO::toBasicDTO)
					.collect(Collectors.toList());
			return results;
		}
		return new ArrayList<>();
	}

	public void update(Long id, PoiItemGroupDTO dto) {
		PoiItemGroup entity = poiItemGroupRepository.findOne(id);
		if (entity != null) {
			entity = PoiItemGroupDTO.toEntity(entity, dto);
			poiItemGroupRepository.save(entity);
		}
	}

	@Transactional(readOnly = false)
	public void delete(Long id) {
		PoiItemGroup entity = poiItemGroupRepository.findOne(id);
		if (entity != null) {
			itemGroupService.deleteGroup(id,entity.getPoi().id());
			poiItemGroupRepository.delete(entity);
		}
	}

}
