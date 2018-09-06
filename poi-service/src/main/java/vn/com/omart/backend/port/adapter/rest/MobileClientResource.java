package vn.com.omart.backend.port.adapter.rest;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.search.MatchQuery;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import vn.com.omart.backend.application.HomeBannerService;
import vn.com.omart.backend.application.HomeFeatureService;
import vn.com.omart.backend.application.MobileAdminService;
import vn.com.omart.backend.application.MobileClientService;
import vn.com.omart.backend.application.PoiPictureService;
import vn.com.omart.backend.application.StoredProcedureService;
import vn.com.omart.backend.application.request.POIOwnerCmd;
import vn.com.omart.backend.application.request.PointOfInterestCmd;
import vn.com.omart.backend.application.response.AdsBannerDTO;
import vn.com.omart.backend.application.response.CategoryDTO;
import vn.com.omart.backend.application.response.CategoryParentDTO;
import vn.com.omart.backend.application.response.FullAddressDTO;
import vn.com.omart.backend.application.response.HomeBannerDTO;
import vn.com.omart.backend.application.response.HomeFeatureDTO;
import vn.com.omart.backend.application.response.ImageDTO;
import vn.com.omart.backend.application.response.ItemDTO;
import vn.com.omart.backend.application.response.POIOwnerDTO;
import vn.com.omart.backend.application.response.PoiActionDTO;
import vn.com.omart.backend.application.response.PoiCommentDTO;
import vn.com.omart.backend.application.response.PoiDTO;
import vn.com.omart.backend.application.response.PoiPictureDTO;
import vn.com.omart.backend.application.response.PoiTimelineDTO;
import vn.com.omart.backend.application.response.PointOfInterestDTO;
import vn.com.omart.backend.application.response.ProvinceDTO;
import vn.com.omart.backend.application.response.SearchPointOfInterestDTO;
import vn.com.omart.backend.application.response.StoreProcedureParams;
import vn.com.omart.backend.constants.Device;
import vn.com.omart.backend.constants.UserTitleEnum;
import vn.com.omart.backend.domain.model.AdsBanner;
import vn.com.omart.backend.domain.model.AdsBannerRepository;
import vn.com.omart.backend.domain.model.Category;
import vn.com.omart.backend.domain.model.CategoryRepository;
import vn.com.omart.backend.domain.model.EmptyJsonResponse;
import vn.com.omart.backend.domain.model.Image;
import vn.com.omart.backend.domain.model.PoiComment;
import vn.com.omart.backend.domain.model.PoiPicture;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.domain.model.PointOfInterestRepository;
import vn.com.omart.backend.domain.model.RecruitmentPosition;
import vn.com.omart.backend.domain.model.RecruitmentPositionRepository;
import vn.com.omart.backend.domain.model.RecruitmentRepository;
import vn.com.omart.backend.port.adapter.elasticsearch.POI;
import vn.com.omart.backend.port.adapter.elasticsearch.POIRepo;
import vn.com.omart.backend.port.adapter.thumbor.ThumborService;
import vn.com.omart.backend.port.adapter.userprofile.UserResponse;
import vn.com.omart.backend.port.adapter.userprofile.UserService;
import vn.com.omart.sharedkernel.application.model.error.AlreadyExistingException;
import vn.com.omart.sharedkernel.application.model.error.ApplicationException;
import vn.com.omart.sharedkernel.application.model.error.NotFoundException;
import vn.com.omart.sharedkernel.application.model.error.UnauthorizedAccessException;

@RestController
@RequestMapping(value = { "/v1" })
@Slf4j
public class MobileClientResource {

	@Autowired
	private HomeFeatureService homeFeatureService;

	@Autowired
	private HomeBannerService homeBannerService;

	@Autowired
	private RecruitmentRepository recruitmentRepository;

	@Value("${elastic_search_max_results:100}")
	private int elasticSearchMaxResults;

	private final MobileClientService service;
	private final MobileAdminService mobileAdminService;
	private final AdsBannerRepository adsBannerRepository;
	private final CategoryRepository categoryRepository;
	private final RecruitmentPositionRepository positionRepository;
	private final PointOfInterestRepository pointOfInterestRepository;
	private final POIRepo poiRepo;

	@Autowired
	private ThumborService thumborService;

	@Autowired
	private PoiPictureService poiPictureService;

	@Autowired
	private UserService userService;

	@Autowired
	private StoredProcedureService storedProcedureService;

	@Autowired
	public MobileClientResource(MobileClientService service, MobileAdminService mobileAdminService,
			AdsBannerRepository adsBannerRepository, CategoryRepository categoryRepository,
			PointOfInterestRepository pointOfInterestRepository, POIRepo poiRepo,
			RecruitmentPositionRepository positionRepository) {
		this.service = service;
		this.mobileAdminService = mobileAdminService;
		this.adsBannerRepository = adsBannerRepository;
		this.categoryRepository = categoryRepository;
		this.pointOfInterestRepository = pointOfInterestRepository;
		this.poiRepo = poiRepo;
		this.positionRepository = positionRepository;
	}

	/**
	 * Temporary disable function
	 * 
	 * @return
	 */
	/*
	 * @GetMapping(value = { "/categories" }) public List<CategoryDTO>
	 * getCategories() { return this.service.getCategories(); }
	 */

