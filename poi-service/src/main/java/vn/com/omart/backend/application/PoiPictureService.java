package vn.com.omart.backend.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.com.omart.backend.application.response.PictureCommentDTO;
import vn.com.omart.backend.application.response.PoiActionDTO;
import vn.com.omart.backend.application.response.PoiDTO;
import vn.com.omart.backend.application.response.PoiPictureDTO;
import vn.com.omart.backend.application.response.PoiTimelineDTO;
import vn.com.omart.backend.application.response.UserDTO;
import vn.com.omart.backend.application.util.DateUtils;
import vn.com.omart.backend.application.util.PoiActionStatus;
import vn.com.omart.backend.constants.ConstantUtils;
import vn.com.omart.backend.domain.model.Image;
import vn.com.omart.backend.domain.model.PoiPicture;
import vn.com.omart.backend.domain.model.PoiPictureAction;
import vn.com.omart.backend.domain.model.PoiPictureActionRepository;
import vn.com.omart.backend.domain.model.PoiPictureCP;
import vn.com.omart.backend.domain.model.PoiPictureCPRepository;
import vn.com.omart.backend.domain.model.PoiPictureRepository;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.domain.model.PointOfInterestRepository;
import vn.com.omart.backend.port.adapter.support.mysql.JpaImageConverter;
import vn.com.omart.backend.port.adapter.userprofile.UserService;
import vn.com.omart.sharedkernel.application.model.error.NotFoundException;
import vn.com.omart.sharedkernel.application.model.error.UnauthorizedAccessException;

@Service
public class PoiPictureService {

	@Value("${share.path.picture}")
	private String Html_Share_Picture_Content;

	@Autowired
	private ResourceLoaderService resourceLoaderService;

	@Autowired
	private PointOfInterestRepository pointOfInterestRepository;

	@Autowired
	private PoiPictureRepository poiPictureRepository;

	@Autowired
	private PoiPictureActionRepository poiPictureActionRepository;

	@Autowired
	private UserService userService;

	/**
	 * Save Poi Picture for create new.
	 *
	 * @param poiCmd
	 * @param userId
	 * @return List of PoiPictureDTO
	 */
	public List<PoiPictureDTO> savePoiPicture(PoiDTO poiCmd, String userId) {
		System.out.println("\n==========old version upload image===========");
		List<PoiPictureDTO> results = new ArrayList<PoiPictureDTO>();
		List<PoiPicture> pictures = poiCmd.getPoiPictures();
		PointOfInterest poi = pointOfInterestRepository.findOne(poiCmd.getPoiId());
		if (poi == null) {
			throw new NotFoundException("Poi not found");
		}
		if (pictures != null) {
			pictures.forEach(picture -> {
				picture.setPoi(poi);
				picture.setUserId(userId);
				if(StringUtils.isNotBlank(picture.getUrl())) {
					List<Image> images = new ArrayList<>();
					images.add(new Image(picture.getUrl()));
					picture.setUrls(images);
				}else {
					picture.setUrls(new ArrayList<>());
				}
			});
			List<PoiPicture> poiPictures = poiPictureRepository.save(pictures);
			results = poiPictures.stream().map(PoiPictureDTO::from).collect(Collectors.toList());
		}
		return results;
	}

	/**
	 * Update Poi Picture.
	 *
	 * @param picture
	 * @param userId
	 * @return PoiPicture
	 */
	public PoiPicture updatePoiPicture(PoiPicture picture, String userId, Long pictureId) {
		PoiPicture poiPicture = poiPictureRepository.findOne(pictureId);
		if (poiPicture == null) {
			throw new NotFoundException("Picture not found");
		}
		poiPicture.setUserId(userId);
		poiPicture.setTitle(picture.getTitle());
		poiPicture.setCreatedAt(DateUtils.getCurrentDate());
		return poiPictureRepository.save(poiPicture);
	}

	/**
	 * Delete poi picture.
	 *
	 * @param pictureId
	 */
	@Transactional
	public void deletePoiPicture(Long pictureId) {
		PoiPicture picture = poiPictureRepository.findOne(pictureId);
		List<PoiPictureAction> actions = poiPictureActionRepository.findByPicture(picture);
		if (picture == null) {
			throw new NotFoundException("picture not exists");
		}
		if (!actions.isEmpty()) {
			poiPictureActionRepository.delete(actions);
		}
		poiPictureRepository.delete(picture);
	}

