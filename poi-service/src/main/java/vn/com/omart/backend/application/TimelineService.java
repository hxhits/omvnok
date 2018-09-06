package vn.com.omart.backend.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.com.omart.backend.application.response.PointOfInterestDTO;
import vn.com.omart.backend.application.response.StoreProcedureParams;
import vn.com.omart.backend.application.response.TimelineCommentDTO;
import vn.com.omart.backend.application.response.TimelineDTO;
import vn.com.omart.backend.application.response.TimelineHistoryDTO;
import vn.com.omart.backend.application.response.UserDTO;
import vn.com.omart.backend.application.util.CommonUtils;
import vn.com.omart.backend.constants.ConstantUtils;
import vn.com.omart.backend.constants.Device;
import vn.com.omart.backend.constants.OmartType;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.domain.model.PointOfInterestRepository;
import vn.com.omart.backend.domain.model.Timeline;
import vn.com.omart.backend.domain.model.TimelineAction;
import vn.com.omart.backend.domain.model.TimelineComment;
import vn.com.omart.backend.domain.model.TimelineRepository;
import vn.com.omart.backend.domain.model.UserFriend;
import vn.com.omart.backend.domain.model.UserFriendRepository;
import vn.com.omart.backend.domain.model.UserProfileRepository;
import vn.com.omart.sharedkernel.application.model.dto.PageDTO;
import vn.com.omart.sharedkernel.application.model.error.NotFoundException;

@Service
public class TimelineService {

	private static final String TIMELINE_TYPES_STAFT_USER_ADMIN = OmartType.TimelineType.STAFT.getId() + ","
			+ OmartType.TimelineType.USER.getId() + "," + OmartType.TimelineType.ADMIN.getId();

	@Autowired
	private TimelineRepository timelineRepository;

	@Autowired
	private StoredProcedureService storedProcedureService;

	@Autowired
	private ResourceLoaderService resourceLoaderService;

	@Value("${share.path.timeline}")
	private String Html_Share_Timeline_Content;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private PointOfInterestRepository pointOfInterestRepository;

	@Autowired
	private UserFriendRepository userFriendRepository;

	/**
	 * Create timeline.
	 * 
	 * @param userId
	 * @param dto
	 * @param device
	 *            default mobile platform
	 * @return
	 */
	public TimelineDTO createTimeline(String userId, TimelineDTO dto, Device device) {
		Object user = timelineRepository.getUserActive(userId);
		if (user != null) {
			dto.setUserId(userId);
			Timeline entity = TimelineDTO.toEntity(dto);
			if (dto.getPoiId() > 0) {
				PointOfInterest poi = pointOfInterestRepository.findOne(dto.getPoiId());
				if (poi != null) {
					entity.setPoiId(dto.getPoiId());
					// entity.setTimelineType(3);// 3 = shop post
				}
			}
			entity.setCreatedAt(new Date());
			try {
				entity = timelineRepository.save(entity);
			} catch (Exception e) {
				// handle if emoij not support.
				String desc = CommonUtils.emoijNormalize(entity.getDescription());
				entity.setDescription(desc);
				timelineRepository.save(entity);
			}
			if (device == Device.web) {
				entity.setTimelineComments(new ArrayList<TimelineComment>());
				entity.setTimelineActions(new ArrayList<TimelineAction>());
				List<Timeline> timelines = new ArrayList<>();
				timelines.add(entity);
				return this.addTimelineInfo(userId, timelines).get(0);
			} else {
				return new TimelineDTO(entity.getId());
			}
		} else {
			throw new NotFoundException("\n User " + userId + " have not activated yet");
		}
	}

	/**
	 * Store love hide history.
	 * 
	 * @param dto
	 */
	public void storeLoveHideHistory(TimelineDTO dto) {
		Timeline entity = timelineRepository.findOne(dto.getId());
		if (entity != null) {
			entity.setPlaceName(dto.getPlaceName());
			entity.setSaveHistory(true);
			timelineRepository.save(entity);
		}
	}