	@Deprecated
	// @GetMapping(value = { "/categories/{categoryId}/pois" })
	public List<PointOfInterestDTO> findPOINearBy_Bending(@PathVariable(value = "categoryId") String categoryId,

			@RequestHeader(value = "X-Location-Geo", required = false) String location,
			@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude,

			@RequestParam(value = "radius", required = false, defaultValue = "2000") Double radius,
			@RequestParam(value = "distance", required = false, defaultValue = "0") Double distance,

			@RequestParam(value = "lastId", required = false, defaultValue = "0") long lastId,
			@RequestParam(value = "limit", required = false, defaultValue = "30") long limit,

			@RequestParam(value = "province", required = false, defaultValue = "all") String province,
			@RequestParam(value = "district", required = false, defaultValue = "all") String district,
			@RequestParam(value = "ward", required = false, defaultValue = "all") String ward) {

		// Temporarily hard code radius
		radius = 2000d;
		Double lat = latitude;
		Double lon = longitude;

		if (null != location) {
			lat = Double.valueOf(location.split(",")[0]);
			lon = Double.valueOf(location.split(",")[1]);
		}

		if (null == lat) {
			// filter recruitment.
			List<PointOfInterestDTO> a = this.service.getListPOI(Long.valueOf(categoryId), limit, lastId, province,
					district, ward);
			List<PointOfInterestDTO> b = null;
			List<RecruitmentPosition> cts = positionRepository.findAll();
			for (RecruitmentPosition ct : cts) {
				if (ct.getId() == Long.valueOf(categoryId)) {
					List<Long> recruits = recruitmentRepository.findDistinctPoiByPositionAndIsDeletedAndState(ct, false,
							0);
					if (!recruits.isEmpty()) {
						int n = recruits.size();
						Long[] poiIds = new Long[n];
						for (int i = 0; i < n; i++) {
							poiIds[i] = Long.valueOf(String.valueOf(recruits.get(i)));
						}
						// PageRequest pageRequest = new PageRequest(0, 100);
						b = this.service.getListPOIByRecruit(poiIds);
					} else {
						return new ArrayList<PointOfInterestDTO>();
					}
				}
			}
			List<PointOfInterestDTO> c = Stream.concat(a.stream(), b.stream()).collect(Collectors.toList());
			List<PointOfInterestDTO> d = c.stream().distinct().collect(Collectors.toList());
			return d;
		}

		// filter recruitment.
		List<RecruitmentPosition> cts = positionRepository.findAll();
		List<PointOfInterestDTO> a1 = null;
		List<PointOfInterestDTO> b1 = this.service.getNearestPOIs(Long.valueOf(categoryId), lat, lon, radius, distance,
				province, district, ward);
		for (RecruitmentPosition ct : cts) {
			if (ct.getId() == Long.valueOf(categoryId)) {
				List<Long> recruits = recruitmentRepository.findDistinctPoiByPositionAndIsDeletedAndState(ct, false, 0);
				if (!recruits.isEmpty()) {
					int n = recruits.size();
					Long[] poiIds = new Long[n];
					String ids = "";
					for (int i = 0; i < n; i++) {
						poiIds[i] = Long.valueOf(String.valueOf(recruits.get(i)));// recruits.get(i).getPoi().id();
						ids += "," + String.valueOf(recruits.get(i));
					}
					ids = ids.replaceFirst(",", "");
					a1 = this.service.findNearestByPoiId(ids, lat, lon, radius, distance, province, district, ward);
				} else {
					return b1;
				}
			}
		}
		List<PointOfInterestDTO> c1 = Stream.concat(a1.stream(), b1.stream()).collect(Collectors.toList());
		List<PointOfInterestDTO> d1 = c1.stream().distinct().collect(Collectors.toList());
		Collections.sort(d1, new Comparator<PointOfInterestDTO>() {
			@Override
			public int compare(PointOfInterestDTO o1, PointOfInterestDTO o2) {
				// TODO Auto-generated method stub
				return o1.getDistance().compareTo(o2.getDistance());
			}
		});
		return d1;
	}

