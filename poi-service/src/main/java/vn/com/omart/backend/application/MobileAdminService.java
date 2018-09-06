package vn.com.omart.backend.application;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.vdurmont.emoji.EmojiParser;

import lombok.extern.slf4j.Slf4j;
import vn.com.omart.backend.application.request.BannerCmd;
import vn.com.omart.backend.application.request.CategoryCmd;
import vn.com.omart.backend.application.request.ItemCmd;
import vn.com.omart.backend.application.request.POIOwnerCmd;
import vn.com.omart.backend.application.request.PointOfInterestCmd;
import vn.com.omart.backend.application.response.CategoryDTO;
import vn.com.omart.backend.application.response.DistrictDTO;
import vn.com.omart.backend.application.response.FullAddressDTO;
import vn.com.omart.backend.application.response.ItemDTO;
import vn.com.omart.backend.application.response.POIOwnerDTO;
import vn.com.omart.backend.application.response.PoiDTO;
import vn.com.omart.backend.application.response.PointOfInterestDTO;
import vn.com.omart.backend.application.response.ProvinceDTO;
import vn.com.omart.backend.application.response.RecruitmentPositionFollowDTO;
import vn.com.omart.backend.application.response.UserDTO;
import vn.com.omart.backend.application.response.WardDTO;
import vn.com.omart.backend.application.util.CommonUtils;
import vn.com.omart.backend.application.util.DateUtils;
import vn.com.omart.backend.application.util.XMLUtils;
import vn.com.omart.backend.constants.ConstantUtils;
import vn.com.omart.backend.constants.OmartType.PoiFeature;
import vn.com.omart.backend.constants.UserTitleEnum;
import vn.com.omart.backend.domain.model.Category;
import vn.com.omart.backend.domain.model.CategoryRepository;
import vn.com.omart.backend.domain.model.District;
import vn.com.omart.backend.domain.model.DistrictRepository;
import vn.com.omart.backend.domain.model.Image;
import vn.com.omart.backend.domain.model.Item;
import vn.com.omart.backend.domain.model.ItemGroup;
import vn.com.omart.backend.domain.model.ItemGroupRepository;
import vn.com.omart.backend.domain.model.ItemRepository;
import vn.com.omart.backend.domain.model.Owner;
import vn.com.omart.backend.domain.model.OwnerRepository;
import vn.com.omart.backend.domain.model.PoiItemGroup;
import vn.com.omart.backend.domain.model.PoiItemGroupRepository;
import vn.com.omart.backend.domain.model.PoiPicture;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.domain.model.PointOfInterestRepository;
import vn.com.omart.backend.domain.model.Province;
import vn.com.omart.backend.domain.model.ProvinceRepository;
import vn.com.omart.backend.domain.model.SupervisorTarget;
import vn.com.omart.backend.domain.model.SupervisorTargetRepository;
import vn.com.omart.backend.domain.model.UserProfile;
import vn.com.omart.backend.domain.model.UserProfileRepository;
import vn.com.omart.backend.domain.model.Ward;
import vn.com.omart.backend.domain.model.WardRepository;
import vn.com.omart.backend.port.adapter.elasticsearch.POI;
import vn.com.omart.backend.port.adapter.elasticsearch.POIRepo;
import vn.com.omart.backend.port.adapter.slack.SlackNotify;
import vn.com.omart.backend.port.adapter.userprofile.UserResponse;
import vn.com.omart.backend.port.adapter.userprofile.UserService;
import vn.com.omart.sharedkernel.application.model.error.ApplicationException;
import vn.com.omart.sharedkernel.application.model.error.InvalidInputException;
import vn.com.omart.sharedkernel.application.model.error.NotFoundException;

@Service
@Slf4j
public class MobileAdminService {

	private final static Logger logger = LoggerFactory.getLogger(MobileAdminService.class);

	@Autowired
	private UserProfileService userProfileService;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Value("${google.geocode-api}")
	private String Google_Geocode_API;

	@Value("${google.api-key}")
	private String Google_API_Key;

	@Value("${user.user-id}")
	private String userIds;

	@Value("${max_trial_days:90}")
	private int maxTrialDays;

	@Value("${default_password}")
	private String defaultPassword;

	@Autowired
	private PoiPictureService poiPictureService;

	@Autowired
	private CategoryFollowService categoryFollowService;

	@Autowired
	private DistrictFollowService districtFollowService;

	@Autowired
	private RecruitmentPositionFollowService recruitmentPositionFollowService;

	private final CategoryRepository categoryRepository;
	private final PointOfInterestRepository pointOfInterestRepository;
	private final ItemRepository itemRepository;
	private final SupervisorTargetRepository supervisorTargetRepository;

	private final ProvinceRepository provinceRepository;
	private final DistrictRepository districtRepository;
	private final WardRepository wardRepository;

	private final OwnerRepository ownerRepository;
	private final UserService userService;

	private final POIRepo poiRepo;
	private final SlackNotify slackNotify;

	@Autowired
	public MobileAdminService(CategoryRepository categoryRepository,
			PointOfInterestRepository pointOfInterestRepository, ItemRepository itemRepository,
			SupervisorTargetRepository supervisorTargetRepository, ProvinceRepository provinceRepository,
			DistrictRepository districtRepository, WardRepository wardRepository, OwnerRepository ownerRepository,
			UserService userService, POIRepo poiRepo, SlackNotify slackNotify) {
		this.categoryRepository = categoryRepository;
		this.pointOfInterestRepository = pointOfInterestRepository;
		this.itemRepository = itemRepository;
		this.supervisorTargetRepository = supervisorTargetRepository;
		this.provinceRepository = provinceRepository;
		this.districtRepository = districtRepository;
		this.wardRepository = wardRepository;
		this.ownerRepository = ownerRepository;
		this.userService = userService;
		this.poiRepo = poiRepo;
		this.slackNotify = slackNotify;
	}

	// ------------ CATEGORY ----------------

	public List<CategoryDTO> getCategories() {
		throw new RuntimeException("Not Implement Yet");
	}

