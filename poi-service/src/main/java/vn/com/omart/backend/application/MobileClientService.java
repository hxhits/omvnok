package vn.com.omart.backend.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import vn.com.omart.backend.application.request.PointOfInterestCmd;
import vn.com.omart.backend.application.response.CategoryDTO;
import vn.com.omart.backend.application.response.CategoryParentDTO;
import vn.com.omart.backend.application.response.DistrictDTO;
import vn.com.omart.backend.application.response.ImageDTO;
import vn.com.omart.backend.application.response.ItemDTO;
import vn.com.omart.backend.application.response.NewsDTO;
import vn.com.omart.backend.application.response.PoiActionDTO;
import vn.com.omart.backend.application.response.PoiCommentDTO;
import vn.com.omart.backend.application.response.PoiDTO;
import vn.com.omart.backend.application.response.PoiPictureDTO;
import vn.com.omart.backend.application.response.PoiStatsDTO;
import vn.com.omart.backend.application.response.PointOfInterestDTO;
import vn.com.omart.backend.application.response.ProvinceDTO;
import vn.com.omart.backend.application.response.StoreProcedureParams;
import vn.com.omart.backend.application.response.WardDTO;
import vn.com.omart.backend.application.util.CommonUtils;
import vn.com.omart.backend.application.util.DateUtils;
import vn.com.omart.backend.application.util.PoiActionStatus;
import vn.com.omart.backend.application.util.PoiState;
import vn.com.omart.backend.constants.ConstantUtils;
import vn.com.omart.backend.constants.OmartType.PoiType;
import vn.com.omart.backend.domain.model.Category;
import vn.com.omart.backend.domain.model.CategoryRepository;
import vn.com.omart.backend.domain.model.District;
import vn.com.omart.backend.domain.model.DistrictRepository;
import vn.com.omart.backend.domain.model.Image;
import vn.com.omart.backend.domain.model.Item;
import vn.com.omart.backend.domain.model.ItemRepository;
import vn.com.omart.backend.domain.model.NewsRepository;
import vn.com.omart.backend.domain.model.OwnerRepository;
import vn.com.omart.backend.domain.model.PoiAction;
import vn.com.omart.backend.domain.model.PoiActionRepository;
import vn.com.omart.backend.domain.model.PoiComment;
import vn.com.omart.backend.domain.model.PoiCommentRepository;
import vn.com.omart.backend.domain.model.PoiPicture;
import vn.com.omart.backend.domain.model.PoiPictureAction;
import vn.com.omart.backend.domain.model.PoiPictureActionRepository;
import vn.com.omart.backend.domain.model.PoiPictureRepository;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.domain.model.PointOfInterestRepository;
import vn.com.omart.backend.domain.model.Province;
import vn.com.omart.backend.domain.model.ProvinceRepository;
import vn.com.omart.backend.domain.model.RecruitmentRepository;
import vn.com.omart.backend.domain.model.SupervisorTargetRepository;
import vn.com.omart.backend.domain.model.UserProfileRepository;
import vn.com.omart.backend.domain.model.Ward;
import vn.com.omart.backend.domain.model.WardRepository;
import vn.com.omart.backend.port.adapter.userprofile.UserService;
import vn.com.omart.sharedkernel.GeoCalculator;
import vn.com.omart.sharedkernel.application.model.error.InvalidInputException;
import vn.com.omart.sharedkernel.application.model.error.NotFoundException;
import vn.com.omart.sharedkernel.application.model.error.UnauthorizedAccessException;

@Service
@Slf4j
public class MobileClientService {

	private final Logger logger = LoggerFactory.getLogger(MobileClientService.class);

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Value("${share.path.poi}")
	private String Html_Share_Poi_Content;

	@Autowired
	private CategoryFollowService categoryFollowService;

	@Autowired
	private DistrictFollowService districtFollowService;

	@Autowired
	private ResourceLoaderService resourceLoaderService;

	@Autowired
	private PoiActionRepository poiActionRepository;

	@Autowired
	private MobileAdminService mobileAdminService;

	@Autowired
	private PoiCommentRepository poiCommentRepository;

	@Autowired
	private PoiPictureRepository poiPictureRepository;

	@Autowired
	private PoiPictureActionRepository poiPictureActionRepository;

	@Autowired
	private EntityManager em;

	@Autowired
	private NewsRepository newsRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private ProvinceRepository provinceRepository;

	@Autowired
	private DistrictRepository districtRepository;

	@Autowired
	private WardRepository wardRepository;

	private final CategoryRepository categoryRepository;
	private final PointOfInterestRepository pointOfInterestRepository;
	private final ItemRepository itemRepository;
	private final OwnerRepository ownerRepository;
	private final SupervisorTargetRepository supervisorTargetRepository;

	@Autowired
	public MobileClientService(CategoryRepository categoryRepository,
			PointOfInterestRepository pointOfInterestRepository, ItemRepository itemRepository,
			OwnerRepository ownerRepository, SupervisorTargetRepository supervisorTargetRepository) {
		this.categoryRepository = categoryRepository;
		this.pointOfInterestRepository = pointOfInterestRepository;
		this.itemRepository = itemRepository;
		this.ownerRepository = ownerRepository;
		this.supervisorTargetRepository = supervisorTargetRepository;
	}

	// @Cacheable(value = CacheName.CATEGORIES, key = "#root.methodName")
	public List<CategoryDTO> getCategories() {
		return this.categoryRepository.findAllByParentIsNullAndIsDisableIsNull().stream().map(category -> {
			List<CategoryDTO> children = category.children().stream().filter(item -> item.getIsDisable() == null)
					.map(CategoryDTO::from).sorted(Comparator.comparingLong(CategoryDTO::getOrder))
					.collect(Collectors.toList());
			return CategoryDTO.from(category, children);
		}).sorted(Comparator.comparing(CategoryDTO::getOrder)).collect(Collectors.toList());
	}

	public List<PointOfInterestDTO> getListPOI(long catId, long limit, long lastId, String province, String district,
			String ward) {
		return this.pointOfInterestRepository.findAllByCategoryId(catId, limit, lastId, province, district, ward)
				.stream().map(PointOfInterestDTO::from).collect(Collectors.toList());
	}

	public List<PointOfInterestDTO> getListPOIByRecruit(Long[] ids) {
		PageRequest pageRequest = new PageRequest(0, Integer.MAX_VALUE);
		return this.pointOfInterestRepository.getByInIds(ids, pageRequest).stream().map(PointOfInterestDTO::from)
				.collect(Collectors.toList());
	}

	public List<PointOfInterestDTO> getNearestPOIs(long categoryId, double latpoint, double longpoint, double radius,
			double distance, String province, String district, String ward) {

		return this.pointOfInterestRepository
				.findNearest(categoryId, latpoint, longpoint, radius, distance, province, district, ward).stream()
				.map(PointOfInterestDTO::from).collect(Collectors.toList());
	}

