package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WardRepository extends JpaRepository<Ward, Long> {
	List<Ward> findByDistrictId(Long districtId);

	Ward findByIdAndDistrictId(Long id, Long districtId);

	List<Ward> findByDistrictIdAndProvinceIdAndNameContaining(Long districtId, Long provinceId, String name);
}