	public CategoryDTO getCategory(Long cateId) {
		Category one = this.categoryRepository.findOne(cateId);
		return CategoryDTO.from(one);
	}

	// @CacheEvict(value = CacheName.CATEGORIES, allEntries = true)
	public CategoryDTO createCategory(CategoryCmd.CreateOrUpdate payload) {
		Category parent = null;
		if (payload.getParentId() != null) {
			parent = categoryRepository.findOne(payload.getParentId());
			if (parent == null) {
				throw new ApplicationException("Parent is not exists");
			}
		}

		Category model = new Category(payload.getName(), payload.getKeywords(), payload.getImageUrl(),
				payload.getTitleColor(), payload.getBackgroundColor(), payload.getDescription(), payload.getOrder(),
				parent);

		model = this.categoryRepository.save(model);

		return CategoryDTO.from(model);
	}

	// @CacheEvict(value = CacheName.CATEGORIES, allEntries = true)
	public CategoryDTO updateCategory(Long id, CategoryCmd.CreateOrUpdate payload) {

		Category parent = null;
		if (payload.getParentId() != null) {
			parent = categoryRepository.findOne(payload.getParentId());
			if (parent == null) {
				throw new ApplicationException("Parent is not exists");
			}
		}

		Category model = this.categoryRepository.findOne(id);

		model.update(payload.getName(), payload.getKeywords(), payload.getImageUrl(), payload.getTitleColor(),
				payload.getBackgroundColor(), payload.getDescription(), payload.getOrder(), parent);

		model = this.categoryRepository.save(model);
		return CategoryDTO.from(model);
	}

	public CategoryDTO updateCategory(String userId, Long id, CategoryCmd.Update payload) {

		SlackNotify.SlackBody abc = new SlackNotify.SlackBody();
		abc.setUsername("AdminTool - Category is Updated");
		String content = " User=" + userId;
		content += " \n Updated Category=" + id;
		content += " \n At=" + new Date().toString();

		Category model = this.categoryRepository.findOne(id);
		content += " \n Name  From=" + model.name() + " To=" + payload.getName();
		content += " \n Image From=" + model.image() + " To=" + payload.getImageUrl();

		model = this.categoryRepository
				.save(model.update(payload.getName(), payload.getKeywords(), payload.getImageUrl()));

		abc.setText(content);
		slackNotify.post(abc);

		return CategoryDTO.from(model);
	}

	// ------------ END CATEGORY ----------------

	// ------------ Point Of Interest ----------------

	public List<PointOfInterestDTO> getPOIByCreator(String username) {
		return this.pointOfInterestRepository.findByCreatedBy(username).stream().map(PointOfInterestDTO::from)
				.collect(Collectors.toList());
	}

	public PointOfInterestDTO getPOI(Long poiId, String username) {
		PointOfInterest poi = this.pointOfInterestRepository.findByIdAndCreatedBy(poiId, username);
		if (null == poi) {
			throw new NotFoundException("PointOfInterest not exists");
		}

		Owner owner = this.ownerRepository.findByUserId(poi.ownerId());
		if (null == owner) {
			throw new NotFoundException("Owner not exists");
		}

		return PointOfInterestDTO.from(poi);
	}

	public PointOfInterestDTO getPOI(Long poiId) {
		PointOfInterest poi = this.pointOfInterestRepository.findOne(poiId);
		if (null == poi) {
			throw new NotFoundException("PointOfInterest not exists");
		}

		return PointOfInterestDTO.from(poi);
	}

	public PointOfInterestDTO createPointOfInterest(String username, String creatorLatLon,
			PointOfInterestCmd.CreateOrUpdate payload) {

		// validate user - district
		boolean isByPass = isByPassUserProvince(username);
		if (!isByPass) {
			boolean needCheckProvince = !payload.isNoCheckProvince();
			if (needCheckProvince) {
				List<Province> allByUser = this.provinceRepository.findAllByUser(username,
						new Long(payload.getProvinceId()));
				if (allByUser.isEmpty()) {
					throw new AccessDeniedException("You are not allow for posting Shop on this Province");
				}
			}
		}

		Category category = this.categoryRepository.findOne(payload.getCategoryId());
		if (null == category) {
			throw new NotFoundException("Category not exists");
		}

		Owner owner = this.ownerRepository.findByUserId(payload.getOwnerId());
		if (null == owner) {
			throw new NotFoundException("Owner not exists");
		}

		Province province = this.provinceRepository.findOne(Long.valueOf(payload.getProvinceId()));
		if (null == province) {
			throw new NotFoundException("Province not exists");
		}

		District district = this.districtRepository.findByIdAndProvinceId(Long.valueOf(payload.getDistrictId()),
				province.id());
		if (null == district) {
			throw new NotFoundException("District not exists");
		}

		Ward ward = this.wardRepository.findByIdAndDistrictId(Long.valueOf(payload.getWardId()), district.id());
		if (null == ward) {
			throw new NotFoundException("Ward not exists");
		}

		Double latOfCreator = Double.valueOf(creatorLatLon.split(",")[0]);
		Double lonOfCreator = Double.valueOf(creatorLatLon.split(",")[1]);

		// TODO: validate salesman lat/lon vs shop lat/lon
		// distance(latOfCreator, lonOfCreator, payload.getLatitude(),
		// payload.getLongitude(), "M");
		String desc = payload.getDescription() == null ? "" : EmojiParser.parseToAliases(payload.getDescription());
		PointOfInterest model = PointOfInterest.builder().category(category).ownerId(owner.userId())
				.name(payload.getName()).description(desc).career(payload.getCareer()).address(payload.getAddress())
				.province(province).district(district).ward(ward).snapshotMap(payload.getSnapshotMap())
				.phone(String.join(",", payload.getPhone())).latitude(payload.getLatitude())
				.longitude(payload.getLongitude()).openHour(payload.getOpenHour()).closeHour(payload.getCloseHour())
				.openingState(1)
				.avatarImage(payload.getAvatarImage().stream().map(imageCmd -> new Image(imageCmd.getUrl()))
						.collect(Collectors.toList()))
				.coverImage(payload.getCoverImage().stream().map(imageCmd -> new Image(imageCmd.getUrl()))
						.collect(Collectors.toList()))
				.featuredImage(payload.getFeaturedImage().stream().map(imageCmd -> new Image(imageCmd.getUrl()))
						.collect(Collectors.toList()))
				.bannerImages(new ArrayList<>()).createdBy(username).createdAt(new Date()).latOfCreator(latOfCreator)
				.lonOfCreator(lonOfCreator).webAddress(payload.getWebAddress()).email(payload.getEmail())
				.currency(payload.getCurrency()).deliveryRadius(payload.getDeliveryRadius())
				.isApproved(true).poiType(payload.getPoiType()).build();
		try {
			model = this.pointOfInterestRepository.save(model);
		} catch (Exception e) {
			String descNormalize = CommonUtils.emoijNormalize(model.getDescription());
			model.setDescription(descNormalize);
			model = this.pointOfInterestRepository.save(model);
		}

		// save poi picture
		List<PointOfInterestCmd.ImageCmd> imageCmd = payload.getFeaturedImage();
		if (!imageCmd.isEmpty()) {
			List<PoiPicture> poiPictures = new ArrayList<>();
			List<Image> images = new ArrayList<>();
			imageCmd.forEach(item -> {
				images.add(new Image(item.getUrl()));
			});
			PoiPicture picItem = new PoiPicture();
			picItem.setUrls(images);
			poiPictures.add(picItem);

			PoiDTO poiPicture = new PoiDTO();
			poiPicture.setPoiPictures(poiPictures);
			poiPicture.setPoiId(model.id());
			poiPictureService.savePoiPicture(poiPicture, model.ownerId());
		}

		// TODO: Indexing es
		POI poi = POI.from(model);
		this.poiRepo.save(poi);

		return PointOfInterestDTO.from(model);
	}

