package vn.com.omart.backend.port.adapter.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.search.MatchQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import vn.com.omart.backend.CacheName;
import vn.com.omart.backend.application.AdminToolResourceService;
import vn.com.omart.backend.application.DashboardService;
import vn.com.omart.backend.application.HomeBannerService;
import vn.com.omart.backend.application.HomeFeatureService;
import vn.com.omart.backend.application.MobileAdminService;
import vn.com.omart.backend.application.ReportAbuseService;
import vn.com.omart.backend.application.TimelineService;
import vn.com.omart.backend.application.request.CategoryCmd;
import vn.com.omart.backend.application.response.CategoryDTO;
import vn.com.omart.backend.application.response.DashboardDTO;
import vn.com.omart.backend.application.response.HomeBannerDTO;
import vn.com.omart.backend.application.response.HomeFeatureDTO;
import vn.com.omart.backend.application.response.PointOfInterestDTO;
import vn.com.omart.backend.application.response.ReportAbuseDTO;
import vn.com.omart.backend.application.response.SearchPointOfInterestDTO;
import vn.com.omart.backend.application.util.CommonUtils;
import vn.com.omart.backend.domain.model.CodeValidationSession;
import vn.com.omart.backend.domain.model.EmptyJsonResponse;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.domain.model.PointOfInterestRepository;
import vn.com.omart.backend.domain.model.Province;
import vn.com.omart.backend.domain.model.ProvinceRepository;
import vn.com.omart.backend.port.adapter.elasticsearch.POIAdmin;
import vn.com.omart.backend.port.adapter.elasticsearch.POIAdminRepo;
import vn.com.omart.backend.port.adapter.smsgw.SmsGatewayService;
import vn.com.omart.sharedkernel.application.model.error.ApplicationException;
import vn.com.omart.sharedkernel.application.model.error.NotFoundException;
import vn.com.omart.sharedkernel.application.model.error.UnauthorizedAccessException;

@RestController
@RequestMapping("/v1/console")
@Slf4j
public class AdminToolResource {

	@Value("${code_to_update_category:198218}")
	private int codeToUpdateCategory;
	private final DashboardService dashboardService;
	private final MobileAdminService mobileAdminService;
	private final CacheManager cacheManager;
	private final SmsGatewayService smsGatewayService;

	@Autowired
	private AdminToolResourceService adminToolResourceService;

	@Autowired
	private ProvinceRepository provinceRepository;

	@Autowired
	private HomeBannerService homeBannerService;

	@Autowired
	private HomeFeatureService homeFeatureService;

	@Autowired
	private ReportAbuseService reportAbuseService;

	@Autowired
	private TimelineService timelineService;

	@Autowired
	public AdminToolResource(DashboardService dashboardService, MobileAdminService mobileAdminService,
			CacheManager cacheManager, SmsGatewayService smsGatewayService) {
		this.dashboardService = dashboardService;
		this.mobileAdminService = mobileAdminService;
		this.cacheManager = cacheManager;
		this.smsGatewayService = smsGatewayService;
	}

	// @GetMapping(value = {"/quota"})
	// public List<DashboardService.Quota> getCategories(
	// @RequestHeader(value = "X-User-Id") String userId
	// ) {
	// return this.dashboardService.getListQuota(userId);
	// }

	@GetMapping(value = { "/quota" })
	public DashboardDTO getDayQuota(@RequestHeader(value = "X-User-Id") String userId) {
		if (StringUtils.isBlank(userId)) {
			throw new UnauthorizedAccessException("Missing user-id");
		}

		return this.dashboardService.getDashboardQuota();
	}

	@PreAuthorize("hasRole(T(vn.com.omart.backend.ROLES).FUNC_CATEGORY_GET_DETAIL.role())")
	@GetMapping(value = { "/categories/{categoryId}" })
	public CategoryDTO getCategory(@RequestHeader(value = "X-User-Id") String userId,
			@PathVariable(value = "categoryId") String categoryId) {
		if (StringUtils.isBlank(userId)) {
			throw new UnauthorizedAccessException("Missing user-id");
		}

		return this.mobileAdminService.getCategory(Long.parseLong(categoryId));
	}