	public List<PointOfInterestDTO> findNearestByPoiId(String ids, double latpoint, double longpoint, double radius,
			double distance, String province, String district, String ward) {

		return this.pointOfInterestRepository
				.findNearestByPoiId(ids, latpoint, longpoint, radius, distance, province, district, ward).stream()
				.map(PointOfInterestDTO::from).collect(Collectors.toList());
	}

	// @Cacheable(value = CacheName.POI, key = "#catId + ':' + #poiId")
	public PointOfInterestDTO getPointOfInterest(long poiId, long catId) {
		PointOfInterest byIdAndCategory = this.pointOfInterestRepository.findByIdAndCategoryIdAndIsDeleted(poiId, catId,
				false);

		if (null == byIdAndCategory) {
			throw new NotFoundException("PointOfInterest not exists");
		}

		return PointOfInterestDTO.from(byIdAndCategory);
	}

	public List<ItemDTO> getPointOfInterestItems(long poiId, long catId) {

		PointOfInterest poi = this.pointOfInterestRepository.findByIdAndCategoryIdAndIsDeleted(poiId, catId, false);
		if (null == poi) {
			throw new NotFoundException("PointOfInterest not exists");
		}

		return this.itemRepository.findByPoiId(poi.id()).stream().map(ItemDTO::from).collect(Collectors.toList());
	}

	public ItemDTO getPointOfInterestItem(long itemId, long poiId, long catId) {

		PointOfInterest poi = this.pointOfInterestRepository.findByIdAndCategoryIdAndIsDeleted(poiId, catId, false);
		if (null == poi) {
			throw new NotFoundException("PointOfInterest not exists");
		}

		Item item = this.itemRepository.findByIdAndPoiId(itemId, poi.id());

		if (null == item) {
			throw new NotFoundException("Item not exists");
		}

		// if (StringUtils.isBlank(item.description())) {
		// throw new RuntimeException("Can not retrieve this item because have no
		// description to show.");
		// }
		if (StringUtils.isEmpty(item.description())) {
			item.setDescription("");
		}

		return ItemDTO.from(item);
	}

	public List<PointOfInterestDTO> getPOIByOwner(String ownerId) {

		// Owner owner = this.ownerRepository.findByUserId(ownerId);

		return this.pointOfInterestRepository.findAllByOwnerId(ownerId).stream().map(PointOfInterestDTO::from)
				.collect(Collectors.toList());
	}

	/**
	 * Save POI action Type 1 = LIKE,2 = DISLIKE,3 = FAVORITE.
	 *
	 * @param poiId
	 *            poiId
	 * @param userId
	 *            userId
	 * @param actionType
	 *            actionType
	 */
	public JSONObject savePoiAction(Long poiId, String userId, PoiActionStatus actionType) {
		JSONObject json = new JSONObject();
		PointOfInterest poi = pointOfInterestRepository.findOne(poiId);
		if (poi != null) {
			PoiAction poiAction = poiActionRepository.findByUserIdAndPoi(userId, poi);
			if (poiAction != null) {
				poiAction.setActionType(actionType.getId());
				poiAction.setCreatedAt(DateUtils.getCurrentDate());
			} else {
				poiAction = new PoiAction();
				poiAction.setUserId(userId);
				poiAction.setPoi(poi);
				poiAction.setActionType(actionType.getId());
				poiAction.setCreatedAt(DateUtils.getCurrentDate());
			}
			poiActionRepository.save(poiAction);
			// count like number
			PoiActionDTO poiActionItem = poiActionRepository.countPoiActionLIKEByPoi(poi);
			int likeNumber = 0;
			if (poiActionItem != null) {
				likeNumber = Math.toIntExact(poiActionItem.getCount());
			}
			json.put(ConstantUtils.LIKE_NUMBER, likeNumber);
			json.put(ConstantUtils.USER_ACTION, getUserAction(userId, poi));
		} else {
			throw new NotFoundException("POI not exists");
		}
		return json;
	}

	/**
	 * Increase one unit when have a user visit shop.
	 *
	 * @param poiId
	 *            poiId
	 * @return JSONObject.
	 */
	public JSONObject viewCount(Long poiId) {
		JSONObject json = new JSONObject();
		PointOfInterest pointOfInterest = pointOfInterestRepository.findOne(poiId);
		if (pointOfInterest != null) {
			pointOfInterest.setViewCount((pointOfInterest.getViewCount() + 1));
			pointOfInterest = pointOfInterestRepository.save(pointOfInterest);
			json.put(ConstantUtils.COUNT, pointOfInterest.getViewCount());
		} else {
			throw new NotFoundException("POI not exists");
		}
		return json;
	}

	/**
	 * Increase one unit when have a user share shop.
	 *
	 * @param poiId
	 *            poiId
	 * @return JSONObject.
	 */
	public JSONObject shareCount(Long poiId) {
		JSONObject json = new JSONObject();
		PointOfInterest pointOfInterest = pointOfInterestRepository.findOne(poiId);
		if (pointOfInterest != null) {
			pointOfInterest.setShareCount((pointOfInterest.getShareCount() + 1));
			pointOfInterest = pointOfInterestRepository.save(pointOfInterest);
			json.put(ConstantUtils.COUNT, pointOfInterest.getShareCount());
		} else {
			throw new NotFoundException("POI not exists");
		}
		return json;
	}

	/**
	 * Find Poi By Id.
	 *
	 * @param poiId
	 *            poiId
	 * @return PointOfInterest
	 */
	public PointOfInterest getPoiById(long poiId) {
		PointOfInterest pointOfInterest = pointOfInterestRepository.findByIdAndIsDeletedAndIsApproved(poiId, false,
				true);
		if (null == pointOfInterest) {
			throw new NotFoundException("PointOfInterest not exists");
		}
		return pointOfInterest;
	}