	// @CachePut(value = CacheName.POI, key = "#payload.categoryId + ':' + #id")
	public PointOfInterestDTO updatePointOfInterest(Long id, String username,
			PointOfInterestCmd.CreateOrUpdate payload) {
		PointOfInterest model = this.pointOfInterestRepository.findOne(id);
		if (null == model) {
			throw new NotFoundException("PointOfInterest not exists");
		}

		Category category = this.categoryRepository.findOne(payload.getCategoryId());
		if (null == category) {
			throw new NotFoundException("Category not exists");
		}

		Owner owner = this.ownerRepository.findByUserId(payload.getOwnerId());
		if (null == owner) {
			throw new NotFoundException("Owner not exists");
		}

		Province province = this.provinceRepository.findOne(Long.valueOf(payload.getProvinceId()));
		if (null == province) {
			throw new NotFoundException("Province not exists");
		}

		District district = this.districtRepository.findByIdAndProvinceId(Long.valueOf(payload.getDistrictId()),
				province.id());
		if (null == district) {
			throw new NotFoundException("District not exists");
		}

		Ward ward = this.wardRepository.findByIdAndDistrictId(Long.valueOf(payload.getWardId()), district.id());
		if (null == ward) {
			throw new NotFoundException("Ward not exists");
		}

		PointOfInterest.PointOfInterestBuilder pointOfInterestBuilder = PointOfInterest.builder().id(model.id())
				.category(category)
				// .owner(owner)
				.ownerId(owner.userId()).name(payload.getName()).description(payload.getDescription())
				.address(payload.getAddress()).province(province).district(district).ward(ward)
				.snapshotMap(payload.getSnapshotMap()).phone(String.join(",", payload.getPhone()))
				.latitude(payload.getLatitude()).longitude(payload.getLongitude()).openHour(payload.getOpenHour())
				.closeHour(payload.getCloseHour()).openingState(payload.isOpening() ? 1 : 0)
				.avatarImage(payload.getAvatarImage().stream().map(imageCmd -> new Image(imageCmd.getUrl()))
						.collect(Collectors.toList()))
				.coverImage(payload.getCoverImage().stream().map(imageCmd -> new Image(imageCmd.getUrl()))
						.collect(Collectors.toList()))
				.featuredImage(payload.getFeaturedImage().stream().map(imageCmd -> new Image(imageCmd.getUrl()))
						.collect(Collectors.toList()))
				.createdBy(model.createdBy()).createdAt(model.createdAt()).updatedBy(username).updatedAt(new Date());

		if (null != model.bannerImages()) {
			pointOfInterestBuilder.bannerImages(model.bannerImages());
		}

		model = pointOfInterestBuilder.build();

		model = this.pointOfInterestRepository.save(model);

		// TODO: Indexing es
		POI poi = POI.from(model);
		this.poiRepo.save(poi);

		return PointOfInterestDTO.from(model);
	}

	// ------------ END Point Of Interest ----------------

	// ------------ Item ----------------

	public List<ItemDTO> getItemsByPOI(Long poiId) {
		return this.itemRepository.findByPoiId(poiId).stream().map(ItemDTO::from).collect(Collectors.toList());
	}

	public ItemDTO getItemByPOI(Long poiId, Long itemId) {
		Item item = this.itemRepository.findByIdAndPoiId(itemId, poiId);

		if (null == item) {
			throw new NotFoundException("Item not exists");
		}

		return ItemDTO.from(item);
	}

	@Autowired
	private ItemGroupRepository itemGroupRepository;

	@Autowired
	private PoiItemGroupRepository poiItemGroupRepository;

	@Transactional(readOnly = false)
	public ItemDTO createItem(Long poiId, ItemCmd.CreateOrUpdate payload) {
		Item model = Item.builder().poi(pointOfInterestRepository.findOne(poiId)).name(payload.getName()).description(payload.getDescription())
				.unitPrice(payload.getUnitPrice())
				.avatarImage(payload.getAvatarImage().stream().map(imageCmd -> new Image(imageCmd.getUrl()))
						.collect(Collectors.toList()))
				.coverImage(payload.getCoverImage().stream().map(imageCmd -> new Image(imageCmd.getUrl()))
						.collect(Collectors.toList()))
				.featuredImage(payload.getFeaturedImage().stream().map(imageCmd -> new Image(imageCmd.getUrl()))
						.collect(Collectors.toList()))
				.isOutOfStock(payload.isOutOfStock()).createdBy("system").createdAt(new Date()).build();
		model = this.itemRepository.save(model);

		// add group item.
		if(payload.getGroupIds() != null) {
			List<Long> groupIds = payload.getGroupIds();
			if (!groupIds.isEmpty()) {
				insertItemGroup(groupIds, model);
			} else {
				// other.
				ItemGroup itemGroup = new ItemGroup();
				itemGroup.setItem(model);
				itemGroup.setPoiId(poiId);
				itemGroupRepository.save(itemGroup);
			}
		}
		// end
		return ItemDTO.from(model);
	}