	@PreAuthorize("hasRole(T(vn.com.omart.backend.ROLES).FUNC_CATEGORY_UPDATE.role())")
	@PostMapping(value = { "/categories/{categoryId}" })
	public void sendVerifyCode(@RequestHeader(value = "X-User-Id") String userId,
			@PathVariable(value = "categoryId") String categoryId) {
		RedisCache cache = (RedisCache) cacheManager.getCache(CacheName.CATEGORY_VERIFY_CODE);

		String cacheKey = userId + ":" + categoryId;

		CodeValidationSession session = cache.get(cacheKey, CodeValidationSession.class);

		if (null == session) {

			Random r = new Random(System.currentTimeMillis());
			// Integer code = ((1 + r.nextInt(2)) * 100000 + r.nextInt(100000));
			Integer code = codeToUpdateCategory;
			session = new CodeValidationSession(code);

			cache.put(cacheKey, session);

			log.info("UpdateCategoryVerifyKey={}", session);

			String template = "Xac nhan cap nhat Category! Ma xac nhan la: ";
			smsGatewayService.send("0917915198", template + code);

		} else {
			log.info("Code in cache -> waiting for expiration before resend");
		}
	}

	// @PreAuthorize("hasRole(T(vn.com.omart.backend.ROLES).FUNC_CATEGORY_UPDATE.role())")
	@PutMapping(value = { "/categories/{categoryId}" })
	public CategoryDTO updateCategory(@RequestHeader(value = "X-User-Id") String userId,
			@PathVariable(value = "categoryId") String categoryId, @RequestBody @Valid CategoryCmd.Update payload) {

		if (StringUtils.isBlank(userId)) {
			throw new UnauthorizedAccessException("Missing user-id");
		}

		log.debug("payload={}", payload);

		// RedisCache cache = (RedisCache)
		// cacheManager.getCache(CacheName.CATEGORY_VERIFY_CODE);
		// String cacheKey = userId + ":" + categoryId;
		// CodeValidationSession session = cache.get(cacheKey,
		// CodeValidationSession.class);
		// if (null != session) {
		// if (Integer.parseInt(payload.getVerifyCode()) == session.getValidationCode())
		// {
		if (Integer.parseInt(payload.getVerifyCode()) == codeToUpdateCategory) {
			try {
				// cache.evict(cacheKey);

				return this.mobileAdminService.updateCategory(userId, Long.parseLong(categoryId), payload);
			} catch (NumberFormatException e) {
				throw new NotFoundException("Category not exists");
			}
		}
		// }

		throw new ApplicationException("Verify code is invalid!");
	}

	/**
	 * Get shop is deleted.
	 * 
	 * @param userId
	 * @param pageable
	 * @return List of PointOfInterestDTO
	 */
	@GetMapping(value = "/poi/deleted")
	public ResponseEntity<Page<PointOfInterestDTO>> getPoiByIsDeleted(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PageableDefault(page = 0, size = Integer.MAX_VALUE) Pageable pageable) {
		return mobileAdminService.getPoiByIsDeleted(true, pageable);
	}

	/**
	 * Get latest shop.
	 * 
	 * @param userId
	 * @param pageable
	 * @returnList of PointOfInterestDTO
	 */
	@GetMapping(value = "/poi/latest")
	public ResponseEntity<List<PointOfInterestDTO>> getLatestPoi(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PageableDefault(page = 0, size = 50) Pageable pageable) {
		List<PointOfInterestDTO> pois = mobileAdminService.getLatestPoi(pageable);
		return new ResponseEntity<List<PointOfInterestDTO>>(pois, HttpStatus.OK);
	}

	@Autowired
	private PointOfInterestRepository pointOfInterestRepository;

	@Autowired
	private POIAdminRepo poiAdminRepo;

