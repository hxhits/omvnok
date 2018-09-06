package vn.com.omart.backend.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.com.omart.backend.application.response.TimelineCommentDTO;
import vn.com.omart.backend.application.response.UserDTO;
import vn.com.omart.backend.domain.model.Timeline;
import vn.com.omart.backend.domain.model.TimelineComment;
import vn.com.omart.backend.domain.model.TimelineCommentRepository;
import vn.com.omart.backend.domain.model.TimelineRepository;
import vn.com.omart.sharedkernel.application.model.error.NotFoundException;

@Service
public class TimelineCommentService {

	@Autowired
	private TimelineCommentRepository timelineCommentRepository;

	@Autowired
	private TimelineRepository timelineRepository;

	/**
	 * Post a comment.
	 * 
	 * @param userId
	 * @param timelineCommentDTO
	 */
	public void postComment(String userId, TimelineCommentDTO timelineCommentDTO) {
		timelineCommentDTO.setUserId(userId);
		Timeline timeline = timelineRepository.findOne(timelineCommentDTO.getTimelineId());
		if (timeline == null) {
			throw new NotFoundException("Timeline not found");
		}
		TimelineComment entity = TimelineCommentDTO.toEntity(timelineCommentDTO);
		entity.setTimeline(timeline);
		timelineCommentRepository.save(entity);
	}

	/**
	 * Get Timeline Comments.
	 * 
	 * @param timelineId
	 * @param pageable
	 * @return List of TimelineCommentDTO
	 */
	public List<TimelineCommentDTO> getTimelineComments(Long timelineId, Pageable pageable) {
		// get comments.
		List<TimelineCommentDTO> tlComments = timelineCommentRepository.getAllByTimelineId(timelineId, pageable)
				.stream().map(TimelineCommentDTO::toDTO).collect(Collectors.toList());
		// get user id list.
		Set<String> cmUserIds = TimelineCommentDTO.getUserIds();
		// retrieve to user get user info.
		List<Object[]> userCMs = timelineRepository.getUserInUserIds(cmUserIds.toArray(new String[cmUserIds.size()]));
		// convert user objects to user DTO.
		List<UserDTO> userCMDTOs = userCMs.stream().map(UserDTO::toDTO).collect(Collectors.toList());
		// set user info into comment.
		tlComments.stream().forEach(comment -> {
			for (UserDTO userC : userCMDTOs) {
				if (userC.getUserId().equals(comment.getUserId())) {
					comment.setUser(userC);
				}
			}
		});
		return tlComments;

	}
}
