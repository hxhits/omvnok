package vn.com.omart.backend.port.adapter.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import vn.com.omart.backend.application.AppCommonService;
import vn.com.omart.backend.application.MobileAdminService;
import vn.com.omart.backend.application.PoiPictureService;
import vn.com.omart.backend.application.request.BannerCmd;
import vn.com.omart.backend.application.request.CategoryCmd;
import vn.com.omart.backend.application.request.ItemCmd;
import vn.com.omart.backend.application.request.PointOfInterestCmd;
import vn.com.omart.backend.application.response.CategoryDTO;
import vn.com.omart.backend.application.response.FullAddressDTO;
import vn.com.omart.backend.application.response.ItemDTO;
import vn.com.omart.backend.application.response.POIOwnerDTO;
import vn.com.omart.backend.application.response.PointOfInterestDTO;
import vn.com.omart.backend.application.response.ProvinceDTO;
import vn.com.omart.backend.application.response.UserDTO;
import vn.com.omart.backend.constants.OmartType.PoiFeature;
import vn.com.omart.backend.constants.UserTitleEnum;
import vn.com.omart.backend.domain.model.EmptyJsonResponse;
import vn.com.omart.backend.domain.model.ProvinceRepository;
import vn.com.omart.backend.port.adapter.userprofile.UserResponse;
import vn.com.omart.backend.port.adapter.userprofile.UserService;
import vn.com.omart.sharedkernel.application.model.dto.Paginated;
import vn.com.omart.sharedkernel.application.model.error.ApplicationException;
import vn.com.omart.sharedkernel.application.model.error.BusinessViolationException;
import vn.com.omart.sharedkernel.application.model.error.NotFoundException;
import vn.com.omart.sharedkernel.application.model.error.UnauthorizedAccessException;

@RestController
@RequestMapping("/v1/_admin")
@Slf4j
public class MobileAdminResource {

	@Autowired
	private PoiPictureService poiPictureService;

	private final MobileAdminService service;

	private final AppCommonService appService;

	private final ProvinceRepository provinceRepository;
	private final UserService userService;

	@Autowired
	public MobileAdminResource(MobileAdminService service, AppCommonService appService,
			ProvinceRepository provinceRepository, UserService userService) {
		this.service = service;
		this.appService = appService;
		this.provinceRepository = provinceRepository;
		this.userService = userService;
	}