	/**
	 * Get love hide histories.
	 * 
	 * @param userId
	 * @param pageable
	 * @return List of TimelineHistoryDTO
	 */
	public List<TimelineHistoryDTO> getLoveHideHistories(String userId, Pageable pageable) {
		List<Timeline> entities = timelineRepository
				.findByUserIdAndIsDeletedAndIsMomentAndIsSaveHistoryOrderByCreatedAtDesc(userId, false, true, true,
						pageable);
		List<TimelineHistoryDTO> tlHistories = new ArrayList<>();
		for (Timeline entity : entities) {
			TimelineHistoryDTO dto = new TimelineHistoryDTO();
			dto.setId(entity.getId());
			dto.setPlaceName(entity.getPlaceName());
			dto.setImageCounts(entity.getImages().size());
			if (entity.getVideos() != null) {
				dto.setVideoCounts(entity.getVideos().size());
			} else {
				dto.setVideoCounts(0);
			}
			dto.setLatitude(entity.getLatitude());
			dto.setLongitude(entity.getLongitude());
			tlHistories.add(dto);
		}
		return tlHistories;
	}

	/**
	 * Update is deleted timeline.
	 * 
	 * @param timelineId
	 * @param dto
	 */
	public void updatIsDeletedTimeline(Long timelineId, TimelineDTO dto) {
		Timeline entity = timelineRepository.findOne(timelineId);
		if (entity != null) {
			entity.setDeleted(dto.isDeleted());
			entity.setUpdatedAt(new Date());
			timelineRepository.save(entity);
		}
	}

	/**
	 * Update Moment Radius.
	 * 
	 * @param timelineId
	 * @param dto
	 */
	public void updateLoveHideRadius(Long timelineId, TimelineDTO dto) {
		Timeline entity = timelineRepository.findOne(timelineId);
		if (entity != null) {
			entity.setMomentRadius(dto.getMomentRadius());
			timelineRepository.save(entity);
		}
	}

	/**
	 * Update is private/public timeline.
	 * 
	 * @param timelineId
	 * @param dto
	 */
	public void updateIsPrivatedTimeline(Long timelineId, TimelineDTO dto) {
		Timeline entity = timelineRepository.findOne(timelineId);
		if (entity != null) {
			entity.setPrivated(dto.isPrivated());
			entity.setUpdatedAt(new Date());
			timelineRepository.save(entity);
		}
	}

	/**
	 * Update
	 * 
	 * @param timelineId
	 * @param dto
	 */
	public void update(Long timelineId, TimelineDTO dto) {
		Timeline entity = timelineRepository.findOne(timelineId);
		if (entity != null) {
			Timeline timeline = TimelineDTO.toEntity(dto);
			timeline.setUpdatedAt(new Date());
			timeline.setDeleted(entity.isDeleted());
			timeline.setComments(entity.getComments());
			timeline.setLikes(entity.getLikes());
			timeline.setCreatedAt(entity.getCreatedAt());
			timeline.setUserId(entity.getUserId());
			timeline.setTimelineType(entity.getTimelineType());
			timeline.setPlaceName(entity.getPlaceName());
			timeline.setSaveHistory(entity.isSaveHistory());
			timelineRepository.save(timeline);
		}
	}

	/**
	 * Get all timeline.
	 * 
	 * @param pageable
	 * @return List of TimelineDTO
	 */
	public List<TimelineDTO> getAllByIsDeleted(String userId, Pageable pageable) {
		// false only for data are not deleted
		List<Timeline> timelineEntitys = timelineRepository.getAllByIsDeleted(false, pageable);
		if (timelineEntitys != null) {
			List<TimelineDTO> timelineDTOs = timelineEntitys.stream()
					.map(timeline -> TimelineDTO.toDTO(userId, timeline)).collect(Collectors.toList());
			// get user timeline.
			Set<String> tlUserIds = TimelineDTO.getUserIds();
			List<Object[]> userTLs = timelineRepository
					.getUserInUserIds(tlUserIds.toArray(new String[tlUserIds.size()]));
			List<UserDTO> userTLDTOs = userTLs.stream().map(UserDTO::toDTO).collect(Collectors.toList());

			// get user comment.
			Set<String> cmUserIds = TimelineCommentDTO.getUserIds();
			List<Object[]> userCMs = new ArrayList<>();
			if (!cmUserIds.isEmpty()) {
				userCMs = timelineRepository.getUserInUserIds(cmUserIds.toArray(new String[cmUserIds.size()]));
			}
			List<UserDTO> userCMDTOs = userCMs.stream().map(UserDTO::toDTO).collect(Collectors.toList());
			timelineDTOs.forEach(item -> {
				// set user of timeline.
				if (item.getUser() == null) {
					for (UserDTO userTL : userTLDTOs) {
						if (userTL.getUserId().equals(item.getUserId())) {
							item.setUser(userTL);
						}
					}
				}

				// set user of timeline comment.
				item.getTimelineComments().forEach(itemComment -> {
					for (UserDTO userC : userCMDTOs) {
						if (userC.getUserId().equals(itemComment.getUserId())) {
							itemComment.setUser(userC);
						}
					}
				});
			});
			return timelineDTOs;
		}
		return new ArrayList<>();
	}

