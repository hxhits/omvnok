package vn.com.omart.backend.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import vn.com.omart.backend.CacheName;
import vn.com.omart.backend.application.response.DistrictDTO;
import vn.com.omart.backend.application.response.ProvinceDTO;
import vn.com.omart.backend.application.response.WardDTO;
import vn.com.omart.backend.domain.model.DistrictRepository;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.domain.model.ProvinceRepository;
import vn.com.omart.backend.domain.model.WardRepository;

@Service
@Slf4j
public class AppCommonService {
	private final ProvinceRepository provinceRepository;
	private final DistrictRepository districtRepository;
	private final WardRepository wardRepository;

	@Autowired
	public AppCommonService(ProvinceRepository provinceRepository, DistrictRepository districtRepository,
			WardRepository wardRepository) {
		this.provinceRepository = provinceRepository;
		this.districtRepository = districtRepository;
		this.wardRepository = wardRepository;
	}

	//@Cacheable(value = CacheName.PROVINCE, key = "#root.methodName")
	public List<ProvinceDTO> getProvinces() {
		return this.provinceRepository.getAllWithout().stream().map(ProvinceDTO::from).collect(Collectors.toList());
	}

	@Cacheable(value = CacheName.DISTRICT, key = "#root.methodName + ':' + #provinceId")
	public List<DistrictDTO> getDistricts(Long provinceId) {
		return this.districtRepository.findByProvinceId(provinceId).stream().map(DistrictDTO::from)
				.collect(Collectors.toList());
	}

	@Cacheable(value = CacheName.WARD, key = "#root.methodName + ':' + #districtId")
	public List<WardDTO> getWards(Long districtId) {
		return this.wardRepository.findByDistrictId(districtId).stream().map(WardDTO::from)
				.collect(Collectors.toList());
	}

	public String getPoiFullAddress(PointOfInterest poi) {
		String fullAddress = poi.getAddress() + ", " + poi.ward().name() + ", " + poi.district().name() + ", "
				+ poi.province().name();
		return fullAddress;
	}

}