	@PostMapping(value = { "/categories" }, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public CategoryDTO createCategory(@RequestHeader(value = "X-User-Id", defaultValue = "system") String username,
			@RequestBody @Valid CategoryCmd.CreateOrUpdate payload) {
		log.debug("payload={}", payload);
		return service.createCategory(payload);
	}

	// @PostMapping(value = {"/categories"},
	// consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
	// public CategoryDTO createCategoryForm(
	// @RequestHeader(value = "X-User-Id", defaultValue = "system") String username,
	// @Valid CategoryCmd.CreateOrUpdate payload
	// ) {
	// log.debug("payload={}", payload);
	// return service.createCategory(payload);
	// }

	@PutMapping(value = { "/categories/{categoryId}" })
	public CategoryDTO updateCategory(@RequestHeader(value = "X-User-Id", defaultValue = "system") String username,
			@PathVariable(value = "categoryId") String categoryId,
			@RequestBody @Valid CategoryCmd.CreateOrUpdate payload) {
		log.debug("payload={}", payload);

		try {
			return this.service.updateCategory(Long.parseLong(categoryId), payload);
		} catch (NumberFormatException e) {
			throw new NotFoundException("Category not exists");
		}
	}

	@GetMapping(value = { "/provinces" })
	public List<ProvinceDTO> getProvinces(@RequestHeader(value = "X-User-Id") String userId) {

		if (StringUtils.isBlank(userId)) {
			throw new UnauthorizedAccessException("Missing user-id");
		}

		/*
		 * Anh hung required to allway return all provinces if user in the properties
		 * config.
		 */
		boolean isByPass = service.isByPassUserProvince(userId);
		if (isByPass) {
			return appService.getProvinces();
		}
		return this.provinceRepository.findAllByUser(userId, 0L).stream().map(ProvinceDTO::from)
				.collect(Collectors.toList());

		// return this.appService.getProvinces();
	}

	// @PreAuthorize("hasRole(T(vn.com.omart.backend.ROLES).ROLE_SALES_GET_POI.name())")
	@GetMapping(value = { "/pois" })
	public List<PointOfInterestDTO> getPOIByCreator(
			@RequestHeader(value = "X-User-Id", required = false) String username) {
		if (StringUtils.isBlank(username)) {
			throw new UnauthorizedAccessException("Missing user-id");
		}

		return this.service.getPOIByCreator(username);
	}

	@GetMapping(value = { "/pois/{poiId}" })
	public PointOfInterestDTO getPOI(@RequestHeader(value = "X-User-Id", required = false) String username,
			@PathVariable(value = "poiId") String poiId) {
		if (StringUtils.isBlank(username)) {
			throw new UnauthorizedAccessException("Missing user-id");
		}

		try {
			long id = Long.parseLong(poiId);
			return this.service.getPOI(id, username);
		} catch (NumberFormatException e) {
			throw new NotFoundException("PointOfInterest not exists");
		}

	}

	@PostMapping(value = { "/pois" })
	public PointOfInterestDTO createPOI(@RequestHeader(value = "X-User-Id", required = false) String username,
			@RequestHeader(value = "X-Location-Geo") String location,
			@RequestBody @Valid PointOfInterestCmd.CreateOrUpdate payload) {

		if (StringUtils.isBlank(username)) {
			throw new UnauthorizedAccessException("Missing user-id");
		}

		UserResponse user = this.userService.getOwner(username);
		String title = user.getTitle();

		if (!title.equalsIgnoreCase(UserTitleEnum.SALESMAN.name())
				&& !title.equalsIgnoreCase(UserTitleEnum.SUPERVISOR.name())
				&& !title.equalsIgnoreCase(UserTitleEnum.OWNER.name())) {
			throw new AccessDeniedException("You have no permission");
		}

		if (null == location) {
			throw new AccessDeniedException("You have to enable GPS");
		}

		log.info("X-Location-Geo={}", location);
		log.debug("payload={}", payload);

		try {
			return this.service.createPointOfInterest(username, location, payload);
		} catch (NumberFormatException e) {
			throw new NotFoundException("Category not exists");
		}
	}

	@PutMapping(value = { "/pois/{poiId}" })
	public PointOfInterestDTO updatePOI(@RequestHeader(value = "X-User-Id", required = false) String username,
			@PathVariable(value = "poiId") String poiId,
			@RequestBody @Valid PointOfInterestCmd.CreateOrUpdate payload) {
		if (StringUtils.isBlank(username)) {
			throw new UnauthorizedAccessException("Missing user-id");
		}
		log.debug("payload={}", payload);

		UserResponse owner = this.userService.getOwner(username);

		if (owner == null) {
			throw new NotFoundException("User " + username + " not found");
		}

		boolean isOwner = UserTitleEnum.OWNER.name().equalsIgnoreCase(owner.getTitle());
		boolean isSupervisor = UserTitleEnum.SUPERVISOR.name().equalsIgnoreCase(owner.getTitle());
		boolean isDirector = UserTitleEnum.DIRECTOR.name().equalsIgnoreCase(owner.getTitle());

		boolean canUpdate = isOwner || isSupervisor || isDirector;

		if (!canUpdate) {
			throw new BusinessViolationException("Only owner or director can update.");
		}

		try {
			return this.service.updatePointOfInterest(Long.parseLong(poiId), username, payload);
		} catch (NumberFormatException e) {
			throw new NotFoundException("PointOfInterest not exists");
		}

		// try {
		// log.info("Disable function update of Salesman");
		//// return this.service.updatePointOfInterest(Long.parseLong(poiId), username,
		// payload);
		// return this.service.getPOI(Long.parseLong(poiId));
		// } catch (NumberFormatException e) {
		// throw new NotFoundException("PointOfInterest not exists");
		// }
	}

	@GetMapping(value = { "/pois/{poiId}/items" })
	public List<ItemDTO> getItems(@RequestHeader(value = "X-User-Id", required = false) String username,
			@PathVariable(value = "poiId") String poiId) {
		if (StringUtils.isBlank(username)) {
			throw new UnauthorizedAccessException("Missing user-id");
		}
		try {
			return this.service.getItemsByPOI(Long.parseLong(poiId));
		} catch (NumberFormatException e) {
			throw new ApplicationException("PointId is Incorrect");
		}
	}

	@GetMapping(value = { "/pois/{poiId}/items/{itemId}" })
	public ItemDTO getItem(@RequestHeader(value = "X-User-Id", required = false) String username,
			@PathVariable(value = "poiId") String poiId, @PathVariable(value = "itemId") String itemId) {
		if (StringUtils.isBlank(username)) {
			throw new UnauthorizedAccessException("Missing user-id");
		}

		try {
			return this.service.getItemByPOI(Long.parseLong(poiId), Long.parseLong(itemId));
		} catch (NumberFormatException e) {
			throw new NotFoundException("Item not exists");
		}
	}

//	@PostMapping(value = { "/pois/{poiId}/items" })
//	public ItemDTO createItem(@RequestHeader(value = "X-User-Id", required = false) String username,
//			@PathVariable(value = "poiId") String poiId, @RequestBody @Valid ItemCmd.CreateOrUpdate payload) {
//		if (StringUtils.isBlank(username)) {
//			throw new UnauthorizedAccessException("Missing user-id");
//		}
//		log.debug("payload={}", payload);
//
//		try {
//			return this.service.createItem(Long.parseLong(poiId), payload);
//		} catch (NumberFormatException e) {
//			throw new NotFoundException("PointOfInterest not exists");
//		}
//	}

	@PostMapping(value = { "/pois/{poiId}/items" })
	public ItemDTO createItem(@RequestHeader(value = "X-User-Id", required = false) String username,
			@PathVariable(value = "poiId") String poiId, @RequestBody @Valid ItemCmd.CreateOrUpdate payload) {
		if (StringUtils.isBlank(username)) {
			throw new UnauthorizedAccessException("Missing user-id");
		}
		log.debug("payload={}", payload);

		try {
			return this.service.createItem(Long.parseLong(poiId), payload);
		} catch (NumberFormatException e) {
			throw new NotFoundException("PointOfInterest not exists");
		}
	}

	@PutMapping(value = { "/pois/{poiId}/items/{itemId}" })
	public ItemDTO updateItem(@RequestHeader(value = "X-User-Id", required = false) String username,
			// @PathVariable(value = "poiId") String poiId,
			@PathVariable(value = "itemId") String itemId, @RequestBody @Valid ItemCmd.CreateOrUpdate payload) {
		if (StringUtils.isBlank(username)) {
			throw new UnauthorizedAccessException("Missing user-id");
		}
		log.debug("payload={}", payload);

		try {
			return this.service.updateItem(Long.parseLong(itemId), payload);
		} catch (NumberFormatException e) {
			throw new NotFoundException("Item not exists");
		}
	}

	@GetMapping(value = { "/owners" })
	public List<POIOwnerDTO> getOwnersByCreator(@RequestHeader(value = "X-User-Id", required = false) String username) {
		if (StringUtils.isBlank(username)) {
			throw new UnauthorizedAccessException("Missing user-id");
		}

		return this.service.getAllOwnerByCreator(username);
	}

	@GetMapping(value = { "/banners/pois" })
	public List<PointOfInterestDTO> getPOIsHavingBanner(
			@RequestHeader(value = "X-User-Id", required = false) String userId) {
		return this.service.getPOIsHavingBanner(userId);
	}

	@GetMapping(value = "/banners/pois/paging")
	public Paginated<PointOfInterestDTO> getListGameMaster(
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int page,
			@RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize) {
		Page<PointOfInterestDTO> result = this.service.getPOIsHavingBanner(page, pageSize);
		return new Paginated<>(result.getContent(), page, pageSize, result.getTotalElements());
	}

	@GetMapping(value = { "/banners/pois/search" })
	public List<PointOfInterestDTO> searchPOIsHavingBanner(@RequestParam(value = "name") String poiName,
			@RequestHeader(value = "X-User-Id", required = false) String userId) {
		return this.service.searchPOIsHavingBanner(poiName, userId);
	}

	@GetMapping(value = { "/banners/pois/{id}" })
	public PointOfInterestDTO getPOIHavingBanner(@PathVariable(value = "id") String id,
			@RequestHeader(value = "X-User-Id", required = false) String userId) {
		try {
			return this.service.getPOIHavingBanner(Long.parseLong(id), userId);
		} catch (NumberFormatException e) {
			throw new NotFoundException("POI not exists");
		}
	}

	@PutMapping(value = { "/banners/pois/{poiId}" })
	public PointOfInterestDTO updateBanner(@PathVariable(value = "poiId") String poiId,
			@RequestHeader(value = "X-User-Id", required = false) String userId,
			@RequestBody @Valid BannerCmd.CreateOrUpdate payload) {

		UserResponse user = this.userService.getOwner(userId);

		if (!user.getTitle().equalsIgnoreCase(UserTitleEnum.SALESMAN.name())
				&& !user.getTitle().equalsIgnoreCase(UserTitleEnum.SUPERVISOR.name())) {
			throw new AccessDeniedException("You have no permission");
		}

		try {
			return this.service.updateBanner(Long.parseLong(poiId), userId, payload);
		} catch (NumberFormatException e) {
			throw new NotFoundException("POI not exists");
		}
	}

	@PostMapping(value = { "/banners/pois/{poiId}/{operation}" })
	public PointOfInterestDTO approveBanner(@PathVariable(value = "poiId") String poiId,
			@PathVariable(value = "operation") String action,
			@RequestHeader(value = "X-User-Id", required = false) String userId) {

		UserResponse user = this.userService.getOwner(userId);

		if (!user.getTitle().equalsIgnoreCase(UserTitleEnum.SUPERVISOR.name())) {
			throw new AccessDeniedException("You have no permission");
		}

		if (action.equalsIgnoreCase("approve")) {
			return this.service.approveBanner(Long.parseLong(poiId), userId, true);
		}

		if (action.equalsIgnoreCase("unapprove")) {
			return this.service.approveBanner(Long.parseLong(poiId), userId, false);
		}

		throw new ApplicationException("Operation Unsupport, just approve/unapprove");
	}

	/**
	 * Populate User Address.
	 * 
	 * @param latitude
	 * @param longitude
	 * @return FullAddressDTO
	 */
	@RequestMapping(value = "/address", method = RequestMethod.GET)
	public ResponseEntity<FullAddressDTO> populateUserAddress(
			@RequestParam(value = "latitude", required = true) Double latitude,
			@RequestParam(value = "longitude", required = true) Double longitude) {
		String latlng = String.valueOf(latitude) + "," + String.valueOf(longitude);
		FullAddressDTO resu = service.getFullAddress(latlng);
		return new ResponseEntity<FullAddressDTO>(resu, HttpStatus.OK);
	}

	/**
	 * Approve / reject by omart admin.
	 * 
	 * @param userId
	 * @param id
	 * @param action
	 * @return HttpStatus
	 */
	@RequestMapping(value = "/pois/{id}/approval", method = RequestMethod.PUT)
	public ResponseEntity<Void> isApproved(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "id", required = true) Long id,
			@RequestParam(value = "action", required = true) String action) {
		service.approval(id, action);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	/**
	 * Transfer shop.
	 * 
	 * @param userId
	 * @param id
	 * @param phone
	 * @return UserDTO
	 */
	@RequestMapping(value = "/pois/{id}/{phone}/transfer", method = RequestMethod.POST)
	public ResponseEntity<UserDTO> poiAdminTransfered(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "id", required = true) Long id,
			@PathVariable(value = "phone", required = true) String phone) {
		UserDTO dto = service.poiAdminTransfered(id, phone, userId);
		return new ResponseEntity<UserDTO>(dto, HttpStatus.OK);
	}