	/**
	 * Save Action Like On Picture.
	 *
	 * @param userId
	 * @param actionType
	 * @param pictureId
	 * @return JSONObject
	 */
	@SuppressWarnings("unchecked")
	public JSONObject saveActionLikeOnPicture(String userId, PoiActionStatus actionType, Long pictureId) {
		JSONObject json = new JSONObject();
		PoiPicture picture = poiPictureRepository.findOne(pictureId);
		if (picture != null) {
			PoiPictureAction pictureAction = poiPictureActionRepository.findByUserIdAndPicture(userId, picture);
			if (pictureAction != null) {
				pictureAction.setCreatedAt(DateUtils.getCurrentDate());
				pictureAction.setActionType(actionType.getId());
			} else {
				pictureAction = new PoiPictureAction();
				pictureAction.setUserId(userId);
				pictureAction.setPicture(picture);
				pictureAction.setActionType(actionType.getId());
				pictureAction.setCreatedAt(DateUtils.getCurrentDate());
			}
			poiPictureActionRepository.save(pictureAction);
			// count like
			PoiActionDTO action = poiPictureActionRepository.countPictureActionLIKEByPicture(picture);
			int likeNumber = 0;
			if (action != null) {
				likeNumber = Math.toIntExact(action.getCount());
			}
			picture.setLikeNumber(likeNumber);
			poiPictureRepository.save(picture);
			json.put(ConstantUtils.LIKE_NUMBER, likeNumber);
			json.put(ConstantUtils.USER_ACTION, getUserAction(userId, picture));

		} else {
			throw new NotFoundException("picture not exists");
		}
		return json;
	}

	/**
	 * Get User Action.
	 *
	 * @param userId
	 * @param picture
	 * @return JSONObject
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getUserAction(String userId, PoiPicture picture) {
		JSONObject userAction = new JSONObject();
		if (!StringUtils.isBlank(userId)) {
			PoiActionDTO poiAction = poiPictureActionRepository.queryByUserIdAndPoi(userId, picture);
			if (poiAction != null) {
				PoiActionStatus status = poiAction.getStatus();
				switch (status) {
				case LIKE:
					userAction.put(ConstantUtils.DISLIKE, false);
					userAction.put(ConstantUtils.LIKE, true);
					break;
				case DISLIKE:
					userAction.put(ConstantUtils.DISLIKE, true);
					userAction.put(ConstantUtils.LIKE, false);
					break;
				default:
					break;
				}
			}
		}
		return userAction;
	}

	/**
	 * Share Picture to facebook.
	 *
	 * @param poiId
	 * @param pictureId
	 * @return HTML
	 */
	public String getSharePicture(Long poiId, Long pictureId) {
		PoiPicture picture = poiPictureRepository.findOne(pictureId);

		String htmlContent = "";
		if (null != picture) {
			PointOfInterest shop = pointOfInterestRepository.findOne(poiId);

			String title = "";
			String desc = "";
			String imageUrl = "";
			if(!picture.getUrls().isEmpty()) {
				imageUrl = picture.getUrls().get(0).getUrl();
			}
			

			if (shop != null) {
				title = shop.name();
			}
			if (picture.getTitle() != null) {
				desc = picture.getTitle();
			}

			htmlContent = resourceLoaderService.getResource(Html_Share_Picture_Content);

			htmlContent = htmlContent.replace("__POI_ID__", "" + poiId);
			htmlContent = htmlContent.replace("__PAGE_TITLE__", title);
			htmlContent = htmlContent.replace("__PAGE_DESC__", desc);
			htmlContent = htmlContent.replace("__PAGE_IMAGE__", imageUrl);

			htmlContent = htmlContent.replace("__IOS_URL__", "omart://shop/" + poiId);
			htmlContent = htmlContent.replace("__ANDROID_URL__", "omart://shop/" + poiId);
		}
		return htmlContent;
	}

	/**
	 * Get Poi Posts
	 * 
	 * @param poiId
	 * @param userId
	 * @param pageable
	 * @return List<PoiPictureDTO>
	 */
	public List<PoiTimelineDTO> getPoiPicture(Long poiId, String userId, Pageable pageable) {
		PointOfInterest poi = pointOfInterestRepository.findOne(poiId);
		if (null == poi || null == poi.ownerId()) {
			throw new NotFoundException("POI not found or has just been deleted");
		}

		List<PoiPicture> entities = poiPictureRepository.findPoiPictureByPoiAndIsDeletedOrderByCreatedAtDesc(poi, false,
				pageable);

		List<PoiTimelineDTO> dtos = entities.stream().map(entity -> PoiTimelineDTO.from(entity, userId))
				.collect(Collectors.toList());

		// Get all User IDs
		Set<String> cmUserIds = PictureCommentDTO.userIds;
		List<Object[]> userCMs = new ArrayList<>();
		if (!cmUserIds.isEmpty()) {
			userCMs = poiPictureRepository.getUserInUserIds(cmUserIds.toArray(new String[cmUserIds.size()]));
		}
		// Get all User DTO and set to CommentDTO
		Map<String, UserDTO> dicUsers = userCMs.stream().map(UserDTO::toDTO)
				.collect(Collectors.toMap(UserDTO::getUserId, Function.identity()));
		dtos.forEach(dto -> {
			dto.getComments().forEach(item -> {
				if (dicUsers.containsKey(item.getUserId())) {
					item.setUser(dicUsers.get(item.getUserId()));
				}
			});
		});

		return dtos;
	}

