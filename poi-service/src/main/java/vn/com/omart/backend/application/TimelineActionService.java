package vn.com.omart.backend.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.com.omart.backend.application.response.TimelineActionDTO;
import vn.com.omart.backend.application.util.PoiActionStatus;
import vn.com.omart.backend.domain.model.Timeline;
import vn.com.omart.backend.domain.model.TimelineAction;
import vn.com.omart.backend.domain.model.TimelineActionRepository;
import vn.com.omart.backend.domain.model.TimelineRepository;
import vn.com.omart.sharedkernel.application.model.error.NotFoundException;

@Service
public class TimelineActionService {

	@Autowired
	private TimelineActionRepository timelineActionRepository;

	@Autowired
	private TimelineRepository timelineRepository;

	/**
	 * Post a action from user.
	 * 
	 * @param userId
	 * @param dto
	 */
	public void postAction(String userId, TimelineActionDTO dto) {
		Timeline timeline = timelineRepository.findOne(dto.getTimelineId());
		if (timeline == null) {
			throw new NotFoundException("Timeline not found");
		} else {
			TimelineAction entity = null;
			entity = timelineActionRepository.findByUserIdAndTimeline(userId, timeline);
			if (entity != null) {
				if (entity.getActionType() != dto.getAction().getId()) {
					// update
					entity.setActionType(dto.getAction().getId());
					timelineActionRepository.save(entity);

					int likeNumber = timeline.getLikes() + this.getUserAction(dto.getAction());
					timeline.setLikes(likeNumber);
					timelineRepository.save(timeline);
				}
			} else {
				// insert
				dto.setUserId(userId);
				entity = TimelineActionDTO.toEntity(dto);
				entity.setTimeline(timeline);
				timelineActionRepository.save(entity);

				int likeNumber = timeline.getLikes() + this.getUserAction(dto.getAction());
				timeline.setLikes(likeNumber);
				timelineRepository.save(timeline);
			}

		}
	}

	/**
	 * Increasing like to one.
	 * 
	 * @param action
	 * @return 1,-1 or 0.
	 */
	private int getUserAction(PoiActionStatus action) {
		switch (action) {
		case LIKE:
			return 1;
		case DISLIKE:
			return -1;
		}
		return 0;
	}

}