	/**
	 * Filter shop between date
	 * 
	 * @param userId
	 * @param from
	 * @param to
	 * @param pageable
	 * @return List of PointOfInterestDTO
	 */
	@RequestMapping(value = "/pois/createdAt", method = RequestMethod.GET)
	public ResponseEntity<List<PointOfInterestDTO>> getShopBetweenDate(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestParam(value = "from", required = true) Long from,
			@RequestParam(value = "to", required = true) Long to,
			@PageableDefault(page = 0, size = Integer.MAX_VALUE) Pageable pageable) {
		List<PointOfInterestDTO> dtos = service.getShopBetweenDate(from, to, pageable);
		return new ResponseEntity<List<PointOfInterestDTO>>(dtos, HttpStatus.OK);
	}

	/**
	 * Filter shop between date and province.
	 * 
	 * @param userId
	 * @param from
	 * @param to
	 * @param provinceId
	 * @param pageable
	 * @return List of PointOfInterestDTO
	 */
	@RequestMapping(value = "/pois/createAtAndProvince", method = RequestMethod.GET)
	public ResponseEntity<Page<PointOfInterestDTO>> getShopBetweenDateAndProvince(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestParam(value = "from", required = false, defaultValue = "0") Long from,
			@RequestParam(value = "to", required = false, defaultValue = "0") Long to,
			@RequestParam(value = "provinceId", required = false, defaultValue = "0") Long provinceId,
			@PageableDefault(page = 0, size = Integer.MAX_VALUE) Pageable pageable) {
		return service.getShopBetweenDateAndProvince(from, to, provinceId, pageable);
	}