	/*
	 * Pending.
	 */
	public List<TimelineDTO> getTimelines1(String userId, Double latitude, Double longitude, int radius,
			Pageable pageable) {
		int sysSize = timelineRepository.countByTimelineType(0);
		List<Timeline> result = null;
		if (sysSize > 0) {
			double sumPage = Math.floor(sysSize / pageable.getPageSize());
			// get only timeline of user system.
			List<Timeline> sysTimelines = timelineRepository.findByIsDeletedAndTimelineTypeOrderByCreatedAtDesc(false,
					0, pageable);
			if (sysTimelines.size() < pageable.getPageSize() && (latitude > 0.0 && longitude > 0.0)) {
				int pNumReq = (int) Math.abs(sumPage - pageable.getPageNumber());
				PageRequest pageRequest = new PageRequest(pNumReq, pageable.getPageSize());
				// get only timeline of user.
				List<Timeline> userTimelines = getAllByIsNotDeletedAndRadius(userId, TIMELINE_TYPES_STAFT_USER_ADMIN,
						latitude, longitude, radius, pageRequest);
				// merger timeline.
				result = Stream.concat(sysTimelines.stream(), userTimelines.stream()).collect(Collectors.toList());

			} else {
				result = sysTimelines;
			}
		} else {
			result = getAllByIsNotDeletedAndRadius(userId, TIMELINE_TYPES_STAFT_USER_ADMIN, latitude, longitude, radius,
					pageable);
		}
		// sort created at desc
		Collections.sort(result, (new Comparator<Timeline>() {

			@Override
			public int compare(Timeline o1, Timeline o2) {
				// TODO Auto-generated method stub
				return o1.getCreatedAt().compareTo(o2.getCreatedAt());
			}
		}).reversed());

		// add likes, comments, comment text and user avatar.
		return this.addTimelineInfo(userId, result);
	}

	/**
	 * Get Timelines.
	 * 
	 * @param userId
	 * @param latitude
	 * @param longitude
	 * @param radius
	 * @param pageable
	 * @return
	 */
	public List<TimelineDTO> getTimelines(String userId, Double latitude, Double longitude, int radius,
			Pageable pageable) {
		// have location.

		List<Timeline> result = null;
		if (latitude > 0.0 && longitude > 0.0) {
			result = getAllByIsNotDeletedAndRadius(userId, TIMELINE_TYPES_STAFT_USER_ADMIN, latitude, longitude,
					ConstantUtils.SYS_RADIUS, pageable);
			// sort created at desc.
			// CommonUtils.metersToMiles(ConstantUtils.SYS_RADIUS);
			Collections.sort(result, (new Comparator<Timeline>() {
				@Override
				public int compare(Timeline o1, Timeline o2) {
					return o1.getCreatedAt().compareTo(o2.getCreatedAt());
				}
			}).reversed());
			// add likes, comments, comment text and user avatar.
			return this.addTimelineInfo(userId, result);
		}

		// do not have location.
		result = timelineRepository.getTimelineOrderByCreatedAtDesc(false, userId, false, false, pageable);
		if (result.isEmpty())
			result = null;
		return this.addTimelineInfo(userId, result);
	}