	/**
	 * Get PoiId By Category Id.
	 * 
	 * @param categoryId
	 * @return Array Long
	 */
	private Long[] getPoiIdByCategoryId(Long categoryId) {
		List<RecruitmentPosition> cts = positionRepository.findAll();
		for (RecruitmentPosition ct : cts) {
			if (ct.getId() == categoryId) {
				List<Long> recruits = recruitmentRepository.findDistinctPoiByPositionAndIsDeletedAndState(ct, false, 0);
				if (!recruits.isEmpty()) {
					int n = recruits.size();
					Long[] poiIds = new Long[n];
					for (int i = 0; i < n; i++) {
						poiIds[i] = Long.valueOf(String.valueOf(recruits.get(i)));
					}
					return poiIds;
				} else {
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * Get PoiIdStr By Category Id.
	 * 
	 * @param categoryId
	 * @return String
	 */
	private String getPoiIdStrByCategoryId(Long categoryId) {
		List<RecruitmentPosition> cts = positionRepository.findAll();
		String poiIdStr = "";
		for (RecruitmentPosition ct : cts) {
			if (ct.getId() == categoryId) {
				List<Long> recruits = recruitmentRepository.findDistinctPoiByPositionAndIsDeletedAndState(ct, false, 0);
				if (!recruits.isEmpty()) {
					int n = recruits.size();
					for (int i = 0; i < n; i++) {
						poiIdStr += "," + Long.valueOf(String.valueOf(recruits.get(i)));
					}
					return poiIdStr.replaceFirst(",", "");
				} else {
					return null;
				}
			}
		}
		return null;
	}

	@GetMapping(value = { "/categories/{categoryId}/pois" })
	public List<PointOfInterestDTO> findPOINearBy(@PathVariable(value = "categoryId") String categoryId,

			@RequestHeader(value = "X-Location-Geo", required = false) String location,
			@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude,

			@RequestParam(value = "radius", required = false, defaultValue = "2000") Double radius,
			@RequestParam(value = "distance", required = false, defaultValue = "0") Double distance,

			@RequestParam(value = "lastId", required = false, defaultValue = "0") long lastId,
			@RequestParam(value = "limit", required = false, defaultValue = "30") long limit,

			@RequestParam(value = "province", required = false, defaultValue = "all") String province,
			@RequestParam(value = "district", required = false, defaultValue = "all") String district,
			@RequestParam(value = "ward", required = false, defaultValue = "all") String ward) {

		// Temporarily hard code radius
		radius = 2000d;
		Double lat = latitude;
		Double lon = longitude;

		if (null != location) {
			lat = Double.valueOf(location.split(",")[0]);
			lon = Double.valueOf(location.split(",")[1]);
		}
		// do not have location.
		if (null == lat) {
			List<PointOfInterestDTO> a = this.service.getListPOI(Long.valueOf(categoryId), limit, lastId, province,
					district, ward);
			// filter recruitment.
			Long[] poiIds = this.getPoiIdByCategoryId(Long.valueOf(categoryId));
			if (poiIds != null) {
				List<PointOfInterestDTO> b = this.service.getListPOIByRecruit(poiIds);
				List<PointOfInterestDTO> c = Stream.concat(a.stream(), b.stream()).collect(Collectors.toList());
				List<PointOfInterestDTO> d = c.stream().distinct().collect(Collectors.toList());
				return d;
			}
			return a;
		}

		List<PointOfInterestDTO> b1 = this.service.getNearestPOIs(Long.valueOf(categoryId), lat, lon, radius, distance,
				province, district, ward);
		// filter recruitment.
		String ids = this.getPoiIdStrByCategoryId(Long.valueOf(categoryId));
		if (ids != null) {
			List<PointOfInterestDTO> a1 = this.service.findNearestByPoiId(ids, lat, lon, radius, distance, province,
					district, ward);
			List<PointOfInterestDTO> c1 = Stream.concat(a1.stream(), b1.stream()).collect(Collectors.toList());
			List<PointOfInterestDTO> d1 = c1.stream().distinct().collect(Collectors.toList());
			Collections.sort(d1, new Comparator<PointOfInterestDTO>() {
				@Override
				public int compare(PointOfInterestDTO o1, PointOfInterestDTO o2) {
					// TODO Auto-generated method stub
					return o1.getDistance().compareTo(o2.getDistance());
				}
			});
			return d1;
		}
		return b1;
	}

	/**
	 * Get pois by category id.
	 * 
	 * @param location
	 * @param params
	 * @return List of PointOfInterestDTO
	 */
	@PostMapping(value = { "/category/pois" })
	public List<PointOfInterestDTO> getPoiByCategoryId(
			@RequestHeader(value = "X-Location-Geo", required = false) String location,
			@RequestBody(required = true) StoreProcedureParams params) {
		if (StringUtils.isNotBlank(location)) {
			params.setLatitude(Double.valueOf(location.split(",")[0]));
			params.setLongitude(Double.valueOf(location.split(",")[1]));
		} else {
			params.setLatitude(0d);
			params.setLongitude(0d);
		}
		List<Object[]> objList = storedProcedureService.getFilterPoiByCategory("filter_poi_by_category", params);
		List<PointOfInterestDTO> dtos = objList.stream().map(service::objectsToDTO).collect(Collectors.toList());
		return dtos;
	}

	/**
	 * Get poi banner approved.
	 * 
	 * @param categoryId
	 * @return List of PointOfInterestDTO
	 */
	@GetMapping(value = { "category/{category-id}/pois/banner-approved" })
	public List<PointOfInterestDTO> getPoiBannerApproved(
			@PathVariable(required = true, value = "category-id") Long categoryId) {
		List<PointOfInterestDTO> dtos = service.getPoiBannerApproved(categoryId);
		return dtos;
	}

	@GetMapping(value = { "/categories/{categoryId}/pois/{poiId}" })
	public PointOfInterestDTO getPointOfInterest(@PathVariable(value = "categoryId") String categoryId,
			@PathVariable(value = "poiId") String poiId) {
		try {
			return this.service.getPointOfInterest(Long.parseLong(poiId), Long.parseLong(categoryId));
		} catch (NumberFormatException e) {
			throw new NotFoundException("PointOfInterest not exists");
		}
	}

	@GetMapping(value = { "/categories/{categoryId}/pois/{poiId}/items" })
	public List getItems(@PathVariable(value = "categoryId") String categoryId,
			@PathVariable(value = "poiId") String poiId) {
		try {
			return this.service.getPointOfInterestItems(Long.parseLong(poiId), Long.parseLong(categoryId));
		} catch (Exception e) {
			log.error("service get item of poiId={} error={}", poiId, e);
			return new ArrayList();
		}
	}

	@GetMapping(value = { "/categories/{categoryId}/pois/{poiId}/items/{itemId}" })
	public ItemDTO getItem(@PathVariable(value = "categoryId") String categoryId,
			@PathVariable(value = "poiId") String poiId, @PathVariable(value = "itemId") String itemId) {
		try {
			return this.service.getPointOfInterestItem(Long.parseLong(itemId), Long.parseLong(poiId),
					Long.parseLong(categoryId));
		} catch (NumberFormatException e) {
			throw new NotFoundException("Item not exists");
		}
	}

	// @PreAuthorize("hasRole('ROLE_FUNC_GET_POIS_OF_OWNER')")
	@GetMapping(value = { "/me/pois" })
	public List<PointOfInterestDTO> getPOIsByOwer(
			@RequestHeader(value = "X-User-Id", required = false) String ownerId) {

		if (StringUtils.isBlank(ownerId)) {
			throw new UnauthorizedAccessException("Missing user-id");
		}
		return service.getPOIByOwner(ownerId);
	}

	@PutMapping(value = { "/me/pois/{poiId}" })
	public PointOfInterestDTO updatePOI(@RequestHeader(value = "X-User-Id", required = false) String ownerId,
			@PathVariable(value = "poiId") String poiId,
			@RequestBody @Valid PointOfInterestCmd.CreateOrUpdate payload) {
		if (StringUtils.isBlank(ownerId)) {
			throw new UnauthorizedAccessException("Missing user-id");
		}
		log.debug("payload={}", payload);

		payload.setOwnerId(ownerId);

		try {
			return this.mobileAdminService.updatePointOfInterest(Long.parseLong(poiId), ownerId, payload);
		} catch (NumberFormatException e) {
			throw new NotFoundException("PointOfInterest not exists");
		}
	}

	@GetMapping(value = { "/me" })
	public POIOwnerDTO getProfile(@RequestHeader(value = "X-User-Id", required = false) String ownerId) {
		if (StringUtils.isBlank(ownerId)) {
			throw new UnauthorizedAccessException("Missing user-id");
		}

		return mobileAdminService.getOwner(ownerId);
	}

	@PutMapping(value = { "/me/password" })
	@ResponseStatus(value = HttpStatus.OK)
	public void changePassword(@RequestHeader(value = "X-User-Id", required = false) String ownerId,
			@RequestBody @Valid POIOwnerCmd.ChangePassword payload) {
		if (StringUtils.isBlank(ownerId)) {
			throw new UnauthorizedAccessException("Missing user-id");
		}
		mobileAdminService.changePassword(ownerId, payload);
	}

	@GetMapping(value = { "/ads/banner" })
	public List<AdsBannerDTO> getAdsBanner(
			@RequestParam(value = "categoryId", required = false, defaultValue = "15") String categoryId) {

		Category one = this.categoryRepository.findOne(Long.parseLong(categoryId));

		List<AdsBanner> all;
		if (one != null) {
			all = this.adsBannerRepository.findAllByCategory(one);
		} else {
			all = this.adsBannerRepository.findAll();
		}

		if (all.size() > 3) {
			all = all.subList(0, 3);
		}
		return all.stream().map(AdsBannerDTO::from).collect(Collectors.toList());
	}

	@GetMapping(value = { "/indexing" })
	public List<POI> indexSearchDocument() {

		// poiRepo.deleteAll();
		//
		// Spliterator<PointOfInterest> poiOfInterests =
		// this.pointOfInterestRepository.findAll().spliterator();
		//
		// List<POI> poiList = StreamSupport.stream(poiOfInterests,
		// false).map(POI::from).collect(Collectors.toList());
		//
		// this.poiRepo.save(poiList);
		//
		// return poiList;

		poiRepo.deleteAll();

		List<PointOfInterest> poiOfInterests = this.pointOfInterestRepository.findAllByIsApprovedAndIsDeleted(true,
				false);

		List<POI> poiList = poiOfInterests.stream().map(POI::from).collect(Collectors.toList());

		this.poiRepo.save(poiList);

		return poiList;
	}

	/**
	 * Search.
	 *
	 * @param text
	 * @param location
	 * @param pageable
	 * @return List of SearchPointOfInterestDTO
	 */
	public List<SearchPointOfInterestDTO> search(String text, String location, Pageable pageable) {

		SearchQuery searchQuery;
		String wildcardValue = text + "*";
		org.elasticsearch.index.query.QueryBuilder query = QueryBuilders.boolQuery()
				.should(org.elasticsearch.index.query.QueryBuilders.multiMatchQuery(text).field("name^6")
						.field("nameNormalize^5").field("categoryName^4").field("categoryNameNormalize^3")
						.field("description^2").field("descriptionNormalize^1"))
				.should(org.elasticsearch.index.query.QueryBuilders.wildcardQuery("name", wildcardValue));

		searchQuery = new NativeSearchQueryBuilder().withQuery(query).withPageable(pageable).build();
		String uu = searchQuery.getQuery().toString();
		Page<POI> searchRs = poiRepo.search(searchQuery);

		// fuzzy search
		if (searchRs.getContent().isEmpty()) {
			org.elasticsearch.index.query.QueryBuilder fuzzyQuery = QueryBuilders.multiMatchQuery(text).field("name^6")
					.field("nameNormalize^5").field("categoryName^4").field("categoryNameNormalize^3")
					.field("description^2").field("descriptionNormalize^1").fuzziness(Fuzziness.AUTO);
			searchQuery = new NativeSearchQueryBuilder().withQuery(fuzzyQuery).withPageable(pageable).build();
			String uu1 = searchQuery.getQuery().toString();
			searchRs = poiRepo.search(searchQuery);
		}

		List<SearchPointOfInterestDTO> list = StreamSupport.stream(searchRs.spliterator(), false)
				.map(poi -> SearchPointOfInterestDTO.from(poi, location)).collect(Collectors.toList());

		return list;
	}

	public List<SearchPointOfInterestDTO> searchWithPhrasePrefix(String text, String location, Pageable pageable) {

		SearchQuery searchQuery;
		String wildcardValue = text + "*";
		org.elasticsearch.index.query.QueryBuilder query = QueryBuilders.boolQuery()
				.should(org.elasticsearch.index.query.QueryBuilders.multiMatchQuery(text).field("categoryName^6")
						.field("categoryNameNormalize^5").field("name^4").field("nameNormalize^3")
						.field("description^2").field("descriptionNormalize^1").type(MatchQuery.Type.PHRASE_PREFIX));

		searchQuery = new NativeSearchQueryBuilder().withQuery(query).withPageable(pageable).build();
		String uu = searchQuery.getQuery().toString();
		Page<POI> searchRs = poiRepo.search(searchQuery);

		// fuzzy search
		if (searchRs.getContent().isEmpty()) {
			org.elasticsearch.index.query.QueryBuilder fuzzyQuery = QueryBuilders.multiMatchQuery(text).field("name^6")
					.field("nameNormalize^5").field("categoryName^4").field("categoryNameNormalize^3")
					.field("description^2").field("descriptionNormalize^1").fuzziness(Fuzziness.AUTO);
			searchQuery = new NativeSearchQueryBuilder().withQuery(fuzzyQuery).withPageable(pageable).build();
			String uu1 = searchQuery.getQuery().toString();
			searchRs = poiRepo.search(searchQuery);
		}

		List<SearchPointOfInterestDTO> list = StreamSupport.stream(searchRs.spliterator(), false)
				.map(poi -> SearchPointOfInterestDTO.from(poi, location)).collect(Collectors.toList());

		return list;
	}

	// ======================custom search not use current=====================
	public Page<POI> queryMatch(String text, Pageable pageable, String originalValue, String normalizeValue) {
		Page<POI> searchRs = null;
		SearchQuery searchQuery;
		QueryBuilder query = QueryBuilders.matchQuery(originalValue, text);
		searchQuery = new NativeSearchQueryBuilder().withQuery(query).withPageable(pageable).build();
		String x = searchQuery.getQuery().toString();
		searchRs = poiRepo.search(searchQuery);
		if (searchRs.getContent().isEmpty()) {
			query = QueryBuilders.matchQuery(normalizeValue, text);
			searchQuery = new NativeSearchQueryBuilder().withQuery(query).withPageable(pageable).build();
			x = searchQuery.getQuery().toString();
			searchRs = poiRepo.search(searchQuery);
		}
		return searchRs;
	}

	public Page<POI> queryWildcard(String text, Pageable pageable, String originalValue) {// , String normalizeValue) {
		SearchQuery searchQuery;
		String wildcardValue = text + "*";
		Page<POI> searchRs = null;
		QueryBuilder wildcard = QueryBuilders.wildcardQuery(originalValue, wildcardValue);
		searchQuery = new NativeSearchQueryBuilder().withQuery(wildcard).withPageable(pageable).build();
		String x = searchQuery.getQuery().toString();
		searchRs = poiRepo.search(searchQuery);
		/*
		 * if (searchRs.getContent().isEmpty()) { wildcard =
		 * QueryBuilders.wildcardQuery(normalizeValue, wildcardValue); searchQuery = new
		 * NativeSearchQueryBuilder().withQuery(wildcard).withPageable(pageable).build()
		 * ; x = searchQuery.getQuery().toString(); searchRs =
		 * poiRepo.search(searchQuery); }
		 */
		return searchRs;
	}

	public List<SearchPointOfInterestDTO> search1(String text, String location, Pageable pageable) {
		SearchQuery searchQuery;
		String wildcardValue = text + "*";
		Page<POI> searchRs = null;
		searchRs = queryWildcard(text, pageable, "name");
		if (searchRs.getContent().isEmpty()) {
			searchRs = queryMatch(text, pageable, "name", "nameNormalize");
			if (searchRs.getContent().isEmpty()) {
				searchRs = queryMatch(text, pageable, "categoryName", "categoryNameNormalize");
				if (searchRs.getContent().isEmpty()) {
					searchRs = queryMatch(text, pageable, "description", "descriptionNormalize");
				}
			}
		}
		List<SearchPointOfInterestDTO> list = StreamSupport.stream(searchRs.spliterator(), false)
				.map(poi -> SearchPointOfInterestDTO.from(poi, location)).collect(Collectors.toList());
		return list;
	}

	public List<SearchPointOfInterestDTO> getPoisRemovedSpace1(List<SearchPointOfInterestDTO> pois, String text) {
		int index = -1;
		for (int i = 0; i < pois.size(); i++) {
			String name = pois.get(i).getName();
			if (!StringUtils.isBlank(name)) {
				// remove white space.
				String namers = name.replaceAll("\\s+", "");
				String output = text.replaceAll("\\s+", "");
				if (output.equalsIgnoreCase(namers)) {
					return pois.stream().skip(i).collect(Collectors.toList());
				}
			}
		}
		return null;
	}

	public List<SearchPointOfInterestDTO> getPoisRemovedSpace(List<SearchPointOfInterestDTO> pois, String text) {
		for (int i = 0; i < pois.size(); i++) {
			String name = pois.get(i).getName();
			if (!StringUtils.isBlank(name)) {
				// remove white space.
				String namers = name.replaceAll("\\s+", "");
				String output = text.replaceAll("\\s+", "");
				if (output.equalsIgnoreCase(namers)) {
					return pois.stream().skip(i).collect(Collectors.toList());
				}
			}
		}
		return null;
	}

	public List<SearchPointOfInterestDTO> getPoisRemovedAccent(List<SearchPointOfInterestDTO> pois, String text) {
		for (int i = 0; i < pois.size(); i++) {
			String name = pois.get(i).getName();
			if (!StringUtils.isBlank(name)) {
				// remove white space.
				String namers = name.replaceAll("\\s+", "");
				String nameR = vn.com.omart.sharedkernel.StringUtils.removeAccent(namers);
				String output = text.replaceAll("\\s+", "");
				String outputR = vn.com.omart.sharedkernel.StringUtils.removeAccent(output);
				if (outputR.equalsIgnoreCase(nameR)) {
					return pois.stream().skip(i).collect(Collectors.toList());
				}
			}
		}
		return null;
	}
	// =================================filter result at the end
	// o==============================

	/**
	 * API search.
	 *
	 * @param text
	 * @param radius
	 * @param location
	 * @return List of SearchPointOfInterestDTO
	 */
	@GetMapping(value = { "/pois" })
	public List<SearchPointOfInterestDTO> search13(@RequestParam(value = "text") String text,
			@RequestParam(value = "radius", required = false, defaultValue = "30") Double radius,
			@RequestHeader(value = "X-Location-Geo", required = false) String location) {

		List<SearchPointOfInterestDTO> list = null;

		// Search by category keywords first.
		if (text == null) {
			text = "";
		}

		list = this.pointOfInterestRepository.findShopByCategory("%," + text.trim() + " %", elasticSearchMaxResults)
				.stream().map(POI::from).map(poi -> SearchPointOfInterestDTO.from(poi, location))
				.collect(Collectors.toList());

		// If not found, then search from eslatic search
		if (list == null || list.isEmpty()) {
			list = this.searchWithPhrasePrefix(text, location, new PageRequest(0, 2000));
		}

		// List<SearchPointOfInterestDTO> list = this.search(text, location, new
		// PageRequest(0, 2000));

		// List<SearchPointOfInterestDTO> list = this.searchWithPhrasePrefix(text,
		// location, new PageRequest(0, 2000));

		if (!StringUtils.isBlank(location)) {

			// //// Remove Location Search condition
			// // Temporarily hard code radius
			// radius = 2000d;
			// radius = radius * 1000;
			// List<SearchPointOfInterestDTO> rs = new ArrayList<>();
			// // List<SearchPointOfInterestDTO> rss = new ArrayList<>();
			// for (SearchPointOfInterestDTO dto : list) {
			// if (isWithinRadius(dto, radius)) {
			// rs.add(dto);
			// }
			// }

			List<SearchPointOfInterestDTO> rssorted = list.stream()
					.sorted(Comparator.comparing(SearchPointOfInterestDTO::getDistance)).collect(Collectors.toList());
			// a.hien request remove checking accent and space.
			/*
			 * rss = this.getPoisRemovedSpace(rssorted, text); if (rss != null) { return
			 * rss; } else { rss = this.getPoisRemovedAccent(rssorted, text); if (rss !=
			 * null) { return rss; } }
			 */

			// return rssorted;
			return rssorted.stream().skip(0).limit(25).collect(Collectors.toList());
		}

		return list.stream().skip(0).limit(25).collect(Collectors.toList());
	}

	@GetMapping(value = { "/pois_holding" })
	public List<SearchPointOfInterestDTO> search(@RequestParam(value = "text") String text,
			@RequestParam(value = "radius", required = false, defaultValue = "30") Double radius,
			@RequestHeader(value = "X-Location-Geo", required = false) String location) {

		List<SearchPointOfInterestDTO> list = null;

		// Search by category keywords first.
		if (text == null) {
			text = "";
		}
		text = "%," + text.trim() + " %";

		list = this.pointOfInterestRepository.findShopByCategory(text, elasticSearchMaxResults).stream().map(POI::from)
				.map(poi -> SearchPointOfInterestDTO.from(poi, location)).collect(Collectors.toList());

		// If not found, then search by category name
		if (list == null || list.isEmpty()) {
			list = searchFromElasticSearch(text, location);
		}

		List<SearchPointOfInterestDTO> rs = new ArrayList<>();

		// Temporarily hard code radius
		radius = 2000d;
		radius = radius * 1000;

		for (SearchPointOfInterestDTO dto : list) {
			if (isWithinRadius(dto, radius)) {
				rs.add(dto);
			}
		}

		Collections.sort(rs, new Comparator<SearchPointOfInterestDTO>() {

			@Override
			public int compare(SearchPointOfInterestDTO o1, SearchPointOfInterestDTO o2) {
				Long distance1 = o1.getDistance();
				Long distance2 = o2.getDistance();

				if (distance1 == null) {
					distance1 = 0l;
				}

				if (distance2 == null) {
					distance2 = 0l;
				}

				return distance1.compareTo(distance2);
			}
		});

		return rs;
	}

	private List<SearchPointOfInterestDTO> searchFromElasticSearch(String text, String location) {

		SearchQuery searchQuery;
		Page<POI> searchRs;

		// Search maximum 100 results
		Pageable pageable = new PageRequest(0, elasticSearchMaxResults);

		// Search by category keywords
		searchQuery = new NativeSearchQueryBuilder().withQuery(multiMatchQuery(text).field("categoryKeywords")
				.type(MultiMatchQueryBuilder.Type.BEST_FIELDS).minimumShouldMatch("85%")).withPageable(pageable)
				.build();

		searchRs = poiRepo.search(searchQuery);

		// If not founds, then search by others fields.
		if (searchRs == null || !searchRs.hasNext()) {
			searchQuery = new NativeSearchQueryBuilder().withQuery(multiMatchQuery(text).field("name")
					.field("nameNormalize").field("description").field("descriptionNormalize").field("categoryName")
					.field("categoryNameNormalize").field("address").field("addressNormalize").field("wardName")
					.field("wardNameNormalize").field("districtName").field("districtNameNormalize")
					.field("provinceName").field("provinceNameNormalize").field("phone")
					.type(MultiMatchQueryBuilder.Type.BEST_FIELDS).minimumShouldMatch("75%")).withPageable(pageable)
					.build();

			searchRs = poiRepo.search(searchQuery);
		}

		List<SearchPointOfInterestDTO> list = StreamSupport.stream(searchRs.spliterator(), false)
				.map(poi -> SearchPointOfInterestDTO.from(poi, location)).collect(Collectors.toList());

		return list;
	}

	@GetMapping(value = { "/pois_old" })
	public List<SearchPointOfInterestDTO> searchOld(@RequestParam(value = "text") String text,
			@RequestParam(value = "radius", required = false, defaultValue = "30") Double radius,
			@RequestHeader(value = "X-Location-Geo", required = false) String location) {

		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(multiMatchQuery(text).field("name")
				.field("nameNormalize").field("description").field("descriptionNormalize").field("categoryName")
				.field("categoryNameNormalize").field("address").field("addressNormalize").field("wardName")
				.field("wardNameNormalize").field("districtName").field("districtNameNormalize").field("provinceName")
				.field("provinceNameNormalize").field("phone").type(MultiMatchQueryBuilder.Type.BEST_FIELDS)
				.minimumShouldMatch("75%")).build();

		// Match _all fields
		// QueryBuilders.queryStringQuery(text)

		List<SearchPointOfInterestDTO> list = StreamSupport.stream(poiRepo.search(searchQuery).spliterator(), false)
				.map(poi -> SearchPointOfInterestDTO.from(poi, location)).collect(Collectors.toList());

		List<SearchPointOfInterestDTO> rs = new ArrayList<>();

		// Temporarily hard code radius
		radius = 2000d;
		radius = radius * 1000;

		for (SearchPointOfInterestDTO dto : list) {
			if (isWithinRadius(dto, radius)) {
				rs.add(dto);
			}
		}

		Collections.sort(rs, new Comparator<SearchPointOfInterestDTO>() {

			@Override
			public int compare(SearchPointOfInterestDTO o1, SearchPointOfInterestDTO o2) {
				Long distance1 = o1.getDistance();
				Long distance2 = o2.getDistance();

				if (distance1 == null) {
					distance1 = 0l;
				}

				if (distance2 == null) {
					distance2 = 0l;
				}

				return distance1.compareTo(distance2);
			}
		});

		return rs;
	}

	private boolean isWithinRadius(SearchPointOfInterestDTO poi, Double radius) {
		Long distance = poi.getDistance();
		return distance == null || distance <= radius;
	}

	@GetMapping(value = { "/ownerprovince/{ownerPhone}" })
	public ProvinceDTO getOwnerProvince(@PathVariable(value = "ownerPhone") String ownerPhone) {
		return this.mobileAdminService.getOwnerProvince(ownerPhone);
	}

	/**
	 * Save Poi Action LIKE,DISLIKE,FAVORITE.
	 *
	 * @param userId
	 * @param action
	 * @param poiId
	 * @return JSONObject
	 */
	@RequestMapping(value = "/pois/{poi-id}/action", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> poiAction(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestBody(required = true) PoiActionDTO action,
			@PathVariable(value = "poi-id", required = true) Long poiId) {
		JSONObject count = service.savePoiAction(poiId, userId, action.getStatus());
		return new ResponseEntity<JSONObject>(count, HttpStatus.OK);
	}

	/**
	 * View Count.
	 *
	 * @param poiId
	 * @return JSONObject.
	 */
	@RequestMapping(value = "/pois/{poi-id}/view", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> viewCount(@PathVariable(value = "poi-id", required = true) Long poiId) {
		JSONObject viewCount = service.viewCount(poiId);
		return new ResponseEntity<JSONObject>(viewCount, HttpStatus.OK);
	}

	/**
	 * Get Poi detail.
	 *
	 * @param poiId
	 * @param latitude
	 * @param longitude
	 * @param username
	 * @return PointOfInterestDTO
	 */
	@RequestMapping(value = "/pois/{poi-id}", method = RequestMethod.GET)
	public ResponseEntity<PointOfInterestDTO> viewPoiDetail(@PathVariable(value = "poi-id", required = true) Long poiId,
			@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude,
			@RequestHeader(value = "X-User-Id", required = false) String username,
			@RequestHeader(value = "lang", required = false, defaultValue = "") String language) {
		PointOfInterestDTO poiDetail = service.getPoiDetail(poiId, username, latitude, longitude, language);
		return new ResponseEntity<PointOfInterestDTO>(poiDetail, HttpStatus.OK);
	}

	/**
	 * Update Poi business.
	 *
	 * @param poiDto
	 * @param username
	 * @param language
	 * @return PointOfInterestDTO
	 */
	@RequestMapping(value = "/pois/update", method = RequestMethod.POST)
	public ResponseEntity<PointOfInterestDTO> updatePoiBuz(@RequestBody(required = true) PointOfInterestDTO poiDto,
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestHeader(value = "lang", required = false, defaultValue = "") String language) {
		PointOfInterestDTO poiDetail = service.updatePoiBuz(poiDto, userId, language);
		return new ResponseEntity<PointOfInterestDTO>(poiDetail, HttpStatus.OK);
	}

	/**
	 * Save poi comment.
	 *
	 * @param poiId
	 * @param comment
	 * @param userId
	 * @return JSONObject
	 */
	@RequestMapping(value = "/pois/{poi-id}/comment", method = RequestMethod.POST)
	public ResponseEntity<EmptyJsonResponse> saveComment(@PathVariable(value = "poi-id", required = true) Long poiId,
			@RequestBody(required = true) PoiComment comment,
			@RequestHeader(value = "X-User-Id", required = true) String userId) {
		service.saveComment(poiId, comment, userId);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.CREATED);
	}

	/**
	 * Get Poi Comments.
	 *
	 * @param poiId
	 * @param pageable
	 * @return Page of PoiCommentDTO
	 */
	@RequestMapping(value = "/pois/{poi-id}/comments", method = RequestMethod.GET)
	public ResponseEntity<Page<PoiCommentDTO>> getComments(@PathVariable(value = "poi-id", required = true) Long poiId,
			@PageableDefault(size = Integer.MAX_VALUE, page = 0) Pageable pageable) {
		Page<PoiCommentDTO> comments = service.getCommentByPoiId(poiId, pageable);
		// List<PoiCommentDTO> poi = comments.getContent();
		if (comments == null) {
			return new ResponseEntity<Page<PoiCommentDTO>>(comments, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Page<PoiCommentDTO>>(comments, HttpStatus.OK);
	}

	/**
	 * Save Picture Action LIKE,DISLIKE.
	 *
	 * @param userId
	 * @param action
	 * @param pictureId
	 * @return JSONObject
	 */
	@RequestMapping(value = "/pois/picture/{picture-id}/action", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> saveActionLikeOnPicture(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestBody(required = true) PoiActionDTO action,
			@PathVariable(value = "picture-id", required = true) Long pictureId) {
		JSONObject entity = poiPictureService.saveActionLikeOnPicture(userId, action.getStatus(), pictureId);
		return new ResponseEntity<JSONObject>(entity, HttpStatus.OK);
	}

	/**
	 * Share Count.
	 *
	 * @param poiId
	 * @return JSONObject.
	 */
	@RequestMapping(value = "/pois/{poi-id}/share", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> sharePoi(@PathVariable(value = "poi-id", required = true) Long poiId) {
		JSONObject viewCount = service.shareCount(poiId);
		return new ResponseEntity<JSONObject>(viewCount, HttpStatus.OK);
	}

	/**
	 * Create shop from shop owner.
	 *
	 * @param username
	 * @param location
	 * @param payload
	 * @return PointOfInterestDTO
	 */
	@PostMapping(value = { "/pois" })
	public PointOfInterestDTO createPOI(@RequestHeader(value = "X-User-Id", required = false) String username,
			@RequestHeader(value = "X-Location-Geo") String location,
			@RequestBody @Valid PointOfInterestCmd.CreateOrUpdate payload) {

		if (service.isSameAddressAndName(username, payload)) {
			throw new AlreadyExistingException("Is the same Shop Name and Address");
		}

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
			return this.mobileAdminService.createPointOfInterest(username, location, payload);
		} catch (NumberFormatException e) {
			throw new NotFoundException("Category not exists");
		}
	}

	/**
	 * Update poi cover.
	 *
	 * @param poiId
	 * @param poiDTO
	 * @return List of Image
	 */
	@RequestMapping(value = "/pois/cover/{poi-id}", method = RequestMethod.PUT)
	public ResponseEntity<List<Image>> updatePoiPanner(@PathVariable(value = "poi-id", required = true) Long poiId,
			@RequestBody(required = true) PoiDTO poiDTO) {
		List<Image> entity = mobileAdminService.updatePoiCoverImage(poiId, poiDTO.getCovers());
		return new ResponseEntity<List<Image>>(entity, HttpStatus.OK);
	}

	/**
	 * Update poi avatar.
	 *
	 * @param poiId
	 * @param poiDTO
	 * @return List of Image
	 */
	@RequestMapping(value = "/pois/avatar/{poi-id}", method = RequestMethod.PUT)
	public ResponseEntity<List<Image>> updatePoiAvatar(@PathVariable(value = "poi-id", required = true) Long poiId,
			@RequestBody(required = true) PoiDTO poiDTO) {
		List<Image> entity = mobileAdminService.updatePoiAvatar(poiId, poiDTO.getAvatars());
		return new ResponseEntity<List<Image>>(entity, HttpStatus.OK);
	}

	/**
	 * Update picture from shop owner.
	 *
	 * @param picture
	 * @param userId
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/pois/picture/{picture-id}", method = RequestMethod.PUT)
	public ResponseEntity<EmptyJsonResponse> updatePoiPicture(
			@PathVariable(value = "picture-id", required = true) Long pictureId,
			@RequestBody(required = false) PoiPicture picture,
			@RequestHeader(value = "X-User-Id", required = true) String userId) {
		poiPictureService.updatePoiPicture(picture, userId, pictureId);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Post picture from shop owner.
	 *
	 * @param picture
	 * @param userId
	 * @return List of PoiPictureDTO
	 */
	@RequestMapping(value = "/pois/picture", method = RequestMethod.POST)
	public ResponseEntity<List<PoiPictureDTO>> postPoiPicture(@RequestBody(required = false) PoiDTO picture,
			@RequestHeader(value = "X-User-Id", required = true) String userId) {
		List<PoiPictureDTO> poiPicture = poiPictureService.savePoiPicture(picture, userId);
		return new ResponseEntity<List<PoiPictureDTO>>(poiPicture, HttpStatus.CREATED);
	}

	/**
	 * Get Poi Timeline from shop owner.
	 *
	 * @param poiId
	 * @param userId
	 * @param pageable
	 * @return List<PoiTimelineDTO>
	 */
	@RequestMapping(value = "/pois/timeline/{poi-id}", method = RequestMethod.GET)
	public ResponseEntity<List<PoiTimelineDTO>> getPoiPictureV2(
			@PathVariable(value = "poi-id", required = true) Long poiId,
			@RequestHeader(value = "X-User-Id", required = false) String userId,
			@PageableDefault(size = 10, page = 0) Pageable pageable) {
		List<PoiTimelineDTO> poiDto = poiPictureService.getPoiPicture(poiId, userId, pageable);
		return new ResponseEntity<List<PoiTimelineDTO>>(poiDto, HttpStatus.OK);
	}

	/**
	 * Post Poi Timeline from shop owner.
	 *
	 * @param dto
	 * @param userId
	 * @return PoiTimelineDTO
	 */
	@RequestMapping(value = "/pois/timeline", method = RequestMethod.POST)
	public ResponseEntity<PoiTimelineDTO> postPoiPictureV2(@RequestBody(required = true) PoiTimelineDTO dto,
			@RequestHeader(value = "X-User-Id", required = true) String userId) {
		PoiTimelineDTO poiDto = poiPictureService.savePoiPicture(dto, userId);
		return new ResponseEntity<PoiTimelineDTO>(poiDto, HttpStatus.CREATED);
	}

	/**
	 * Delete Poi Timeline from shop owner.
	 *
	 * @param postId
	 * @param userId
	 * @return PoiTimelineDTO
	 */
	@RequestMapping(value = "/pois/timeline/{post-id}", method = RequestMethod.DELETE)
	public ResponseEntity<EmptyJsonResponse> deletePoiPictureV2(
			@PathVariable(value = "post-id", required = true) Long postId,
			@RequestHeader(value = "X-User-Id", required = true) String userId) {
		poiPictureService.deletePoiPicture(postId, userId);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Update Poi by Name.
	 *
	 * @param poiId
	 * @param userId
	 * @param poiDTO
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/pois/name/{poi-id}", method = RequestMethod.PUT)
	public ResponseEntity<EmptyJsonResponse> updatePoiByName(
			@PathVariable(value = "poi-id", required = true) Long poiId,
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestBody(required = true) PoiDTO poiDTO) {
		service.updatePoiByName(poiId, poiDTO, userId);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Update Poi state.
	 *
	 * @param poiId
	 * @param userId
	 * @param poiDTO
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/pois/state/{poi-id}", method = RequestMethod.PUT)
	public ResponseEntity<EmptyJsonResponse> updatePoiByState(
			@PathVariable(value = "poi-id", required = true) Long poiId,
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestBody(required = true) PoiDTO poiDTO) {
		service.updatePoiByState(poiId, poiDTO, userId);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Update Poi by Address.
	 *
	 * @param poiId
	 * @param userId
	 * @param poiDTO
	 * @return EmptyJsonResponse
	 */
	@Deprecated
	@RequestMapping(value = "/pois/address/{poi-id}", method = RequestMethod.PUT)
	public ResponseEntity<EmptyJsonResponse> updatePoiAddress(
			@PathVariable(value = "poi-id", required = true) Long poiId,
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestBody(required = true) PoiDTO poiDTO) {
		service.updatePoiByAddress(poiId, poiDTO, userId);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Upload picture to thumbor Server.
	 *
	 * @param imgName
	 * @param file
	 * @return ImageDTO.
	 */
	@PostMapping(value = "/pois/picture-upload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ImageDTO attachMultiPart(
			@RequestParam(value = "filename", required = false, defaultValue = "omart") String imgName,
			@RequestParam("file") MultipartFile file) {
		if ("omart".equals(imgName)) {
			imgName = file.getOriginalFilename();
		}
		try {
			return new ImageDTO(thumborService.uploadImage(imgName, file.getBytes()));
		} catch (IOException e) {
			throw new ApplicationException("Cannot get file from MultiPart form");
		}
	}

	/**
	 * Delete poi picture.
	 *
	 * @param userId
	 * @param pictureId
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/pois/picture/{picture-id}", method = RequestMethod.DELETE)
	public ResponseEntity<EmptyJsonResponse> deletePoiPicture(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "picture-id", required = true) Long pictureId) {
		poiPictureService.deletePoiPicture(pictureId);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Share to facebook.
	 *
	 * @param poiId
	 * @return HTML
	 */
	@RequestMapping(value = "/pois/{poi-id}/facebook", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public ResponseEntity<String> facebookShare(@PathVariable(value = "poi-id", required = true) Long poiId) {
		String html = service.getShareContent(poiId);
		return new ResponseEntity<String>(html, HttpStatus.OK);
	}

	/**
	 * Update basic poi.
	 *
	 * @param poiId
	 * @param userId
	 * @param poiDTO
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/pois/{poi-id}", method = RequestMethod.PUT)
	public ResponseEntity<EmptyJsonResponse> updateBasicInfoPoi(
			@PathVariable(value = "poi-id", required = true) Long poiId,
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestBody(required = true) PoiDTO poiDTO) {
		service.updateBasicInfoPoi(poiId, poiDTO, userId);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Populate Full Address.
	 *
	 * @param latitude
	 * @param longitude
	 * @return FullAddressDTO
	 */
	@RequestMapping(value = "pois/full-address", method = RequestMethod.GET)
	public ResponseEntity<FullAddressDTO> getFullAddressFromGoogle(
			@RequestParam(value = "latitude", required = true) Double latitude,
			@RequestParam(value = "longitude", required = true) Double longitude) {
		String latlng = String.valueOf(latitude) + "," + String.valueOf(longitude);
		FullAddressDTO resu = mobileAdminService.getFullAddressFromGoogle(latlng);
		return new ResponseEntity<FullAddressDTO>(resu, HttpStatus.OK);
	}

	/**
	 * Find Pois By Nearest.
	 *
	 * @param pageable
	 * @param latitude
	 * @param longitude
	 * @param radius
	 * @return List of PointOfInterestDTO.
	 */
	@RequestMapping(value = "/pois/nearest", method = RequestMethod.GET)
	public ResponseEntity<List<PointOfInterestDTO>> findAllNearestPoi(
			@PageableDefault(size = 300, page = 0) Pageable pageable,
			@RequestParam(value = "latitude", required = true) Double latitude,
			@RequestParam(value = "longitude", required = true) Double longitude,
			@RequestParam(value = "radius", required = true) int radius) {
		List<PointOfInterestDTO> pois = service.findAllNearestPois(latitude, longitude, radius, pageable);
		return new ResponseEntity<List<PointOfInterestDTO>>(pois, HttpStatus.OK);
	}

	/**
	 * Find Pois By Nearest.
	 *
	 * @param pageable
	 * @param latitude
	 * @param longitude
	 * @param radius
	 * @return List of PointOfInterestDTO.
	 */
	@RequestMapping(value = "/pois/nearest1", method = RequestMethod.GET)
	public ResponseEntity<List<PointOfInterestDTO>> findAllNearestPoi(
			@RequestHeader(value = "X-User-Id", required = false) String userId,
			@PageableDefault(size = 300, page = 0) Pageable pageable,
			@RequestParam(value = "latitude", required = true) Double latitude,
			@RequestParam(value = "longitude", required = true) Double longitude,
			@RequestParam(value = "radius", required = true) int radius) {
		List<PointOfInterestDTO> pois = service.findAllNearestPois_Enhance(userId, latitude, longitude, radius,
				pageable);
		return new ResponseEntity<List<PointOfInterestDTO>>(pois, HttpStatus.OK);
	}

	/**
	 * Find Pois By Nearest V1.1. Note pageable in request body.
	 * 
	 * @param userId
	 * @param pageable
	 * @param nearest
	 * @return List of PointOfInterestDTO
	 */
	@RequestMapping(value = "/pois/nearest", method = RequestMethod.POST, headers = { "X-API-Version=1.1" })
	public ResponseEntity<List<PointOfInterestDTO>> findAllNearestPoi_V11(
			@RequestHeader(value = "X-User-Id", required = false) String userId,
			@RequestBody(required = true) StoreProcedureParams nearest) {
		List<PointOfInterestDTO> pois = service.findAllNearestPois_V11(userId, nearest);
		return new ResponseEntity<List<PointOfInterestDTO>>(pois, HttpStatus.OK);
	}

	/**
	 * Find Pois By Nearest V1.2. Note pageable in request body.
	 * 
	 * @param userId
	 * @param pageable
	 * @param nearest
	 * @return List of PointOfInterestDTO
	 */
	@RequestMapping(value = "/pois/nearest", method = RequestMethod.POST, headers = { "X-API-Version=1.2" })
	public ResponseEntity<List<PointOfInterestDTO>> findAllNearestPoi_V12(
			@RequestHeader(value = "X-User-Id", required = false) String userId,
			@RequestBody(required = true) StoreProcedureParams nearest) {
		List<PointOfInterestDTO> pois = service.findAllNearestPois_V12(userId, nearest);
		return new ResponseEntity<List<PointOfInterestDTO>>(pois, HttpStatus.OK);
	}

	/**
	 * Get category by language.
	 *
	 * @param lang
	 * @return List of CategoryDTO
	 */
	@RequestMapping(value = "/categories", method = RequestMethod.GET)
	public ResponseEntity<List<CategoryDTO>> getCategories(
			@RequestHeader(value = "lang", required = false, defaultValue = "") String lang) {
		return new ResponseEntity<List<CategoryDTO>>(service.getCategories(lang), HttpStatus.OK);
	}

	/**
	 * Get category by language V1.
	 *
	 * @param lang
	 * @return List of CategoryDTO
	 */
	@RequestMapping(value = "/categories", method = RequestMethod.GET, headers = { "X-API-Version=1.1" })
	public ResponseEntity<CategoryParentDTO> getCategories_V1(
			@RequestHeader(value = "X-User-Id", required = false) String userId,
			@RequestHeader(value = "lang", required = false, defaultValue = "") String lang) {
		return new ResponseEntity<CategoryParentDTO>(service.getCategories_V1(userId, lang), HttpStatus.OK);
	}

	/**
	 * Get category by language V2.
	 *
	 * @param lang
	 * @return List of CategoryDTO
	 */
	@RequestMapping(value = "/categories", method = RequestMethod.GET, headers = { "X-API-Version=1.2" })
	public ResponseEntity<CategoryParentDTO> getCategories_V2(
			@RequestHeader(value = "X-User-Id", required = false) String userId,
			@RequestHeader(value = "lang", required = false, defaultValue = "") String lang) {
		return new ResponseEntity<CategoryParentDTO>(service.getCategories_V2(userId, lang), HttpStatus.OK);
	}

	/**
	 * Get owner info of poi.
	 * 
	 * @param ownerId
	 * @return POIOwnerDTO
	 */
	@RequestMapping(value = "/me", method = RequestMethod.GET, headers = { "X-API-Version=1.1" })
	public POIOwnerDTO getProfile_V1(@RequestHeader(value = "X-User-Id", required = false) String ownerId) {
		if (StringUtils.isBlank(ownerId)) {
			throw new UnauthorizedAccessException("Missing user-id");
		}
		return mobileAdminService.getOwner_V1(ownerId);
	}

	/**
	 * Update Last Notification.
	 * 
	 * @param poiId
	 * @param userId
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/pois/{poi-id}/notification", method = RequestMethod.PUT)
	public ResponseEntity<EmptyJsonResponse> postNotification(
			@PathVariable(value = "poi-id", required = true) Long poiId,
			@RequestHeader(value = "X-User-Id", required = false) String userId) {
		service.updateLastNotification(poiId);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}

	/**
	 * Get home features.
	 * 
	 * @return List of HomeFeatureDTO
	 */
	@GetMapping(value = "/home-features")
	public ResponseEntity<List<HomeFeatureDTO>> getHomeFeatures() {
		return new ResponseEntity<List<HomeFeatureDTO>>(homeFeatureService.getByApproval(), HttpStatus.OK);
	}

	/**
	 * Get home banners.
	 * 
	 * @param client
	 * @param pageable
	 * @return List of HomeBannerDTO
	 */
	@GetMapping(value = "/home-banners")
	public ResponseEntity<List<HomeBannerDTO>> getHomeBanners(
			@RequestHeader(value = "client", required = false) Device client,
			@PageableDefault(page = 0, size = 20) Pageable pageable) {
		return new ResponseEntity<List<HomeBannerDTO>>(homeBannerService.getByApprovalAndBannerType(client, pageable),
				HttpStatus.OK);
	}

	/**
	 * Get pois user followed.
	 * 
	 * @param userId
	 * @param pageable
	 * @return List of PointOfInterestDTO
	 */
	@GetMapping(value = "/pois/follows")
	public ResponseEntity<List<PointOfInterestDTO>> getPoiByUserLiked(
			@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PageableDefault(page = 0, size = Integer.MAX_VALUE) Pageable pageable) {
		List<PointOfInterestDTO> dtos = service.getPoiByUserLiked(userId, pageable);
		return new ResponseEntity<List<PointOfInterestDTO>>(dtos, HttpStatus.OK);
	}

	/*
	 * Only for developer to migrated data.
	 */
	@RequestMapping(value = "/pois/timeline/copyDataIntoCreatedAt", method = RequestMethod.GET)
	public ResponseEntity<Void> copyDataIntoCreatedAt() {
		poiPictureService.copyDataIntoCreatedAt();
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
