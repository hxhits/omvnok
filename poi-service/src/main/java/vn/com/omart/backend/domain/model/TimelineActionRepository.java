package vn.com.omart.backend.domain.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimelineActionRepository extends JpaRepository<TimelineAction, Long> {

	public TimelineAction findByUserIdAndTimeline(String userId, Timeline timeLine);
	
}
