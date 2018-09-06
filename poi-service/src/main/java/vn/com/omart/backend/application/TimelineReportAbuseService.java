package vn.com.omart.backend.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.omart.backend.application.response.TimelineReportAbuseDTO;
import vn.com.omart.backend.application.util.DateUtils;
import vn.com.omart.backend.constants.OmartType.TimeLineReportType;
import vn.com.omart.backend.domain.model.Timeline;
import vn.com.omart.backend.domain.model.TimelineLock;
import vn.com.omart.backend.domain.model.TimelineLockRepository;
import vn.com.omart.backend.domain.model.TimelineReportAbuse;
import vn.com.omart.backend.domain.model.TimelineReportAbuseRepository;
import vn.com.omart.backend.domain.model.TimelineRepository;
import vn.com.omart.backend.domain.model.UserProfile;
import vn.com.omart.backend.domain.model.UserProfileRepository;

@Service
public class TimelineReportAbuseService {

	@Autowired
	private TimelineReportAbuseRepository timelineReportAbuseRepository;

	@Autowired
	private TimelineRepository timelineRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private TimelineLockRepository timelineLockRepository;

	/**
	 * Report abuse.
	 * 
	 * @param userId
	 * @param dto
	 */
	public void reportAbuse(String userId, TimelineReportAbuseDTO dto) {
		this.updateBy(userId, dto.getTimelineId(), dto.getReason(), TimeLineReportType.ABUSE.getId());
	}

	/**
	 * Hide timeline
	 * 
	 * @param userId
	 * @param timelineId
	 */
	public void hideTimeline(String userId, Long timelineId) {
		this.updateBy(userId, timelineId, "", TimeLineReportType.HIDE.getId());
	}

	/**
	 * Update by
	 * 
	 * @param userId
	 * @param timelineId
	 * @param reason
	 * @param timeLineReportType
	 */
	public void updateBy(String userId, Long timelineId, String reason, int timeLineReportType) {
		Timeline timeline = timelineRepository.findOne(timelineId);
		if (timeline != null) {
			UserProfile userFrom = userProfileRepository.findByUserId(userId);
			UserProfile userTo = userProfileRepository.findByUserId(timeline.getUserId());
			if (userFrom != null && userTo != null) {
				TimelineReportAbuse entity = new TimelineReportAbuse();
				entity.setCreatedAt(DateUtils.getCurrentDate());
				entity.setReason(reason);
				entity.setUserFrom(userFrom);
				entity.setUserTo(userTo);
				entity.setTimeline(timeline);
				entity.setReportType(timeLineReportType);
				timelineReportAbuseRepository.save(entity);
			}
		}
	}

	/**
	 * Lock timeline user.
	 * 
	 * @param userId
	 * @param dto
	 */
	public void lockTimeline(String userId, TimelineReportAbuseDTO dto) {
		if (!userId.equals(dto.getUserId())) {
			UserProfile userFrom = userProfileRepository.findByUserId(userId);
			UserProfile userTo = userProfileRepository.findByUserId(dto.getUserId());
			if (userFrom != null && userTo != null) {
				TimelineLock entity = timelineLockRepository.findByUserFromAndUserTo(userFrom.getUserId(),
						userTo.getUserId());
				if (entity == null) {
					entity = new TimelineLock();
				}
				entity.setUserFrom(userFrom.getUserId());
				entity.setUserTo(userTo.getUserId());
				entity.setCreatedAt(DateUtils.getCurrentDate());
				timelineLockRepository.save(entity);
			}
		}
	}

}