	@Transactional(readOnly = false)
	public ItemDTO updateItem(Long id, ItemCmd.CreateOrUpdate payload) {
		Item model = this.itemRepository.findOne(id);
		
		model = Item.builder().poi(model.getPoi()).id(id).name(payload.getName()).description(payload.getDescription())
				.unitPrice(payload.getUnitPrice())
				.avatarImage(payload.getAvatarImage().stream().map(imageCmd -> new Image(imageCmd.getUrl()))
						.collect(Collectors.toList()))
				.coverImage(payload.getCoverImage().stream().map(imageCmd -> new Image(imageCmd.getUrl()))
						.collect(Collectors.toList()))
				.featuredImage(payload.getFeaturedImage().stream().map(imageCmd -> new Image(imageCmd.getUrl()))
						.collect(Collectors.toList()))
				.isOutOfStock(payload.isOutOfStock()).createdBy(model.createdBy()).createdAt(model.createdAt())
				.isPosted(model.isPosted())
				.postedAt(model.getPostedAt())
				.catId(model.getCatId())
				.updatedBy("system").updatedAt(new Date()).build();
		
		model = this.itemRepository.save(model);
		
		// reset group item
		List<ItemGroup> itemGroups = itemGroupRepository.findByItemAndPoiId(model, model.getPoi().id());
		if (!itemGroups.isEmpty()) {
			itemGroupRepository.delete(itemGroups);
		}
		// add new group item.
		if(payload.getGroupIds() != null) {
			List<Long> groupIds = payload.getGroupIds();
			if (!groupIds.isEmpty()) {
				insertItemGroup(groupIds, model);
			} else {
				// other.
				ItemGroup itemGroup = new ItemGroup();
				itemGroup.setItem(model);
				itemGroup.setPoiId(model.getPoi().id());
				itemGroupRepository.save(itemGroup);
			}
		}
		// end
		return ItemDTO.from(model);
	}

	private void insertItemGroup(List<Long> groupIds, Item model) {
		List<ItemGroup> entities = new ArrayList<>();
		for (Long id : groupIds) {
			PoiItemGroup poiItemGroup = poiItemGroupRepository.findOne(id);
			if (poiItemGroup != null) {
				ItemGroup itemGroup = new ItemGroup();
				itemGroup.setGroup(poiItemGroup);
				itemGroup.setItem(model);
				itemGroup.setPoiId(model.getPoi().id());
				entities.add(itemGroup);
			}
		}
		if (!entities.isEmpty()) {
			itemGroupRepository.save(entities);
		}
	}

	// ------------ END Item ----------------

	// ------------ POI Owner ----------------

	// public List<POIOwnerDTO> getAllOwner() {
	// return this.ownerRepository.findAll().stream()
	// .map(POIOwnerDTO::from)
	// .collect(Collectors.toList());
	// }

	public List<POIOwnerDTO> getAllOwnerByCreator(String username) {
		return this.ownerRepository.findByCreatedBy(username).stream()
				.sorted(Comparator.comparing(Owner::createdAt).reversed()).map(POIOwnerDTO::from)
				.collect(Collectors.toList());
	}

	public POIOwnerDTO getOwner(String ownerId) {

		UserResponse user = userService.getOwner(ownerId);
		if (user == null) {
			throw new NotFoundException("User not exists for this owner");
		}

		POIOwnerDTO ownerDTO = new POIOwnerDTO.Mapper().map(user);

		String title = user.getTitle();
		boolean isOwner = UserTitleEnum.OWNER.name().equalsIgnoreCase(title);

		if (isOwner) {
			Owner owner = ownerRepository.findByUserId(user.getId());

			if (owner == null) {
				throw new NotFoundException("Owner not exists");
			}

			ownerDTO = addMoreInfo(owner, ownerDTO, user.getManagedBy());
		}

		return ownerDTO;
	}

	/**
	 * Get owner of poi.
	 *
	 * @param ownerId
	 * @return POIOwnerDTO
	 */
	public POIOwnerDTO getOwner_V1(String ownerId) {
		POIOwnerDTO ownerDTO = this.getOwner(ownerId);
		if (ownerDTO != null) {
			PointOfInterest poi = pointOfInterestRepository.findTop1ByOwnerId(ownerId);
			if (poi != null) {
				ownerDTO.setPoiId(poi.id());
			} else {
				ownerDTO.setPoiId(0L);
			}
		}
		String categoriesSelectedStr = categoryFollowService.getCategoryFollowByUserId(ownerId);
		ownerDTO.setCategoriesSelectedStr(categoriesSelectedStr);
		ownerDTO.setProvinceDistrictSelectedStr(districtFollowService.getProvinceDistrictSelectedStr(ownerId));
		RecruitmentPositionFollowDTO rectPosFlDto = recruitmentPositionFollowService
				.getPositionFollowDTOByUserId(ownerId);
		ownerDTO.setRecruitmentPositionLevelsSelectedStr(rectPosFlDto.getPositionsSelectedStr());
		ownerDTO.setRecruitmentProvinceDistrictSelectedStr(rectPosFlDto.getProvinceDistrictSelectedStr());
		return ownerDTO;
	}