	/**
	 * Update poi feature.
	 * 
	 * @param userId
	 * @param id
	 * @param feature
	 * @return EmptyJsonResponse
	 */
	@PutMapping(value = "/pois/{id}/feature")
	public ResponseEntity<EmptyJsonResponse> onOffFeature(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(required = true, value = "id") Long id,
			@RequestParam(required = true, value = "action") PoiFeature feature) {
		service.updatePoiFeature(id, feature);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Update ring index.
	 * 
	 * @param userId
	 * @param id
	 * @param ringIndex
	 * @return EmptyJsonResponse
	 */
	@PutMapping(value = "/pois/{id}/ringIndex/{ring-index}")
	public ResponseEntity<EmptyJsonResponse> updateRingIndex(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(required = true, value = "id") Long id,
			@PathVariable(required = true, value = "ring-index") int ringIndex) {
		service.updateRingIndex(id, ringIndex);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Update delivery radius
	 * 
	 * @param userId
	 * @param id
	 * @param radius
	 * @return EmptyJsonResponse
	 */
	@PutMapping(value = "/pois/{id}/deliveryRadius/{radius}")
	public ResponseEntity<EmptyJsonResponse> updateDeliveryRadius(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(required = true, value = "id") Long id,
			@PathVariable(required = true, value = "radius") int radius) {
		service.updateDeliveryRadius(id, radius);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Update Discount
	 * 
	 * @param userId
	 * @param id
	 * @param discount
	 * @return EmptyJsonResponse
	 */
	@PutMapping(value = "/pois/{id}/commission/{discount}")
	public ResponseEntity<EmptyJsonResponse> updatePoiDiscount(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(required = true, value = "id") Long id,
			@PathVariable(required = true, value = "discount") int discount) {
		service.updatePoiDiscount(id, discount);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

}
