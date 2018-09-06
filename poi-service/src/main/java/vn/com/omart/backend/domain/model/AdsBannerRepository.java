package vn.com.omart.backend.domain.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdsBannerRepository extends JpaRepository<AdsBanner, Long> {
    List<AdsBanner> findAllByCategory(Category category);
}