	@Transactional
	public POIOwnerDTO createOwner(String username, POIOwnerCmd.CreateOrUpdate payload, boolean upgradeFromUser) {

		String ownerPassword = payload.getPassword();

		// Create auth_user with default password and active flag is true
		if (!upgradeFromUser) {
			payload.setPassword(defaultPassword);
		}
		payload.setActivated(true);

		UserResponse owner = userService.createOrUpdateOwner(username, payload);

		String phoneNumber = payload.getPhoneNumber();

		Owner model = this.ownerRepository.findByPhoneNumber(phoneNumber);
		if (model != null) {
			POIOwnerDTO ownerDTO = POIOwnerDTO.from(model);
			ownerDTO = addMoreInfo(model, ownerDTO, "");
			return ownerDTO;
		}

		model = new Owner(payload.getName(), payload.getAvatar(), phoneNumber, ownerPassword, username, owner.getId());

		boolean isPaid = true;

		if (upgradeFromUser) {
			isPaid = false;
		}

		model.setTrialAt(DateUtils.getCurrentDate());
		model.setPaid(isPaid);

		model = this.ownerRepository.save(model);

		POIOwnerDTO ownerDTO = POIOwnerDTO.from(model);
		// ownerDTO = addMoreInfo(model, ownerDTO);

		ownerDTO.setNew(owner.isNew());

		if (upgradeFromUser) {
			ownerDTO.setRemainingTrialDays(90);
		}
		return ownerDTO;
	}

	@Transactional
	public POIOwnerDTO updateOwner(String ownerId, String username, POIOwnerCmd.CreateOrUpdate payload) {

		Owner model = this.ownerRepository.findByUserId(ownerId);

		if (null == model) {
			throw new NotFoundException("POI Owner not exists");
		}

		model.setPassword(payload.getPassword());
		model.setUpdatedBy(username);
		model.setUpdatedAt(DateUtils.getCurrentDate());

		model.setName(payload.getName());
		model.setAvatar(payload.getAvatar());
		model.setPhoneNumber(payload.getPhoneNumber());

		model = this.ownerRepository.save(model);

		// Update to auth_service
		userService.updateOwner(ownerId, username, payload);

		return POIOwnerDTO.from(model);
	}

	@Deprecated
	public void changePassword(String ownerId, POIOwnerCmd.ChangePassword payload) {

		if (payload == null || StringUtils.isEmpty(payload.getCurrentPassword())) {
			throw new InvalidInputException("Current password should not be null");
		}

		Owner model = this.ownerRepository.findByUserId(ownerId);

		if (model == null) {
			throw new InvalidInputException("Owner does not exist or has just been deleted");
		}

		if (!payload.getCurrentPassword().equals(model.password())) {
			throw new InvalidInputException("Current password does not match");
		}

		POIOwnerCmd.CreateOrUpdate request = new POIOwnerCmd.CreateOrUpdate();
		request.setName(model.name());
		request.setPhoneNumber(model.phoneNumber());
		request.setPassword(payload.getPassword());

		userService.updateOwner(ownerId, model.userId(), request);

		model.setPassword(payload.getPassword());

		this.ownerRepository.save(model);
	}

	// ------------ END POI Owner ----------------

	// ------------ POI Banner ----------------

	public List<PointOfInterestDTO> getPOIsHavingBanner(String createdBy) {
		return this.pointOfInterestRepository.findAllByBannerImagesIsNotNullOrderByUpdatedAtDesc().stream()
				.map(new PointOfInterestDTO.BannerPOIMapper()::map)
				.filter(pointOfInterestDTO -> !pointOfInterestDTO.getBannerImages().isEmpty())
				.collect(Collectors.toList());
	}

	public Page<PointOfInterestDTO> getPOIsHavingBanner(int page, int pageSize) {
		List<String> filters = new ArrayList<>();
		// filters.add("status!{}" + GameMasterStatus.DELETED);
		// filters.add("appId="+appId);
		// if (filterQueries != null && filterQueries.length > 0) {
		// Collections.addAll(filters, filterQueries);
		// }
		// Sort orders = new Sort(new Sort.Order(Sort.Direction.fromString("asc"),
		// "name"));
		// GameMasterQuerySpecification querySpecification = new
		// GameMasterQuerySpecification(filters);
		Page<PointOfInterest> rs = pointOfInterestRepository
				.findAllByBannerImagesIsNotNullOrderByUpdatedAtDesc(new PageRequest(page - 1, pageSize));
		return rs.map(new PointOfInterestDTO.BannerPOIMapper()::map);
	}

	public List<PointOfInterestDTO> searchPOIsHavingBanner(String sText, String createdBy) {
		return this.pointOfInterestRepository.findAllByNameLike("%" + sText + "%").stream()
				.map(new PointOfInterestDTO.BannerPOIMapper()::map).collect(Collectors.toList());
	}

	public PointOfInterestDTO getPOIHavingBanner(Long poiId, String createdBy) {
		PointOfInterest poi = this.pointOfInterestRepository.findOne(poiId);
		if (null == poi) {
			throw new NotFoundException("POI not exists or not created by you");
		}
		return new PointOfInterestDTO.BannerPOIMapper().map(poi);
	}

	public PointOfInterestDTO updateBanner(Long poiId, String createdBy, BannerCmd.CreateOrUpdate payload) {
		PointOfInterest poi = this.pointOfInterestRepository.findOne(poiId);
		poi = this.pointOfInterestRepository.save(poi.updateBanner(payload.getImageUrl()));
		return new PointOfInterestDTO.BannerPOIMapper().map(poi);
	}

	public PointOfInterestDTO approveBanner(Long poiId, String createdBy, boolean isApprove) {
		PointOfInterest poi = this.pointOfInterestRepository.findOne(poiId);
		if (isApprove && poi.bannerImages().isEmpty()) {
			throw new ApplicationException("This POI having banner before approve");
		}
		poi = this.pointOfInterestRepository.save(poi.approveBanner(isApprove));
		return new PointOfInterestDTO.BannerPOIMapper().map(poi);
	}

	public ProvinceDTO getOwnerProvince(String ownerPhone) {
		return getOwnerProvince(ownerPhone, null);
	}

	public ProvinceDTO getOwnerProvince(String ownerPhone, String managedBy) {

		Owner owner = this.ownerRepository.findByPhoneNumber(ownerPhone);

		return getOwnerProvince(owner, managedBy);
	}