	/**
	 * Save Poi Post.
	 *
	 * @param dto
	 * @param userId
	 * @return PoiPictureDTO
	 */
	public PoiTimelineDTO savePoiPicture(PoiTimelineDTO dto, String userId) {
		if (null == userService.getOwner(userId)) {
			throw new UnauthorizedAccessException("User is Unauthorized");
		}

		PointOfInterest poi = pointOfInterestRepository.findOne(dto.getPoiId());
		if (null == poi || null == poi.ownerId()) {
			throw new NotFoundException("POI not found or has just been deleted");
		}

		if (!userId.equals(poi.ownerId())) {
			throw new UnauthorizedAccessException("User is Unauthorized");
		}

		// Update
		PoiPicture oldEntity = null;
		if (dto.getId() != null) {
			oldEntity = poiPictureRepository.getOne(dto.getId());
		}

		PoiPicture entity = null;
		if (oldEntity == null) {
			// New
			entity = PoiTimelineDTO.to(dto);
			entity.setUserId(userId);
			entity.setPoi(poi);
		} else {
			// Update
			entity = PoiTimelineDTO.to(dto, oldEntity);
		}

		entity = poiPictureRepository.save(entity);

		return PoiTimelineDTO.from(entity, userId);
	}

	/**
	 * Delete Poi Post.
	 *
	 * @param postId
	 * @param userId
	 */
	public void deletePoiPicture(Long postId, String userId) {
		if (null == userService.getOwner(userId)) {
			throw new UnauthorizedAccessException("User is Unauthorized");
		}

		// Get Old
		PoiPicture oldEntity = poiPictureRepository.getOne(postId);
		if (null == oldEntity) {
			throw new NotFoundException("POI POST not found or has just been deleted");
		}

		PointOfInterest poi = oldEntity.getPoi();
		if (null == poi || null == poi.ownerId()) {
			throw new NotFoundException("POI not found or has just been deleted");
		}

		if (!userId.equals(poi.ownerId())) {
			throw new UnauthorizedAccessException("User is Unauthorized");
		}

		// Set IsDelete is TRUE
		oldEntity.setDeleted(true);
		poiPictureRepository.save(oldEntity);
	}

	@Autowired
	private PoiPictureCPRepository poiPictureCPRepository;
//
//	public void convertImageUrl() {
//		// PageRequest p = new PageRequest(0, 10);
//		List<PoiPictureCP> pictures = poiPictureCPRepository.getAllByUrlNotLike();
//		List<PoiPictureCP> entities = new ArrayList<>();
//		for (PoiPictureCP pic : pictures) {
//			if (pic != null) {
//				if (StringUtils.isNotBlank(pic.getUrl())) {
//					List<Image> images = new ArrayList<>();
//					Image image = new Image();
//					image.setUrl(pic.getUrl());
//					images.add(image);
//					String url = jpaImageConverter.convertToDatabaseColumn(images);
//					pic.setUrl(url);
//				} else {
//					pic.setUrl(jpaImageConverter.convertToDatabaseColumn(new ArrayList<>()));
//				}
//				entities.add(pic);
//			}
//		}
//		if (!entities.isEmpty()) {
//			poiPictureCPRepository.save(entities);
//		}
//
//	}
	
	public void copyDataIntoCreatedAt() {
		List<PoiPictureCP> poiPictures = poiPictureCPRepository.findAllByCreatedAtIsNull();
		List<PoiPictureCP> entities = new ArrayList<>();
		for(PoiPictureCP e : poiPictures) {
			if(e.getPoiId()!= null) {
				PointOfInterest poi = pointOfInterestRepository.findOne(e.getPoiId());
				if(poi!=null) {
					if(poi.createdAt()!=null) {
						e.setCreatedAt(poi.createdAt());
						entities.add(e);
					}
				}
			}
		}
		if(!entities.isEmpty()) {
			poiPictureCPRepository.save(entities);
		}
	}

}