	@GetMapping(value = { "/indexing" })
	public List<POIAdmin> indexSearchDocument() {
		poiAdminRepo.deleteAll();
		List<PointOfInterest> poiOfInterests = (List<PointOfInterest>) this.pointOfInterestRepository.findAll();
		List<POIAdmin> poiList = poiOfInterests.stream().map(POIAdmin::from).collect(Collectors.toList());
		this.poiAdminRepo.save(poiList);
		return poiList;
	}

	@GetMapping(value = { "/pois" })
	public Page<SearchPointOfInterestDTO> search13(@RequestParam(value = "text", required = true) String text,
			@RequestParam(value = "radius", required = false, defaultValue = "30") Double radius,
			@RequestHeader(value = "X-Location-Geo", required = false) String location,
			@PageableDefault(page = 0, size = Integer.MAX_VALUE) Pageable pageable) {
		Page<SearchPointOfInterestDTO> list = this.searchWithPhrasePrefix(text, "", pageable);
		return list;
	}

	public Page<SearchPointOfInterestDTO> searchWithPhrasePrefix(String text, String location, Pageable pageable) {
		// SearchQuery searchQuery;
		Page<SearchPointOfInterestDTO> list = null;

		if (StringUtils.isNumericSpace(text)) {
			String phoneText = CommonUtils.removeSpace(text);
			String wildcardValue = "*" + phoneText + "*";
			org.elasticsearch.index.query.QueryBuilder queryPhone = QueryBuilders.boolQuery()
					.should(org.elasticsearch.index.query.QueryBuilders.multiMatchQuery(text).field("phone^1"))
					.should(org.elasticsearch.index.query.QueryBuilders.wildcardQuery("phone", wildcardValue));
			Page<POIAdmin> searchPhones = poiAdminRepo.search(queryPhone, pageable);
			list = searchPhones.map(new SearchPointOfInterestDTO.QueryMapper()::map);
			if (!list.getContent().isEmpty()) {
				return list;
			}
		}

		org.elasticsearch.index.query.QueryBuilder query = QueryBuilders.boolQuery()
				.should(org.elasticsearch.index.query.QueryBuilders.multiMatchQuery(text).field("phone^7")
						.field("categoryName^6").field("categoryNameNormalize^5").field("name^4")
						.field("nameNormalize^3").field("description^2").field("descriptionNormalize^1")
						.type(MatchQuery.Type.PHRASE_PREFIX));

		// searchQuery = new NativeSearchQueryBuilder().withQuery(query).build();

		Page<POIAdmin> searchRs = poiAdminRepo.search(query, pageable);

		// fuzzy search
		if (searchRs.getContent().isEmpty()) {
			org.elasticsearch.index.query.QueryBuilder fuzzyQuery = QueryBuilders.multiMatchQuery(text).field("phone^7")
					.field("name^6").field("nameNormalize^5").field("categoryName^4").field("categoryNameNormalize^3")
					.field("description^2").field("descriptionNormalize^1").fuzziness(Fuzziness.AUTO);
			// searchQuery = new NativeSearchQueryBuilder().withQuery(fuzzyQuery).build();
			searchRs = poiAdminRepo.search(fuzzyQuery, pageable);
		}
		list = searchRs.map(new SearchPointOfInterestDTO.QueryMapper()::map);

		return list;
	}