	public ProvinceDTO getOwnerProvince(Owner owner, String managedBy) {

		if (owner == null) {
			throw new NotFoundException("Owner not exists");
		}

		String salemanId = owner.createdBy();
		if (salemanId == null) {
			salemanId = owner.updatedBy();
		}

		SupervisorTarget target = supervisorTargetRepository.findOne(salemanId);

		if (target == null) {

			if (StringUtils.isNotEmpty(managedBy)) {
				salemanId = managedBy;

			} else {
				UserResponse user = userService.getOwner(owner.userId());
				if (user == null) {
					throw new NotFoundException("User not exists for this owner");
				}

				salemanId = user.getManagedBy();
			}

			target = supervisorTargetRepository.findOne(salemanId);
		}

		if (target == null) {
			throw new NotFoundException("Saleman not exists");
		}

		Province provice = target.province();
		if (provice == null) {
			throw new NotFoundException("Province of saleman not exists");
		}

		return ProvinceDTO.from(provice);
	}

	private POIOwnerDTO addMoreInfo(Owner owner, POIOwnerDTO ownerDTO, String managedBy) {

		// ProvinceDTO province = getOwnerProvince(owner, managedBy);

		Date trialAt = owner.getTrialAt();
		boolean isPaid = owner.isPaid();
		int remainingTrialDays = 0;

		if (trialAt == null) {
			trialAt = DateUtils.getCurrentDate();

			Owner owner1 = ownerRepository.findOne(owner.id());
			owner1.setTrialAt(trialAt);
			ownerRepository.save(owner1);
		}

		int trialDays = DateUtils.daysBetweenCurrent(trialAt);
		remainingTrialDays = maxTrialDays - trialDays;

		ownerDTO.setRemainingTrialDays(remainingTrialDays);
		ownerDTO.setPaid(isPaid);
		// ownerDTO.setProvince(province);

		return ownerDTO;
	}

	// ------------ END POI Banner ----------------