	/**
	 * Custom View Poi Detail.
	 *
	 * @param poiId
	 * @param userId
	 * @param latitude
	 * @param longitude
	 * @return PointOfInterestDTO
	 */
	public PointOfInterestDTO getPoiDetail(Long poiId, String userId, Double latitude, Double longitude,
			String language) {
		// get poi
		PointOfInterest poi = getPoiById(poiId);
		PointOfInterestDTO pointOfInterestDTO = PointOfInterestDTO.from(poi);
		// get category by language
		pointOfInterestDTO.setCategory(CategoryDTO.from(poi.category(), language));
		// add poi picture
		List<PoiPicture> pictures = poiPictureRepository.findPoiPictureByPoi(poi);
		// get picture
		List<PoiPictureDTO> poiPictureDTO = pictures.stream().map(PoiPictureDTO::from).collect(Collectors.toList());
		// pointOfInterestDTO.setPoiPictures(pictures.stream().map(PoiPictureDTO::from).collect(Collectors.toList()));
		// get user picture action
		List<PoiPictureAction> userPictureAction = null;
		if (!pictures.isEmpty()) {
			userPictureAction = poiPictureActionRepository.getPictureInList(userId, pictures);
			// pointOfInterestDTO.setPictureActions(userPictureAction.stream().map(PoiPictureActionDTO::from).collect(Collectors.toList()));
			// add picture
			pointOfInterestDTO.setPoiPictures(getPoiPicture(poiPictureDTO, userPictureAction));
		}
		// get item
		List<ItemDTO> items = mobileAdminService.getItemsByPOI(poiId);
		// add items
		pointOfInterestDTO.setItems(items);
		// add stats
		pointOfInterestDTO.setStats(getStats(poi, latitude, longitude, pictures.size()));
		// add user if user is logged in.
		pointOfInterestDTO.setUserAction(getUserAction(userId, poi));
		// add comments
		PageRequest pageRequest = new PageRequest(0, 5);
		List<PoiCommentDTO> comments = this.getCommentByPoiId(poiId, pageRequest).getContent();
		pointOfInterestDTO.setPoiComments(comments);
		return pointOfInterestDTO;
	}

	/**
	 * Update Poi
	 * 
	 * @param dto
	 * @param userId
	 * @param language
	 * @return PointOfInterestDTO
	 */
	public PointOfInterestDTO updatePoiBuz(PointOfInterestDTO dto, String userId, String language) {
		if (null == dto) {
			throw new NotFoundException("POI not exists");
		}

		if (null == userService.getOwner(userId)) {
			throw new UnauthorizedAccessException("User is Unauthorized");
		}

		PointOfInterest entity = pointOfInterestRepository.findOne(dto.getId());
		if (null == entity || null == entity.ownerId()) {
			throw new NotFoundException("POI not found or has just been deleted");
		}

		if (!userId.equals(entity.ownerId())) {
			throw new UnauthorizedAccessException("User is Unauthorized");
		}

		if (null == PoiType.getById(dto.getPoiType())) {
			throw new InvalidInputException("Invalid ShopType");
		}

		entity = PointOfInterestDTO.update(entity, dto);
		pointOfInterestRepository.save(entity);

		return dto;
	}

	/**
	 * Get Poi Picture.
	 *
	 * @param pictures
	 * @param actions
	 * @return List of PoiPictureDTO
	 */
	public List<PoiPictureDTO> getPoiPicture(List<PoiPictureDTO> pictures, List<PoiPictureAction> actions) {
		for (PoiPictureAction action : actions) {
			for (int i = 0; i < pictures.size(); i++) {
				if (action.getPicture().getId() == pictures.get(i).getId()) {
					pictures.get(i).setLike(action.getActionType() == PoiActionStatus.LIKE.getId() ? true : false);
					break;
				}
			}
		}
		return pictures;
	}

	/**
	 * Get User Action.
	 *
	 * @param userId
	 * @param poi
	 * @return JSONObject
	 */
	public JSONObject getUserAction(String userId, PointOfInterest poi) {
		JSONObject userAction = new JSONObject();
		if (!StringUtils.isBlank(userId)) {
			PoiActionDTO poiAction = poiActionRepository.queryByUserIdAndPoi(userId, poi);
			if (poiAction != null) {
				PoiActionStatus status = poiAction.getStatus();
				switch (status) {
				case LIKE:
					userAction.put(ConstantUtils.DISLIKE, false);
					userAction.put(ConstantUtils.LIKE, true);
					userAction.put(ConstantUtils.FAVORITE, false);
					break;
				case DISLIKE:
					userAction.put(ConstantUtils.DISLIKE, true);
					userAction.put(ConstantUtils.LIKE, false);
					userAction.put(ConstantUtils.FAVORITE, false);
					break;
				case FAVORITE:
					userAction.put(ConstantUtils.DISLIKE, false);
					userAction.put(ConstantUtils.LIKE, false);
					userAction.put(ConstantUtils.FAVORITE, true);
					break;
				default:
					break;
				}
			}
		}
		return userAction;
	}

	/**
	 * Get Stats.
	 *
	 * @param poi
	 * @param latitude
	 * @param longitude
	 * @param photoNumber
	 * @return PoiStatsDTO
	 */
	public PoiStatsDTO getStats(PointOfInterest poi, Double latitude, Double longitude, int photoNumber) {
		List<PoiActionDTO> countResutls = poiActionRepository.countPoiActionByActionType(poi);
		PoiStatsDTO stats = new PoiStatsDTO();
		for (PoiActionDTO entity : countResutls) {
			switch (entity.getStatus()) {
			case LIKE:
				stats.setLikes(java.lang.Math.toIntExact(entity.getCount()));
				break;
			case DISLIKE:
				stats.setDislikes(java.lang.Math.toIntExact(entity.getCount()));
				break;
			default:
				break;
			}
		}
		stats.setPhotos(photoNumber);
		// distance is m
		Double distance = -1d;
		if (latitude != null && longitude != null) {
			distance = new Double(
					GeoCalculator.distance(latitude, longitude, poi.latitude(), poi.longitude(), "K") * 1000);
		}
		stats.setViews(poi.getViewCount());
		stats.setDistance(distance);
		stats.setRate(7.5); // TODO
		stats.setComments(poiCommentRepository.countByPoi(poi));
		stats.setShares(poi.getShareCount());
		return stats;
	}

	/**
	 * Save comment from user post.
	 *
	 * @param poiId
	 * @param comment
	 * @param userId
	 * @return JSONObject
	 */
	public PoiComment saveComment(Long poiId, PoiComment comment, String userId) {
		PointOfInterest poi = pointOfInterestRepository.findOne(poiId);
		PoiComment poiComment = null;
		if (poi == null) {
			throw new NotFoundException("PointOfInterest not exists");
		}
		comment.setCreatedAt(DateUtils.getCurrentDate());
		comment.setUserId(userId);
		comment.setPoi(poi);
		try {
			poiComment = poiCommentRepository.save(comment);
		} catch (Exception e) {
			// handle if emoij not support.
			String commentStr = CommonUtils.emoijNormalize(comment.getComment());
			comment.setComment(commentStr);
			poiComment = poiCommentRepository.save(comment);
		}
		return poiComment;
	}

	/**
	 * Get all Comments by PoiId.
	 *
	 * @param poiId
	 * @param pageable
	 * @return Page of PoiCommentDTO
	 */
	public Page<PoiCommentDTO> getCommentByPoiId(Long poiId, Pageable pageable) {
		Page<Object[]> objectPageComments = poiCommentRepository.findCommentByPoiId(poiId, pageable);
		Page<PoiCommentDTO> pageComments = objectPageComments.map(new PoiCommentDTO.QueryMapper()::converter);
		return pageComments;
	}