	@GetMapping(value = "/poi/province/{province-id}")
	public List<PointOfInterestDTO> findByProvinceId(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "province-id", required = true) Long id,
			@PageableDefault(page = 0, size = Integer.MAX_VALUE) Pageable pageable) {
		List<PointOfInterestDTO> result = new ArrayList<>();
		Province province = provinceRepository.findOne(id);
		if (province != null) {
			List<PointOfInterest> entities = pointOfInterestRepository.findByProvince(province, pageable);
			result = entities.stream().map(PointOfInterestDTO::from).collect(Collectors.toList());
		}
		return result;
	}

	/**
	 * Create/update home banner
	 * 
	 * @param userId
	 * @param dto
	 * @return EmptyJsonResponse
	 */
	@PostMapping(value = "/home-banner")
	public ResponseEntity<HomeBannerDTO> createOrUpdateHomeBanner(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@Valid @RequestBody(required = true) HomeBannerDTO dto) {
		HomeBannerDTO homeBannerDTO = homeBannerService.createOrUpdateHomeBanner(userId, dto);
		return new ResponseEntity<HomeBannerDTO>(homeBannerDTO, HttpStatus.CREATED);
	}

	/**
	 * Create/update home feature
	 * 
	 * @param userId
	 * @param dto
	 * @return EmptyJsonResponse
	 */
	@PostMapping(value = "/home-feature")
	public ResponseEntity<EmptyJsonResponse> createOrUpdateHomeFeature(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@Valid @RequestBody(required = true) HomeFeatureDTO dto) {
		homeFeatureService.createOrUpdateHomeFeature(userId, dto);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.CREATED);
	}

	/**
	 * Get all home banners.
	 * 
	 * @param pageable
	 * @return List of HomeBannerDTO
	 */
	@GetMapping(value = "/home-banners")
	public ResponseEntity<List<HomeBannerDTO>> getHomeBanners(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PageableDefault(page = 0, size = 20, direction = Direction.DESC, sort = "updatedAt") Pageable pageable) {
		return new ResponseEntity<List<HomeBannerDTO>>(homeBannerService.getAll(pageable), HttpStatus.OK);
	}

	@PutMapping(value = "/home-banner/{id}/approve")
	public ResponseEntity<Void> approveHomeBanner(@PathVariable(value = "id", required = true) Long id,
			@RequestHeader(value = "X-User-Id", required = true) String userId) {
		homeBannerService.approval(id, true);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@PutMapping(value = "/home-banner/{id}/un-approve")
	public ResponseEntity<Void> unApproveHomeBanner(@PathVariable(value = "id", required = true) Long id,
			@RequestHeader(value = "X-User-Id", required = true) String userId) {
		homeBannerService.approval(id, false);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	/**
	 * Get home features.
	 * 
	 * @return List of HomeFeatureDTO
	 */
	@GetMapping(value = "/home-features")
	public ResponseEntity<List<HomeFeatureDTO>> getHomeFeatures(
			@RequestHeader(value = "X-User-Id", required = true) String userId) {
		return new ResponseEntity<List<HomeFeatureDTO>>(homeFeatureService.getAll(), HttpStatus.OK);
	}

	/**
	 * 
	 * @param poiId
	 * @param userId
	 * @return
	 */
	@GetMapping(value = "/home-banners/poiId/{poi-id}")
	public ResponseEntity<List<HomeBannerDTO>> getHomeBanneByPoiId(
			@PathVariable(value = "poi-id", required = true) Long poiId,
			@RequestHeader(value = "X-User-Id", required = true) String userId) {
		return new ResponseEntity<List<HomeBannerDTO>>(homeBannerService.getHomeBannerByPoi(poiId), HttpStatus.OK);
	}

	/**
	 * Get report abuse.
	 * 
	 * @param userId
	 * @param pageable
	 * @return List of ReportAbuseDTO
	 */
	@GetMapping(value = "/report-abuse/all")
	public ResponseEntity<List<ReportAbuseDTO>> getAll(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PageableDefault(page = 0, size = 20, direction = Direction.DESC, sort = "createdAt") Pageable pageable) {
		List<ReportAbuseDTO> dtos = reportAbuseService.getAll(pageable);
		return new ResponseEntity<List<ReportAbuseDTO>>(dtos, HttpStatus.OK);
	}

	/**
	 * Timeline report abuse
	 * 
	 * @param id
	 * @param userId
	 * @return EmptyJsonResponse
	 */
	@PutMapping(value = "/timeline/{timeline-id}/report-abuse")
	public ResponseEntity<EmptyJsonResponse> timelineReportAbuse(
			@PathVariable(value = "timeline-id", required = true) Long id,
			@RequestHeader(value = "X-User-Id", required = true) String userId) {
		timelineService.reportAbuse(id);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

}