	public List<TimelineDTO> getTimelines(String userId, Pageable pageable) {
		try {
			List<Timeline> result = null;
			// for your self
			String friendStr = userId;
			List<UserFriend> friends = userFriendRepository.getByUserId(userId);
			if (!friends.isEmpty()) {
				for (UserFriend item : friends) {
					if (!item.getFriend().getUserId().equals(userId)) {
						friendStr += "," + item.getFriend().getUserId();
					} else {
						friendStr += "," + item.getUser().getUserId();
					}
				}
			}
			result = getAllByIsNotDeletedAndRadiusV5(userId, friendStr, pageable);
			// sort created at desc.
			Collections.sort(result, (new Comparator<Timeline>() {
				@Override
				public int compare(Timeline o1, Timeline o2) {
					return o1.getCreatedAt().compareTo(o2.getCreatedAt());
				}
			}).reversed());
			// add likes, comments, comment text and user avatar.
			return this.addTimelineInfo(userId, result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public List<TimelineDTO> getTimelinesByUserId(String userId, Pageable pageable) {
		List<Timeline> result = timelineRepository.getTimelinesByUserId(userId, pageable);
		if (result.isEmpty())
			result = null;
		return this.addTimelineInfo(userId, result);
	}

	/**
	 * Get All By Is Not Deleted And Radius.
	 * 
	 * @param latitude
	 * @param longitude
	 * @param radius
	 * @param pageable
	 * @return List of Timeline
	 */
	public List<Timeline> getAllByIsNotDeletedAndRadius(String userId, String timelineTypes, Double latitude,
			Double longitude, int radius, Pageable pageable) {
		StoreProcedureParams params = new StoreProcedureParams();
		params.setUserId(userId);
		params.setTimelineTypes(timelineTypes);
		params.setLatitude(latitude);
		params.setLongitude(longitude);
		params.setPage(pageable.getPageNumber());
		params.setSize(pageable.getPageSize());
		params.setRadius(radius);
		List<Timeline> entitys = storedProcedureService.getTimelineStoredProcedureQuery("geo_dist_timeline_v4", params);
		return entitys;
	}

	public List<Timeline> getAllByIsNotDeletedAndRadiusV5(String userId, String friendStr, Pageable pageable) {
		StoreProcedureParams params = new StoreProcedureParams();
		params.setUserId(userId);
		params.setFriendStr(friendStr);
		params.setPage(pageable.getPageNumber());
		params.setSize(pageable.getPageSize());
		List<Timeline> entitys = storedProcedureService.getTimelineStoredProcedureQueryV5("geo_dist_timeline_v5",
				params);
		return entitys;
	}

	/**
	 * Get My Timelines.
	 * 
	 * @param userId
	 * @param latitude
	 * @param longitude
	 * @param pageable
	 * @return List of TimelineDTO
	 */
	public List<TimelineDTO> getMyTimelines(String userId, Double latitude, Double longitude, Pageable pageable) {
		List<Timeline> result = null;
		// has location.
		if (latitude > 0.0 && longitude > 0.0) {
			// get only timeline of user with radius.
			List<Timeline> momentTimelines = getAllByIsNotDeletedAndRadiusMyTL_V3(userId, ConstantUtils.USER_TIMELINE,
					latitude, longitude, pageable);
			// merge.
			if (momentTimelines.size() < pageable.getPageSize()) {
				// caculate normal timeline pagination.
				int totalLoveHideSize = timelineRepository.countByIsMoment(userId);
				int pageLH = totalLoveHideSize / pageable.getPageSize();
				int pageTL = pageable.getPageNumber() - pageLH;
				if (pageTL < 0) {
					pageTL = 0;
				}
				PageRequest pageRequest = new PageRequest(pageTL, pageable.getPageSize());

				List<Timeline> timelines = timelineRepository
						.findByUserIdAndIsDeletedAndIsMomentOrderByCreatedAtDesc(userId, false, false, pageRequest);
				result = Stream.concat(momentTimelines.stream(), timelines.stream()).collect(Collectors.toList());
			} else {
				result = momentTimelines;
			}
		} else {
			// do not have location.
			result = timelineRepository.findByIsMomentAndIsDeletedAndUserIdOrderByCreatedAtDesc(false, false, userId,
					pageable);
		}

		if (result.isEmpty())
			result = null;
		return this.addTimelineInfo(userId, result);
	}

	/**
	 * Get My Timelines [TEMPORARY FIX]
	 * 
	 * @param userId
	 * @param latitude
	 * @param longitude
	 * @param pageable
	 * @return List of TimelineDTO
	 */
	public List<TimelineDTO> getMyTimelinesFIXTemporary(String userId, Double latitude, Double longitude,
			Pageable pageable) {
		List<Timeline> result = null;
		// has location.
		if (latitude > 0.0 && longitude > 0.0) {
			if (pageable.getPageNumber() > 0) {
				return new ArrayList<>();
			}
			PageRequest pageRequest = new PageRequest(0, Integer.MAX_VALUE);
			List<Timeline> momentTimelines = getAllByIsNotDeletedAndRadiusMyTL_V3(userId, ConstantUtils.USER_TIMELINE,
					latitude, longitude, pageRequest);

			List<Timeline> timelines = timelineRepository
					.findByUserIdAndIsDeletedAndIsMomentOrderByCreatedAtDesc(userId, false, false, pageRequest);
			result = Stream.concat(momentTimelines.stream(), timelines.stream()).collect(Collectors.toList());

		} else {
			// do not have location.
			result = timelineRepository.findByIsMomentAndIsDeletedAndUserIdOrderByCreatedAtDesc(false, false, userId,
					pageable);
		}

		if (result.isEmpty())
			result = null;
		return this.addTimelineInfo(userId, result);
	}

	/**
	 * Get All By Is Not Deleted And Radius.
	 * 
	 * @param latitude
	 * @param longitude
	 * @param radius
	 * @param pageable
	 * @return List of Timeline
	 */
	public List<Timeline> getAllByIsNotDeletedAndRadiusMyTL(String userId, int type, Double latitude, Double longitude,
			int radius, Pageable pageable) {
		StoreProcedureParams params = new StoreProcedureParams();
		params.setUserId(userId);
		params.setTimelineTypes(String.valueOf(type));
		params.setLatitude(latitude);
		params.setLongitude(longitude);
		params.setPage(pageable.getPageNumber());
		params.setSize(pageable.getPageSize());
		params.setRadius(radius);
		List<Timeline> entitys = storedProcedureService.getMyTimelineStoredProcedureQuery("geo_dist_my_timeline_v2",
				params);
		return entitys;
	}

	/**
	 * Get All By Is Not Deleted And Radius.
	 * 
	 * @param latitude
	 * @param longitude
	 * @param radius
	 * @param pageable
	 * @return List of Timeline
	 */
	public List<Timeline> getAllByIsNotDeletedAndRadiusMyTL_V3(String userId, int type, Double latitude,
			Double longitude, Pageable pageable) {
		StoreProcedureParams params = new StoreProcedureParams();
		params.setUserId(userId);
		params.setTimelineTypes(String.valueOf(type));
		params.setLatitude(latitude);
		params.setLongitude(longitude);
		params.setPage(pageable.getPageNumber());
		params.setSize(pageable.getPageSize());
		List<Timeline> entitys = storedProcedureService.getMyTimelineStoredProcedureQuery_V3("geo_dist_my_timeline_v3",
				params);
		return entitys;
	}

	/**
	 * Add Timeline Info.
	 * 
	 * @param userId
	 * @param timelineEntitys
	 * @return List of TimelineDTO
	 */
	public List<TimelineDTO> addTimelineInfo(String userId, List<Timeline> timelineEntitys) {
		if (timelineEntitys != null) {
			List<TimelineDTO> timelineDTOs = timelineEntitys.stream().map(timeline -> {
				try {
					TimelineDTO tlDTO = TimelineDTO.toDTO(userId, timeline);
					if (timeline.getPoiId() != null) {
						PointOfInterest poi = pointOfInterestRepository.findOne(timeline.getPoiId());
						if (poi != null) {
							PointOfInterestDTO poiDTO = PointOfInterestDTO.toBasicDTO(poi);
							poiDTO.setDescription(poi.getDescription());
							tlDTO.setPoi(poiDTO);
						}
					}
					return tlDTO;
				} catch (Exception e) {

				}
				return null;
			}).collect(Collectors.toList());
			// get user timeline.
			Set<String> tlUserIds = TimelineDTO.getUserIds();
			List<Object[]> userTLs = null;
			if (!tlUserIds.isEmpty()) {
				userTLs = timelineRepository.getUserInUserIds(tlUserIds.toArray(new String[tlUserIds.size()]));
			}
			List<UserDTO> userTLDTOs = (userTLs != null)
					? userTLs.stream().map(UserDTO::toDTO).collect(Collectors.toList())
					: new ArrayList<>();

			// get user comment.
			Set<String> cmUserIds = TimelineCommentDTO.getUserIds();
			List<Object[]> userCMs = new ArrayList<>();
			if (!cmUserIds.isEmpty()) {
				userCMs = timelineRepository.getUserInUserIds(cmUserIds.toArray(new String[cmUserIds.size()]));
			}
			List<UserDTO> userCMDTOs = userCMs.stream().map(UserDTO::toDTO).collect(Collectors.toList());
			timelineDTOs.forEach(item -> {
				try {
					// set user of timeline.
					if (item.getUser() == null) {
						for (UserDTO userTL : userTLDTOs) {
							if (userTL.getUserId().equals(item.getUserId())) {
								item.setUser(userTL);
							}
						}
					}
					// set user of timeline comment.
					item.getTimelineComments().forEach(itemComment -> {
						for (UserDTO userC : userCMDTOs) {
							if (userC.getUserId().equals(itemComment.getUserId())) {
								itemComment.setUser(userC);
							}
						}
					});
				} catch (Exception e) {

				}

			});
			return timelineDTOs;
		}
		return new ArrayList<TimelineDTO>();
	}

	public void updateTimelineType() {
		int index = 1;
		List<Timeline> times = timelineRepository.findByTimelineType(index);
	}

	/**
	 * Get all.
	 * 
	 * @param pageable
	 * @return
	 */
	public PageDTO<TimelineDTO> getAll(Pageable pageable) {
		Page<Timeline> page = timelineRepository.findByOrderByCreatedAtDesc(pageable);
		PageDTO<TimelineDTO> pages = new PageDTO<>();
		List<TimelineDTO> timelineDTOs = page.getContent().stream().map(TimelineDTO::toDTO)
				.collect(Collectors.toList());
		// get user timeline.
		Set<String> tlUserIds = TimelineDTO.getUserIds();
		List<Object[]> userTLs = timelineRepository.getUserInUserIds(tlUserIds.toArray(new String[tlUserIds.size()]));
		List<UserDTO> userTLDTOs = userTLs.stream().map(UserDTO::toDTO).collect(Collectors.toList());
		timelineDTOs.forEach(item -> {
			// set user of timeline.
			for (UserDTO userTL : userTLDTOs) {
				if (item.getUser() == null) {
					if (userTL.getUserId().equals(item.getUserId())) {
						item.setUser(userTL);
					}
				}
			}
		});
		pages.setPageDTO(page);
		pages.setContent(timelineDTOs);
		return pages;
	}

	/**
	 * Share to facebook.
	 *
	 * @param timelineId
	 * @return String of HTML
	 */
	public String getShareTimeline(Long timelineId) {
		Timeline entity = timelineRepository.findOne(timelineId);
		String htmlContent = "";
		if (entity != null) {
			htmlContent = resourceLoaderService.getResource(Html_Share_Timeline_Content);
			if (htmlContent != null) {
				String imageUrl = "";
				String name = "";
				String desc = "";

				if (!entity.getImages().isEmpty()) {
					imageUrl = entity.getImages().get(0).getUrl();
				}
				List<Object[]> objList = timelineRepository.getUserByUserId(entity.getUserId());
				String userName = "";
				if (!objList.isEmpty()) {
					Object[] objs = objList.get(0);
					userName = String.valueOf(objs[1]);
				}
				name = ConstantUtils.TIMELINE_SHARE_TITLE + userName;

				if (entity.getDescription() != null) {
					desc = entity.getDescription();
				}

				htmlContent = htmlContent.replace("__TIMELINE_ID__", "" + timelineId);
				htmlContent = htmlContent.replace("__PAGE_TITLE__", name);
				htmlContent = htmlContent.replace("__PAGE_DESC__", desc);
				htmlContent = htmlContent.replace("__PAGE_IMAGE__", imageUrl);

				htmlContent = htmlContent.replace("__IOS_URL__", "omart://timeline/" + timelineId);
				htmlContent = htmlContent.replace("__ANDROID_URL__", "omart://timeline/" + timelineId);
			}
		}
		return htmlContent;
	}

	/**
	 * Report abuse.
	 * 
	 * @param id
	 */
	public void reportAbuse(Long id) {
		Timeline entity = timelineRepository.findOne(id);
		if (entity != null) {
			entity.setReportAbuse(true);
			timelineRepository.save(entity);
		}
	}

}