	/**
	 * Share to facebook.
	 *
	 * @param poiId
	 * @return String of HTML
	 */
	public String getShareContent(Long poiId) {
		PointOfInterest entity = pointOfInterestRepository.findOne(poiId);
		String htmlContent = "";
		if (entity != null) {
			htmlContent = resourceLoaderService.getResource(Html_Share_Poi_Content);
			if (htmlContent != null) {
				String imageUrl = "";
				String name = "";
				String desc = "";

				if (!entity.coverImage().isEmpty()) {
					imageUrl = entity.coverImage().get(0).getUrl();
				}
				if (entity.name() != null) {
					name = entity.name();
				}
				if (entity.description() != null) {
					desc = entity.description();
				}

				htmlContent = htmlContent.replace("__POI_ID__", "" + poiId);
				htmlContent = htmlContent.replace("__PAGE_TITLE__", name);
				htmlContent = htmlContent.replace("__PAGE_DESC__", desc);
				htmlContent = htmlContent.replace("__PAGE_IMAGE__", imageUrl);

				htmlContent = htmlContent.replace("__IOS_URL__", "omart://shop/" + poiId);
				htmlContent = htmlContent.replace("__ANDROID_URL__", "omart://shop/" + poiId);
			}
		}
		return htmlContent;
	}

	/**
	 * Update poi name.
	 *
	 * @param poiId
	 * @param poiDTO
	 * @param userId
	 */
	public void updatePoiByName(Long poiId, PoiDTO poiDTO, String userId) {
		PointOfInterest poi = pointOfInterestRepository.findOne(poiId);
		if (poi == null) {
			throw new NotFoundException("Poi not found");
		}
		// set data to update.
		poi.setName(poiDTO.getName());
		poi.setUpdatedBy(userId);
		poi.setUpdatedAt(DateUtils.getCurrentDate());
		// checking is the same address and name.
		pointOfInterestRepository.save(poi);
	}

	/**
	 * Update Poi By State.
	 * 
	 * @param poiId
	 * @param poiDTO
	 * @param userId
	 */
	public void updatePoiByState(Long poiId, PoiDTO poiDTO, String userId) {
		PointOfInterest poi = pointOfInterestRepository.findOne(poiId);
		if (poi == null) {
			throw new NotFoundException("Poi not found");
		}
		// set data to update.
		poi.setPoiState(poiDTO.getPoiState().getId());
		poi.setUpdatedBy(userId);
		poi.setUpdatedAt(DateUtils.getCurrentDate());
		pointOfInterestRepository.save(poi);
	}

	/**
	 * Update poi address.
	 *
	 * @param poiId
	 * @param poiDTO
	 * @param userId
	 */
	public void updatePoiByAddress(Long poiId, PoiDTO poiDTO, String userId) {
		PointOfInterest poi = pointOfInterestRepository.findOne(poiId);
		if (poi == null) {
			throw new NotFoundException("Poi not found");
		}
		poi.setAddress(poiDTO.getAddress());
		poi.setUpdatedBy(userId);
		poi.setUpdatedAt(DateUtils.getCurrentDate());
		pointOfInterestRepository.save(poi);
	}

	/**
	 * Update poi basic.Option params
	 * 
	 * @param poiId
	 * @param poiDTO
	 * @param userId
	 */
	public void updateBasicInfoPoi(Long poiId, PoiDTO poiDTO, String userId) {
		PointOfInterest poi = pointOfInterestRepository.findOne(poiId);
		if (poi == null) {
			throw new NotFoundException("Poi not found");
		}
		if (poiDTO.getDescription() != null) {
			poi.setDescription(poiDTO.getDescription());
		}
		if (poiDTO.getOpenHour() != null) {
			poi.setOpenHour(poiDTO.getOpenHour());
		}
		if (poiDTO.getCloseHour() != null) {
			poi.setCloseHour(poiDTO.getCloseHour());
		}
		if (poiDTO.getPhone() != null) {
			poi.setPhone(poiDTO.getPhone());
		}
		if (poiDTO.getWebAddress() != null) {
			poi.setWebAddress(poiDTO.getWebAddress());
		}
		if (poiDTO.getEmail() != null) {
			poi.setEmail(poiDTO.getEmail());
		}
		poi.setUpdatedBy(userId);
		poi.setUpdatedAt(DateUtils.getCurrentDate());
		// if display state do not have value it's be default 0.
		poi.setDisplayState(poiDTO.getDisplayState());
		pointOfInterestRepository.save(poi);
	}

