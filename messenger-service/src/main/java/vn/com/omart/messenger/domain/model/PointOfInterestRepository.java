package vn.com.omart.messenger.domain.model;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointOfInterestRepository extends PagingAndSortingRepository<PointOfInterest, Long> {

}
