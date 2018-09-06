package vn.com.omart.backend.domain.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
  List<District> findByProvinceId(Long provinceId);

  District findByIdAndProvinceId(Long id, Long provinceId);

  District findByName(String name);

  List<District> findByProvinceIdAndNameContaining(Long id, String name);
}
