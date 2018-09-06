package vn.com.omart.backend.domain.model;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TimelineCommentRepository extends JpaRepository<TimelineComment, Long> {

	@Query("SELECT tlcm FROM TimelineComment tlcm WHERE tlcm.timeline.id = :timelineId")
	public List<TimelineComment> getAllByTimelineId(@Param("timelineId") Long timelineId, Pageable pageable);

}