	/**
	 * Validation: a user cannot create more shop with the same name,address.
	 *
	 * @param username
	 * @param payload
	 * @return boolean
	 */
	public boolean isSameAddressAndName(String username, PointOfInterestCmd.CreateOrUpdate payload) {
		List<PointOfInterest> pois = pointOfInterestRepository.findAllByOwnerIdAndIsDeleted(username, false);
		if (!pois.isEmpty()) {
			// validate address,war,district,province,name
			long wardId = payload.getWardId().longValue();
			long provinceId = payload.getProvinceId().longValue();
			long districtId = payload.getDistrictId().longValue();
			String address = payload.getAddress().replaceAll("\\s+", "");
			String name = payload.getName().trim();
			List<PointOfInterest> poisByAddress = pois.stream()
					.filter(item -> item.province().id() == provinceId && item.district().id() == districtId
							&& item.ward().id() == wardId
							&& item.address().replaceAll("\\s+", "").equalsIgnoreCase(address)
							&& item.name().equalsIgnoreCase(name))
					.collect(Collectors.toList());
			if (!poisByAddress.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Find Pois By Nearest.(BE v1)
	 * 
	 * @param latitude
	 * @param longitude
	 * @param radius
	 * @param pageable
	 * @return List of PointOfInterestDTO.
	 */
	public List<PointOfInterestDTO> findAllNearestPois(Double latitude, Double longitude, int radius,
			Pageable pageable) {
		List<PointOfInterestDTO> poisDTO = null;
		// no open location
		if (latitude == 0 && longitude == 0) {
			List<PointOfInterest> pois = pointOfInterestRepository.findAllByIsDeletedOrderByCreatedAtDesc(false,
					pageable);
			poisDTO = pois.stream().map(PointOfInterestDTO::from).collect(Collectors.toList());
			return poisDTO;
		}
		if (radius == 0) {
			radius = 10000000;
		}
		int size = pageable.getPageSize();
		int pageNumber = pageable.getPageNumber();
		// get pois from Procedure.
		StoredProcedureQuery query = em.createNamedStoredProcedureQuery("geo_poi_dist");
		query.setParameter("orig_latitude", latitude);
		query.setParameter("orig_longitude", longitude);
		query.setParameter("radius", CommonUtils.metersToMiles(radius));
		query.setParameter("paging", (pageNumber * size));
		query.setParameter("size", size);
		query.execute();
		List<Object[]> poiObj = query.getResultList();
		poisDTO = poiObj.stream().map(PointOfInterestDTO::toDTO).collect(Collectors.toList());
		return poisDTO;
	}

	/**
	 * Find Pois By Nearest.(BE v0)
	 * 
	 * @param userId
	 * @param latitude
	 * @param longitude
	 * @param radius
	 * @param pageable
	 * @return
	 */
	public List<PointOfInterestDTO> findAllNearestPois_Enhance(String userId, Double latitude, Double longitude,
			int radius, Pageable pageable) {
		List<PointOfInterestDTO> poisDTO = null;
		// no open location
		List<Long> poiIdList = new ArrayList<Long>();
		if (latitude == 0 && longitude == 0) {
			List<PointOfInterest> poiEntities = pointOfInterestRepository.findAllByOrderByCreatedAtDesc(pageable);
			poisDTO = PointOfInterestDTO.entityToDTO(poiEntities, poiIdList);
			poisDTO = this.setNews(poisDTO, poiIdList, userId);
			return poisDTO;
		}
		if (radius == 0) {
			radius = 10000000;
		}

		int size = pageable.getPageSize();
		int pageNumber = pageable.getPageNumber();

		// get pois from Procedure.
		StoredProcedureQuery query = em.createNamedStoredProcedureQuery("geo_poi_dist_enhance");
		query.setParameter("orig_latitude", latitude);
		query.setParameter("orig_longitude", longitude);
		query.setParameter("radius", CommonUtils.metersToMiles(radius));
		query.setParameter("paging", (pageNumber * size));
		query.setParameter("size", size);
		query.execute();
		List<Object[]> poiObj = query.getResultList();
		poisDTO = new ArrayList<>();
		poisDTO = PointOfInterestDTO.objectToDTO(poiObj, poiIdList);
		poisDTO = this.setNews(poisDTO, poiIdList, userId);
		return poisDTO;
	}

	@Autowired
	private RecruitmentRepository recruitmentRepository;

	@Autowired
	private StoredProcedureService storedProcedureService;

	/**
	 * Find Pois By Nearest.(BE v1.1). Be used at tab Gan day, Quan tam
	 * 
	 * @param userId
	 * @param nearest
	 * @param pageable
	 * @return List of PointOfInterestDTO
	 */
	public List<PointOfInterestDTO> findAllNearestPois_V11(String userId, StoreProcedureParams nearest) {
		List<PointOfInterestDTO> poisDTO = null;
		if (!nearest.getCategoryIds().isEmpty()) {
			// no open location
			List<Long> poiIdList = new ArrayList<Long>();
			if (nearest.getLatitude() == 0 && nearest.getLongitude() == 0) {
				List<PointOfInterest> poiEntities = null;
				PageRequest pageRequest = new PageRequest(nearest.getPage(), nearest.getSize());
				if (!nearest.getCategoryIds().equalsIgnoreCase(ConstantUtils.ALL)) {
					Long[] catIds = CommonUtils.toLongNumber(nearest.getCategoryIds());
					poiEntities = pointOfInterestRepository.getAllByOrderByCreatedAtDesc(false, catIds, pageRequest);
				} else {
					poiEntities = pointOfInterestRepository.getAllByOrderByCreatedAtDesc(true, null, pageRequest);
				}
				poisDTO = PointOfInterestDTO.entityToDTO(poiEntities, poiIdList);

				List<Long> recruitPoiIds = recruitmentRepository
						.getRecruitmentInPositionIdAndState(nearest.getCategoryIds(), 0);
				if (!recruitPoiIds.isEmpty()) {////
					List<Long> recruitPoiIds1 = convertToLong(recruitPoiIds);
					recruitPoiIds1.removeAll(poiIdList);
					if (!recruitPoiIds1.isEmpty()) {
						Long[] ids = new Long[recruitPoiIds1.size()];
						for (int i = 0; i < recruitPoiIds1.size(); i++) {
							String value = String.valueOf(recruitPoiIds1.get(i));
							ids[i] = Long.valueOf(value);
						}
						List<PointOfInterest> recruitPoiEntities = pointOfInterestRepository.getByInIds(ids,
								pageRequest);
						List<PointOfInterestDTO> recruitPoisDTO = PointOfInterestDTO.entityToDTO(recruitPoiEntities,
								poiIdList);
						// merger.
						List<PointOfInterestDTO> results = Stream.concat(poisDTO.stream(), recruitPoisDTO.stream())
								.collect(Collectors.toList());
						// sort.
						Collections.sort(results, new Comparator<PointOfInterestDTO>() {
							@Override
							public int compare(PointOfInterestDTO o1, PointOfInterestDTO o2) {
								// TODO Auto-generated method stub
								return o1.getCreatedAt().compareTo(o2.getCreatedAt());
							}
						});
						return results;
					}

				} ///
				return poisDTO;

			}

			// get pois from Procedure.
			List<Object[]> poiObj = storedProcedureService
					.getPoiNotificationStoredProcedureQuery("geo_poi_dist_enhance_v1", nearest);
			List<PointOfInterestDTO> poisDTOp = new ArrayList<>();
			poisDTOp = PointOfInterestDTO.objectToDTO(poiObj, poiIdList);

			// get pois from Procedure with recruit.
			List<Long> recruitPoiIds = recruitmentRepository
					.getRecruitmentInPositionIdAndState(nearest.getCategoryIds(), 0);

			if (!recruitPoiIds.isEmpty()) {
				List<Long> recruitPoiIds1 = convertToLong(recruitPoiIds);
				recruitPoiIds1.removeAll(poiIdList);
				if (!recruitPoiIds1.isEmpty()) {

					nearest.setCategoryIds(this.getPoiIdStrs(recruitPoiIds1));

					List<Object[]> poiObjr = storedProcedureService
							.getPoiStoredProcedureQueryWithPoiId("geo_poi_dist_enhance_v2", nearest);
					List<PointOfInterestDTO> poisDTOr = new ArrayList<>();
					poisDTOr = PointOfInterestDTO.objectToDTO(poiObjr, poiIdList);

					// merger.
					List<PointOfInterestDTO> results = Stream.concat(poisDTOp.stream(), poisDTOr.stream())
							.collect(Collectors.toList());
					// sort.
					Collections.sort(results, new Comparator<PointOfInterestDTO>() {
						@Override
						public int compare(PointOfInterestDTO o1, PointOfInterestDTO o2) {
							// TODO Auto-generated method stub
							return o1.getDistance().compareTo(o2.getDistance());
						}
					});
					return results;
				}
			}

			return poisDTOp;
		}
		return new ArrayList<>();
	}

	/**
	 * Find Pois By Nearest.(BE v1.2). Be used at tab Quan tam
	 * 
	 * @param userId
	 * @param nearest
	 * @param pageable
	 * @return List of PointOfInterestDTO
	 */
	public List<PointOfInterestDTO> findAllNearestPois_V12(String userId, StoreProcedureParams nearest) {
		List<PointOfInterestDTO> poisDTO = null;
		if (!nearest.getCategoryIds().isEmpty()) {
			List<Long> poiIdList = new ArrayList<Long>();
			String provinceDistrictSelectedStr = nearest.getProvinceDistrictSelectedStr(), districtIds = "ALL";
			int provinceId = 0;
			provinceDistrictSelectedStr = provinceDistrictSelectedStr.replace(" ", "");
			if (!provinceDistrictSelectedStr.isEmpty()) {
				int index = provinceDistrictSelectedStr.indexOf(",");
				if (index != -1) {
					provinceId = Integer.parseInt(provinceDistrictSelectedStr.substring(0, index));
					districtIds = provinceDistrictSelectedStr.substring(index + 1,
							provinceDistrictSelectedStr.length());
				} else {
					provinceId = Integer.parseInt(provinceDistrictSelectedStr);
				}
			}
			// no open location

			// get pois from Procedure.
			List<Object[]> poiObj = storedProcedureService
					.getYourFollowPoiStoredProcedureQuery("geo_poi_dist_enhance_v4", provinceId, districtIds, nearest);
			List<PointOfInterestDTO> poisDTOp = new ArrayList<>();
			poisDTOp = PointOfInterestDTO.objectToDTO(poiObj, poiIdList);
			return poisDTOp;
			// }
		}
		return new ArrayList<>();
	}

	/**
	 * 
	 * @param recruitPoiIds
	 * @return
	 */
	public static List<Long> convertToLong(List<Long> recruitPoiIds) {
		List<Long> results = new ArrayList<>();
		for (int i = 0; i < recruitPoiIds.size(); i++) {
			Long e = Long.valueOf(String.valueOf(recruitPoiIds.get(i)));
			results.add(e);
		}
		return results;
	}

	/**
	 * 
	 * @param recruitPoiIds
	 * @return
	 */
	private String getPoiIdStrs(List<Long> recruitPoiIds) {
		String poiIds = "";
		for (int i = 0; i < recruitPoiIds.size(); i++) {
			poiIds += "," + recruitPoiIds.get(i);
		}
		poiIds = poiIds.replaceFirst(",", "");
		return poiIds;
	}

	/*
	 * Backup function if the copy function is not used.
	 */
	/*
	 * public List<PointOfInterestDTO> findAllNearestPois_V11_backup(String userId,
	 * StoreProcedureParams nearest) { List<PointOfInterestDTO> poisDTO = null; if
	 * (!nearest.getCategoryIds().isEmpty()) { // no open location List<Long>
	 * poiIdList = new ArrayList<Long>(); if (nearest.getLatitude() == 0 &&
	 * nearest.getLongitude() == 0) { List<PointOfInterest> poiEntities = null;
	 * PageRequest pageRequest = new PageRequest(nearest.getPage(),
	 * nearest.getSize()); if
	 * (!nearest.getCategoryIds().equalsIgnoreCase(ConstantUtils.ALL)) { Long[]
	 * catIds = CommonUtils.toLongNumber(nearest.getCategoryIds()); poiEntities =
	 * pointOfInterestRepository.getAllByOrderByCreatedAtDesc(false, catIds,
	 * pageRequest); } else { poiEntities =
	 * pointOfInterestRepository.getAllByOrderByCreatedAtDesc(true, null,
	 * pageRequest); } poisDTO = PointOfInterestDTO.entityToDTO(poiEntities,
	 * poiIdList); return poisDTO; }
	 * 
	 * // int size = pageable.getPageSize(); // int pageNumber =
	 * pageable.getPageNumber(); // get pois from Procedure. StoredProcedureQuery
	 * query = em.createNamedStoredProcedureQuery("geo_poi_dist_enhance_v1");
	 * query.setParameter("orig_latitude", nearest.getLatitude());
	 * query.setParameter("orig_longitude", nearest.getLongitude());
	 * query.setParameter("radius", CommonUtils.metersToMiles(nearest.getRadius()));
	 * query.setParameter("paging", nearest.getPage() * nearest.getSize());
	 * query.setParameter("size", nearest.getSize());
	 * query.setParameter("categoryIds", nearest.getCategoryIds()); query.execute();
	 * List<Object[]> poiObj = query.getResultList(); poisDTO = new ArrayList<>();
	 * poisDTO = PointOfInterestDTO.objectToDTO(poiObj, poiIdList); return poisDTO;
	 * } return new ArrayList<>(); }
	 */

	/*
	 * public List<PointOfInterestDTO> findAllNotification(String userId, NearestDTO
	 * nearest) { List<PointOfInterestDTO> poisDTO = null; if
	 * (!nearest.getCategoryIds().isEmpty()) { // no open location List<Long>
	 * poiIdList = new ArrayList<Long>(); if (nearest.getLatitude() == 0 &&
	 * nearest.getLongitude() == 0) { List<PointOfInterest> poiEntities = null;
	 * PageRequest pageRequest = new PageRequest(nearest.getPage(),
	 * nearest.getSize()); if
	 * (!nearest.getCategoryIds().equalsIgnoreCase(ConstantUtils.ALL)) { Long[]
	 * catIds = CommonUtils.toLongNumber(nearest.getCategoryIds()); poiEntities =
	 * pointOfInterestRepository.getAllByOrderByCreatedAtDesc(false, catIds,
	 * pageRequest); } else { poiEntities =
	 * pointOfInterestRepository.getAllByOrderByCreatedAtDesc(true, null,
	 * pageRequest); } poisDTO = PointOfInterestDTO.entityToDTO(poiEntities,
	 * poiIdList); return poisDTO; } // has location. List<Object[]> objs = null;
	 * List<Object[]> objWithinNotis =
	 * this.getDataStoredProcedureQuery("dist_within_noti_v1", nearest); int sizeDto
	 * = nearest.getSize(); int pageDto = nearest.getPage(); double pageSum =
	 * Math.floor(objWithinNotis.size() / sizeDto);
	 * 
	 * if (objWithinNotis.size() < sizeDto) { int page = (int) Math.abs(pageSum -
	 * pageDto); nearest.setPage(page); List<Object[]> objWithoutNotis =
	 * this.getDataStoredProcedureQuery("dist_without_noti_v1", nearest); objs =
	 * Stream.concat(objWithinNotis.stream(),
	 * objWithoutNotis.stream()).collect(Collectors.toList()); } else { objs =
	 * objWithinNotis; }
	 * 
	 * poisDTO = new ArrayList<>(); poisDTO = PointOfInterestDTO.objectToDTO(objs,
	 * poiIdList); return poisDTO; } return new ArrayList<>(); }
	 */

	public List<Object[]> getDataStoredProcedureQuery(String namedStoredProcedureQuery, StoreProcedureParams params) {
		StoredProcedureQuery query = em.createNamedStoredProcedureQuery(namedStoredProcedureQuery);
		query.setParameter("orig_latitude", params.getLatitude());
		query.setParameter("orig_longitude", params.getLongitude());
		query.setParameter("radius", CommonUtils.metersToMiles(params.getRadius()));
		query.setParameter("paging", params.getPage() * params.getSize());
		query.setParameter("size", params.getSize());
		query.setParameter("categoryIds", params.getCategoryIds());
		query.execute();
		List<Object[]> poiObj = query.getResultList();
		return poiObj;
	}

	/**
	 * Get User Actions.
	 * 
	 * @param poiIdList
	 * @param userId
	 * @return List of PoiActionDTO.
	 */
	public List<PoiActionDTO> getUserActions(List<Long> poiIdList, String userId) {
		List<PoiActionDTO> actions = null;
		List<NewsDTO> news = null;
		if (!poiIdList.isEmpty()) {
			int n = poiIdList.size();
			Long[] ids = poiIdList.toArray(new Long[n]);
			actions = poiActionRepository.getPoiAction(ids, userId);
		}
		return actions;
	}

	/**
	 * Get News With UserActions.
	 * 
	 * @param poiIdList
	 * @param userId
	 * @return List of NewsDTO.
	 */
	public List<NewsDTO> getNewsWithUserActions(List<Long> poiIdList, String userId) {
		List<PoiActionDTO> actions = null;
		List<NewsDTO> news = null;
		if (!poiIdList.isEmpty()) {
			int n = poiIdList.size();
			Long[] ids = poiIdList.toArray(new Long[n]);
			actions = poiActionRepository.getPoiAction(ids, userId);
			news = newsRepository.getNews(ids);
			return this.getMergeNewsAndAction(actions, news);
		}
		return null;
	}

	/**
	 * Merge news and action DTO.
	 * 
	 * @param actions
	 * @param news
	 * @return List of NewsDTO.
	 */
	public List<NewsDTO> getMergeNewsAndAction(List<PoiActionDTO> actions, List<NewsDTO> news) {
		boolean flag = false;
		if (news == null)
			news = new ArrayList<>();
		if (actions != null) {
			/*
			 * if actions is not null. We merge it with News.
			 */
			for (PoiActionDTO x : actions) {
				flag = false;
				for (int i = 0; i < news.size(); i++) {
					if (x.getPoiId().equals(news.get(i).getPoiId())) {
						news.get(i).setActionLike(true);
						flag = true;
					}
				}
				if (!flag) {
					NewsDTO e = new NewsDTO();
					e.setPoiId(x.getPoiId());
					e.setActionLike(true);
					news.add(e);
				}
			}
		}
		return news;
	}

	/**
	 * Set News into PointOfInterestDTO model.
	 * 
	 * @param poisDTO
	 * @param poiIdList
	 * @param userId
	 * @return List of PointOfInterestDTO.
	 */
	public List<PointOfInterestDTO> setNews(List<PointOfInterestDTO> poisDTO, List<Long> poiIdList, String userId) {
		List<NewsDTO> news = getNewsWithUserActions(poiIdList, userId);
		if (news != null) {
			for (NewsDTO newsItem : news) {
				poisDTO.forEach(item -> {
					if (newsItem.getPoiId().equals(item.getId())) {
						// set user action
						if (newsItem.isActionLike()) {
							JSONObject userAction = new JSONObject();
							userAction.put(ConstantUtils.DISLIKE, false);
							userAction.put(ConstantUtils.LIKE, true);
							userAction.put(ConstantUtils.FAVORITE, false);
							item.setUserAction(userAction);
						}
						// set news
						if (!StringUtils.isBlank(newsItem.getDesc())) {
							item.setDescription(newsItem.getDesc());
						}

						if (!StringUtils.isBlank(newsItem.getThumbnailUrl())) {
							List<ImageDTO> avatars = new ArrayList<>();
							avatars.add(new ImageDTO(newsItem.getThumbnailUrl()));
							item.setAvatarImage(avatars);
						}

						if (!StringUtils.isBlank(newsItem.getBannerUrl())) {
							List<ImageDTO> coverImages = new ArrayList<>();
							coverImages.add(new ImageDTO(newsItem.getBannerUrl()));
							item.setCoverImage(coverImages);
						}
					}
				});
			}
		}
		return poisDTO;
	}

	/**
	 * Set User Actions.
	 * 
	 * @param poisDTO
	 * @param poiIdList
	 * @param userId
	 * @return List of PointOfInterestDTO
	 */
	public List<PointOfInterestDTO> setUserAction(List<PointOfInterestDTO> poisDTO, List<Long> poiIdList,
			String userId) {
		List<PoiActionDTO> actions = getUserActions(poiIdList, userId);
		if (actions != null) {
			for (PoiActionDTO action : actions) {
				poisDTO.forEach(item -> {
					if (action.getPoiId().equals(item.getId())) {
						JSONObject userAction = new JSONObject();
						userAction.put(ConstantUtils.DISLIKE, false);
						userAction.put(ConstantUtils.LIKE, true);
						userAction.put(ConstantUtils.FAVORITE, false);
						item.setUserAction(userAction);
					}
				});
			}
		}
		return poisDTO;
	}

	/**
	 * Get category by language.
	 * 
	 * @return List of CategoryDTO
	 */
	public List<CategoryDTO> getCategories(String language) {
		return this.categoryRepository.findAllByParentIsNull().stream().map(category -> {
			List<CategoryDTO> children = category.children().stream().filter(item -> item.getIsDisable() == null)
					.map(childrenItem -> CategoryDTO.from(childrenItem, language.trim()))
					.sorted(Comparator.comparingLong(CategoryDTO::getOrder)).collect(Collectors.toList());
			return CategoryDTO.from(category, children, language.trim());
		}).sorted(Comparator.comparing(CategoryDTO::getOrder)).collect(Collectors.toList());
	}

	/**
	 * Get category by language V1.
	 * 
	 * @return List of CategoryDTO
	 */
	public CategoryParentDTO getCategories_V1(String userId, String language) {

		List<CategoryDTO> categoryDTOs = this.categoryRepository.findAllByParentIsNull().stream().map(category -> {
			List<CategoryDTO> children = category.children().stream().filter(item -> item.getIsDisable() == null)
					.map(childrenItem -> CategoryDTO.from(childrenItem, language.trim()))
					.sorted(Comparator.comparingLong(CategoryDTO::getOrder)).collect(Collectors.toList());
			return CategoryDTO.from(category, children, language.trim());
		}).sorted(Comparator.comparing(CategoryDTO::getOrder)).collect(Collectors.toList());

		CategoryParentDTO categoryParentDTO = new CategoryParentDTO();
		String categoriesSelectedStr = categoryFollowService.getCategoryFollowByUserId(userId);
		String provinceDistrictSelectedStr = districtFollowService.getProvinceDistrictSelectedStr(userId);
		categoryParentDTO.setProvinceDistrictSelectedStr(provinceDistrictSelectedStr);
		categoryParentDTO.setCategoriesSelectedStr(categoriesSelectedStr);
		categoryParentDTO.setCategories(categoryDTOs);
		return categoryParentDTO;
	}

	/**
	 * Get category by language V2.
	 * 
	 * @return List of CategoryDTO
	 */
	public CategoryParentDTO getCategories_V2(String userId, String language) {

		List<CategoryDTO> categoryDTOs = this.categoryRepository.findAllByParentIsNullAndIsDisableIsNull().stream()
				.map(category -> {
					List<CategoryDTO> children = category.children().stream()
							.filter(item -> item.getIsDisable() == null)
							.map(childrenItem -> CategoryDTO.from(childrenItem, language.trim()))
							.sorted(Comparator.comparingLong(CategoryDTO::getOrder)).collect(Collectors.toList());
					return CategoryDTO.from(category, children, language.trim());
				}).sorted(Comparator.comparing(CategoryDTO::getOrder)).collect(Collectors.toList());

		CategoryParentDTO categoryParentDTO = new CategoryParentDTO();
		String categoriesSelectedStr = categoryFollowService.getCategoryFollowByUserId(userId);
		String provinceDistrictSelectedStr = districtFollowService.getProvinceDistrictSelectedStr(userId);
		categoryParentDTO.setProvinceDistrictSelectedStr(provinceDistrictSelectedStr);
		categoryParentDTO.setCategoriesSelectedStr(categoriesSelectedStr);
		categoryParentDTO.setCategories(categoryDTOs);
		return categoryParentDTO;
	}

	/**
	 * Update Last Notification.
	 * 
	 * @param poiId
	 */
	public void updateLastNotification(Long poiId) {
		PointOfInterest entity = pointOfInterestRepository.findOne(poiId);
		if (entity != null) {
			entity.setLastNotification(new Date());
			pointOfInterestRepository.save(entity);
		}
	}

	/**
	 * Get banner is approved.
	 * 
	 * @param categoryId
	 * @return List of PointOfInterestDTO
	 */
	public List<PointOfInterestDTO> getPoiBannerApproved(Long categoryId) {
		List<PointOfInterestDTO> dtos = new ArrayList<>();
		Category category = categoryRepository.findOne(categoryId);
		if (category != null) {
			List<PointOfInterest> entities = pointOfInterestRepository
					.findTop10ByCategoryAndBannerIsApproveAndIsDeletedAndIsApproved(category, 1, false, true);
			if (entities != null) {
				dtos = entities.stream().map(PointOfInterestDTO::toBannerApproved).collect(Collectors.toList());
			}
		}
		return dtos;
	}

	/**
	 * Convert obj to DTO
	 * 
	 * @param objs
	 * @return PointOfInterestDTO
	 */
	public PointOfInterestDTO objectsToDTO(Object[] objs) {

		PointOfInterestDTO dto = new PointOfInterestDTO();
		dto.setId(Long.valueOf(String.valueOf(objs[0])));
		dto.setName(PointOfInterestDTO.getStringValueOfObject(objs[1]));
		dto.setDescription(PointOfInterestDTO.getStringValueOfObject(objs[2]));
		List<String> phone = new ArrayList<>();
		phone.add(PointOfInterestDTO.getStringValueOfObject(objs[3]));
		dto.setPhone(phone);
		dto.setOwnerId(PointOfInterestDTO.getStringValueOfObject(objs[4]));

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			if (objs[5] != null) {
				String avatarUrl = String.valueOf(objs[5]);
				List<Image> avatarImages = objectMapper.readValue(avatarUrl,
						objectMapper.getTypeFactory().constructCollectionType(List.class, Image.class));
				dto.setAvatarImage(ImageDTO.from(avatarImages));
			}

			if (objs[15] != null) {
				String bannerUrl = String.valueOf(objs[15]);
				List<Image> bannerImages = objectMapper.readValue(bannerUrl,
						objectMapper.getTypeFactory().constructCollectionType(List.class, Image.class));
				dto.setBannerImages(ImageDTO.from(bannerImages));
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dto.setAddress(PointOfInterestDTO.getStringValueOfObject(objs[6]));

		if (objs[7] != null) {
			Long provinceId = Long.valueOf(String.valueOf(objs[7]));
			Province province = provinceRepository.findOne(provinceId);
			dto.setProvince(ProvinceDTO.from(province));

		}
		if (objs[8] != null) {
			Long districtId = Long.valueOf(String.valueOf(objs[8]));
			District district = districtRepository.findOne(districtId);
			dto.setDistrict(DistrictDTO.from(district));
		}
		if (objs[9] != null) {
			Long wardId = Long.valueOf(String.valueOf(objs[9]));
			Ward ward = wardRepository.findOne(wardId);
			dto.setWard(WardDTO.from(ward));
		}
		Double distance = Double.valueOf(String.valueOf(objs[10]));
		dto.setDistance(Double.parseDouble(String.valueOf(CommonUtils.milestoMeters(distance))));
		dto.setCloseHour(Double.valueOf(String.valueOf(objs[11])));
		dto.setOpenHour(Double.valueOf(String.valueOf(objs[12])));
		Boolean isOpening = Boolean.valueOf(String.valueOf(objs[13]));
		dto.setOpening(isOpening);
		if (objs[14] != null) {
			dto.setPoiState(PoiState.getById(Integer.valueOf(String.valueOf(objs[14]))));
		}
		Boolean isBannerApproved = Boolean.valueOf(String.valueOf(objs[16]));
		dto.setBannerApproved(isBannerApproved);
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setId(Long.valueOf(String.valueOf(objs[17])));
		dto.setCategory(categoryDTO);
		return dto;
	}

	/**
	 * Get pois user followed
	 * 
	 * @param userId
	 * @param pageable
	 * @return List of PointOfInterestDTO
	 */
	public List<PointOfInterestDTO> getPoiByUserLiked(String userId, Pageable pageable) {
		List<PoiAction> entities = poiActionRepository.getPoiByUserLiked(userId, pageable);
		if (entities != null) {
			List<PointOfInterestDTO> dtos = new ArrayList<>();
			for (PoiAction entity : entities) {
				PointOfInterest poi = entity.getPoi();
				if (poi != null) {
					PointOfInterestDTO dto = new PointOfInterestDTO();
					dto.setId(poi.id());
					dto.setName(poi.getName());
					dto.setLatitude(poi.latitude());
					dto.setLongitude(poi.longitude());
					dto.setPoiState(PoiState.getById(poi.getPoiState()));
					dto.setAvatarImage(ImageDTO.from(poi.getAvatarImage()));
					dtos.add(dto);
				}
			}
			return dtos;
		}
		return new ArrayList<>();
	}

}