	/**
	 * Anh hung required to allway return all provinces if user in the properties
	 * config.
	 *
	 * @param userId
	 * @return
	 */
	public boolean isByPassUserProvince(String userId) {
		if (!StringUtils.isBlank(userIds)) {
			List<String> users = Arrays.asList(userIds.split(","));
			if (users.contains(userId)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get province.
	 *
	 * @param latlng
	 * @return ProvinceDTO
	 */
	public ProvinceDTO getProvince(String latlng) {
		// get document.
		String xml = getDocumentFromGoogle(latlng);
		// XML Query
		ProvinceDTO province = new ProvinceDTO();
		String provinceName = getAddressByComponentType(xml, ConstantUtils.ADMINISTRATIVE_AREA_LEVEL_1);
		province.setName(provinceName);
		return province;
	}

	/**
	 * Get status of xml response from google api.
	 *
	 * @param xml
	 * @return Status
	 */
	public String getXMLStatus(String xml) {
		String query = "/GeocodeResponse/status/text()";
		String status = XMLUtils.excuteXpathQuery(xml, query);
		return status;
	}

	/**
	 * Get full address too populate to user.
	 *
	 * @param latlng
	 * @return FullAddressDTO
	 */
	public FullAddressDTO getFullAddress(String latlng) {
		FullAddressDTO fullAddress = new FullAddressDTO();
		try {
			// get document.
			String xml = this.getDocumentFromGoogle(latlng);
			// XML Query
			String gStatus = this.getXMLStatus(xml);
			if (gStatus.equalsIgnoreCase("OK")) {
				String provinceName = getAddressByComponentType(xml, ConstantUtils.ADMINISTRATIVE_AREA_LEVEL_1);
				String districtName = getAddressByComponentType(xml, ConstantUtils.ADMINISTRATIVE_AREA_LEVEL_2);
				String wardName1 = getAddressByComponentType(xml, ConstantUtils.ADMINISTRATIVE_AREA_LEVEL_3);
				String wardName2 = getAddressByComponentType(xml, ConstantUtils.SUBLOCALITY_LEVEL_1);
				String streetName = getAddressByComponentType(xml, ConstantUtils.ROUTE);
				String streetNumber = getAddressByComponentType(xml, ConstantUtils.STREET_NUMBER);
				// Checking in DB
				if (!StringUtils.isBlank(provinceName)) {
					List<String> wordsProvince = Arrays.asList("province", "tỉnh", "tĩnh", "tinh");
					provinceName = removeWords(provinceName, wordsProvince);
					provinceName = CommonUtils.removeHyphen(provinceName);
					Province province = provinceRepository.findByNameContaining(provinceName);
					List<District> district = null;
					List<Ward> ward = null;
					if (province != null) {
						fullAddress.setProvince(ProvinceDTO.from(province));
						if (!StringUtils.isBlank(districtName)) {
							List<String> wordsDistrict = Arrays.asList("District", "Huyện", "huyen");
							districtName = removeWords(districtName, wordsDistrict);
							district = districtRepository.findByProvinceIdAndNameContaining(province.id(),
									districtName);
							if (district != null || !district.isEmpty()) {
								fullAddress.setDistrict(DistrictDTO.from(district.get(0)));
								if (!StringUtils.isBlank(wardName1)) {
									List<String> wordWards = Arrays.asList("Xã", "p.", "phường", "phuong", "xa");
									wardName1 = removeWords(wardName1, wordWards);
									ward = wardRepository.findByDistrictIdAndProvinceIdAndNameContaining(
											district.get(0).id(), district.get(0).provinceId(), wardName1);
									if (!StringUtils.isBlank(wardName2) && (ward == null || ward.isEmpty())) {
										wardName2 = removeWords(wardName2, wordWards);
										ward = wardRepository.findByDistrictIdAndProvinceIdAndNameContaining(
												district.get(0).id(), district.get(0).provinceId(), wardName2);
										if (ward != null && !ward.isEmpty()) {
											fullAddress.setWard(WardDTO.from(ward.get(0)));
											fullAddress.setStreet(formatStreet(streetNumber, streetName));
										}
									} else {
										fullAddress.setWard(WardDTO.from(ward.get(0)));
										fullAddress.setStreet(formatStreet(streetNumber, streetName));
									}
								}
							}
						}
					}
				}
			} else {
				logger.error("Get address form google api: " + xml);
			}
		} catch (Exception e) {
			logger.error("Get address from google api " + e.getMessage());
		}
		return fullAddress;
	}

	/**
	 * Get Document From Google.
	 *
	 * @param latlng
	 * @return
	 */
	public String getDocumentFromGoogle(String latlng) {
		String transactionUrl = Google_Geocode_API + ConstantUtils.XML + "?latlng={latlng}&key="
				+ CommonUtils.getGoogleAPIKey(Google_API_Key);
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(transactionUrl);
		RestTemplate restTemplate = new RestTemplate();

		// set params
		Map<String, String> uriParams = new HashMap<String, String>();
		uriParams.put("latlng", latlng);

		// invoke the google api
		URI uri = builder.buildAndExpand(uriParams).toUri();
		String xml = restTemplate.getForObject(uri, String.class);
		return xml;
	}

	/**
	 * Address Full.
	 *
	 * @param latlng
	 * @return FullAddressDTO
	 */
	public FullAddressDTO getFullAddressFromGoogle(String latlng) {
		FullAddressDTO fullAddress = new FullAddressDTO();
		String xml = getDocumentFromGoogle(latlng);
		String address = getFormattedAddressByComponentType(xml);
		if (address.contains(ConstantUtils.UNNAMED_ROAD)) {
			String text = address.replaceAll(ConstantUtils.UNNAMED_ROAD, "").replaceFirst(",", "");
			fullAddress.setFullAddress(text);
			return fullAddress;
		}
		fullAddress.setFullAddress(address);
		return fullAddress;
	}

	/**
	 * Remove word in list.
	 *
	 * @param value
	 * @param words
	 * @return String
	 */
	public String removeWords(String value, List<String> words) {
		String text = value.toLowerCase();
		for (String word : words) {
			if (text.contains(word.toLowerCase())) {
				text = text.replaceAll(word.toLowerCase(), "");
			}
		}
		return text.trim();
	}

	/**
	 * Get Address By Component Type.
	 *
	 * @param xml
	 * @param type
	 * @return String
	 */
	public String getAddressByComponentType(String xml, String type) {
		String query = "/GeocodeResponse/result[1]/address_component[type='" + type + "']/long_name/text()";
		String address = XMLUtils.excuteXpathQuery(xml, query);
		return address;
	}

	/**
	 * Get Address Full By Component Type.
	 *
	 * @param xml
	 * @param type
	 * @return String
	 */
	public String getFormattedAddressByComponentType(String xml) {
		String query = "/GeocodeResponse/result[1]/formatted_address[1]/text()";
		String address = XMLUtils.excuteXpathQuery(xml, query);
		return address;
	}

	/**
	 * Format street.
	 *
	 * @param streetNumber
	 * @param streetName
	 * @return String
	 */
	public static String formatStreet(String streetNumber, String streetName) {
		if (streetNumber != null && streetName != null) {
			if (streetName.contains(ConstantUtils.UNNAMED_ROAD)) {
				return "";
			}
			return (streetNumber + " " + streetName);
		}
		if (streetNumber == null && streetName == null) {
			return "";
		}
		if (streetNumber == null && streetName != null) {
			if (streetName.contains(ConstantUtils.UNNAMED_ROAD)) {
				return "";
			}
			return streetName;
		}
		return streetNumber;
	}

	/**
	 * Update Poi Cover Image.
	 *
	 * @param poiId
	 * @param images
	 * @return List of Image
	 */
	public List<Image> updatePoiCoverImage(Long poiId, List<Image> images) {
		PointOfInterest poiUpdated = null;
		PointOfInterest poi = pointOfInterestRepository.findOne(poiId);
		if (poi == null) {
			throw new NotFoundException("Poi not found");
		}
		poi.setCoverImage(images);
		poiUpdated = pointOfInterestRepository.save(poi);
		return poiUpdated.getCoverImage();
	}

	/**
	 * Update Poi Avatar.
	 *
	 * @param poiId
	 * @param images
	 * @return List of Image
	 */
	public List<Image> updatePoiAvatar(Long poiId, List<Image> images) {
		PointOfInterest poiUpdated = null;
		PointOfInterest poi = pointOfInterestRepository.findOne(poiId);
		if (poi == null) {
			throw new NotFoundException("Poi not found");
		}
		poi.setAvatarImage(images);
		poiUpdated = pointOfInterestRepository.save(poi);
		return poiUpdated.getAvatarImage();
	}

	/**
	 * Delete Item By Id.
	 *
	 * @param itemId
	 */
	@Transactional
	public void deleteItemById(Long itemId) {
		Item item = itemRepository.findOne(itemId);
		if (item == null) {
			throw new NotFoundException("Item not found");
		}
		itemRepository.delete(item);
	}

	/**
	 * Approve by omart admin.
	 *
	 * @param id
	 * @param action
	 */
	public void approval(Long id, String action) {
		PointOfInterest poi = pointOfInterestRepository.findOne(id);
		if (poi != null) {
			if (ConstantUtils.ACCEPT.equals(action)) {
				poi.setApproved(true);
			} else if (ConstantUtils.REJECT.equals(action)) {
				poi.setApproved(false);
			}
			pointOfInterestRepository.save(poi);
		}
	}

	/**
	 * Transfer shop.
	 *
	 * @param poiId
	 * @param phoneNumber
	 * @param userId
	 * @return UserDTO
	 */
	public UserDTO poiAdminTransfered(Long poiId, String phoneNumber, String userId) {
		UserProfile user = userProfileRepository.findByPhone(phoneNumber);
		PointOfInterest poi = pointOfInterestRepository.findByIdAndIsDeletedAndIsApproved(poiId, false, true);
		if (poi != null) {
			if (poi.getOwnerId().equals(userId)) {
				UserDTO dto = new UserDTO();
				if (user == null) {
					dto.setId(userId);
					if (!poi.avatarImage().isEmpty()) {
						dto.setAvatar(poi.avatarImage().get(0).getUrl());
					}
					dto.setFirstname(poi.name());
					dto.setPhoneNumber(phoneNumber);
					dto = userService.createAccount(dto);
					if (dto != null) {
						user = userProfileService.fastCreate(dto, poi);
					}
				}
				if (user != null) {
					poi = this.setTransfer(user, poi);
					if (poi != null) {
						UserDTO dtoResponse = new UserDTO();
						dtoResponse.setUsername(dto.getUsername());
						dtoResponse.setPassword(dto.getPassword());
						return dtoResponse;
					}
				}
			} else {
				logger.error("\n[SHOP TRANSFER] you only transfer your shop");
			}
		}
		return null;
	}

	/**
	 * Action transfer.
	 *
	 * @param user
	 * @param poi
	 * @return PointOfInterest
	 */
	private PointOfInterest setTransfer(UserProfile user, PointOfInterest poi) {
		poi.setPhone(user.getPhone());
		poi.setOwnerId(user.getUserId());
		poi = pointOfInterestRepository.save(poi);
		return poi;
	}

	/**
	 * [TOOL ADMIN] Filter date of create at.
	 *
	 * @param from
	 * @param to
	 * @param pageable
	 * @return List of PointOfInterestDTO
	 */
	public List<PointOfInterestDTO> getShopBetweenDate(Long from, Long to, Pageable pageable) {
		List<PointOfInterestDTO> dtos = new ArrayList<>();
		Date fromDate = new Date(from);
		Date toDate = new Date(to);
		String strTo = DateUtils.getDateByFormat(toDate, ConstantUtils.YYYYMMDD);
		String strFrom = DateUtils.getDateByFormat(fromDate, ConstantUtils.YYYYMMDD);
		List<PointOfInterest> pois = pointOfInterestRepository.getByCreatedAtBetween(strFrom, strTo, pageable);
		if (pois != null) {
			dtos = pois.stream().map(PointOfInterestDTO::from).collect(Collectors.toList());
		}
		return dtos;
	}

	public ResponseEntity<Page<PointOfInterestDTO>> getShopBetweenDateAndProvince(Long from, Long to, Long provinceId,
			Pageable pageable) {

		boolean isIgnoreDate = true;
		boolean isIgnoreProvince = true;
		String strTo = "";
		String strFrom = "";
		Province province = null;

		if (from > 0 && to > 0) {
			Date fromDate = new Date(from);
			Date toDate = new Date(to);
			strTo = DateUtils.getDateByFormat(toDate, ConstantUtils.YYYYMMDD);
			strFrom = DateUtils.getDateByFormat(fromDate, ConstantUtils.YYYYMMDD);
			isIgnoreDate = false;
		}

		if (provinceId > 0) {
			province = provinceRepository.findOne(provinceId);
			isIgnoreProvince = false;
		}

		Page<PointOfInterest> pois = pointOfInterestRepository.getByCreatedAtBetweenAndProvince(isIgnoreDate, strFrom,
				strTo, isIgnoreProvince, province, pageable);

		if (!pois.getContent().isEmpty()) {
			Page<PointOfInterestDTO> dtos = pois.map(new PointOfInterestDTO.QueryMapper()::map);
			return new ResponseEntity<Page<PointOfInterestDTO>>(dtos,HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * [TOOL ADMIN] Get poi by is deleted.
	 *
	 * @param isDeleted
	 * @param pageable
	 * @return
	 */
	public ResponseEntity<Page<PointOfInterestDTO>> getPoiByIsDeleted(boolean isDeleted, Pageable pageable) {
		Page<PointOfInterest> pois = pointOfInterestRepository.findAllByIsDeleted(isDeleted, pageable);
		if (!pois.getContent().isEmpty()) {
			Page<PointOfInterestDTO> dtos = pois.map(new PointOfInterestDTO.QueryMapper()::map);
			return new ResponseEntity<Page<PointOfInterestDTO>>(dtos,HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * [TOOL ADMIN] Get latest poi created
	 *
	 * @param pageable
	 * @return List of PointOfInterestDTO
	 */
	public List<PointOfInterestDTO> getLatestPoi(Pageable pageable) {
		List<PointOfInterestDTO> dtos = new ArrayList<>();
		List<PointOfInterest> pois = pointOfInterestRepository.findAllByOrderByCreatedAtDesc(pageable);
		if (pois != null) {
			dtos = pois.stream().map(PointOfInterestDTO::from).collect(Collectors.toList());
		}
		return dtos;
	}

	/**
	 * Update sale feature
	 * 
	 * @param id
	 * @param feature
	 */
	public void updatePoiFeature(Long id, PoiFeature feature) {
		PointOfInterest entity = pointOfInterestRepository.findOne(id);
		if (entity != null) {
			switch (feature) {
			case DISABLE_SALE:
				entity.setDisableSaleFeature(true);
				break;

			case ENABLE_SALE:
				entity.setDisableSaleFeature(false);
				break;

			default:
				break;
			}
			pointOfInterestRepository.save(entity);
		}
	}

	/**
	 * Update ring index.
	 * 
	 * @param id
	 * @param ringIndex
	 */
	public void updateRingIndex(Long id, int ringIndex) {
		PointOfInterest entity = pointOfInterestRepository.findOne(id);
		if (entity != null) {
			entity.setRingIndex(ringIndex);
			pointOfInterestRepository.save(entity);
		}
	}

	/**
	 * Update Delivery Radius.
	 * 
	 * @param id
	 * @param radius
	 */
	public void updateDeliveryRadius(Long id, int radius) {
		PointOfInterest entity = pointOfInterestRepository.findOne(id);
		if (entity != null) {
			entity.setDeliveryRadius(radius);
			pointOfInterestRepository.save(entity);
		}
	}

	/**
	 * Update Discount.
	 * 
	 * @param id
	 * @param discount
	 */
	public void updatePoiDiscount(Long id, int discount) {
		PointOfInterest entity = pointOfInterestRepository.findOne(id);
		if (entity != null) {
			entity.setDiscount(discount);
			pointOfInterestRepository.save(entity);
		}
	}

}
