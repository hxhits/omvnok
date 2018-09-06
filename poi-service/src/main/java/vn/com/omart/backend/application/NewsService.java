package vn.com.omart.backend.application;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vdurmont.emoji.EmojiParser;

import vn.com.omart.backend.application.request.NewsCmd;
import vn.com.omart.backend.application.response.NewsDTO;
import vn.com.omart.backend.application.response.PoiActionDTO;
import vn.com.omart.backend.application.response.StoreProcedureParams;
import vn.com.omart.backend.application.util.CommonUtils;
import vn.com.omart.backend.application.util.DateUtils;
import vn.com.omart.backend.constants.ConstantUtils;
import vn.com.omart.backend.domain.model.News;
import vn.com.omart.backend.domain.model.NewsRepository;
import vn.com.omart.backend.domain.model.PoiActionRepository;
import vn.com.omart.backend.domain.model.PointOfInterest;
import vn.com.omart.backend.domain.model.PointOfInterestRepository;
import vn.com.omart.sharedkernel.application.model.error.InvalidInputException;
import vn.com.omart.sharedkernel.application.model.error.NotFoundException;

@Service
public class NewsService {

	@Autowired
	private PoiActionRepository poiActionRepository;

	private final NewsRepository newsRepository;

	@Autowired
	private EntityManager em;

	@Autowired
	private PointOfInterestRepository pointOfInterestRepository;

	@Autowired
	public NewsService(NewsRepository newsRepository) {
		this.newsRepository = newsRepository;
	}

	public NewsDTO createNews(String userId, NewsCmd.CreateNew dto) {

		if (dto == null) {
			throw new InvalidInputException("dto should not be null");
		}

		if (StringUtils.isEmpty(dto.getTitle())) {
			throw new InvalidInputException("title should not be null or empty");
		}

		News entity = toEntity(userId, dto);

		entity = this.newsRepository.save(entity);

		return NewsDTO.from(entity);
	}

	private News toEntity(String userId, NewsCmd.CreateNew dto) {

		News entity = new News();

		entity.setTitle(dto.getTitle());
		entity.setDesc(dto.getDesc());

		entity.setThumbnailUrl(dto.getThumbnailUrl());
		entity.setBannerUrl(dto.getBannerUrl());

		entity.setNewsType(dto.getType());
		entity.setRead(dto.isRead());

		entity.setCreatedAt(DateUtils.getCurrentDate());
		entity.setCreatedBy(userId);

		return entity;
	}

	public NewsDTO getNews(Long newsId) {

		News one = newsRepository.findOne(newsId);

		if (one == null) {
			throw new NotFoundException("News not exists or has just been delete");
		}

		return NewsDTO.from(one);
	}

	public List<NewsDTO> getAllNews() {
		return this.newsRepository.findAll().stream().map(NewsDTO::from).sorted(newsCompare())
				.collect(Collectors.toList());
	}

	public NewsDTO updateNews(String userId, Long id, NewsCmd.CreateOrUpdate payload) {

		if (payload == null) {
			throw new InvalidInputException("Input should not be null");
		}

		News model = this.newsRepository.findOne(id);

		if (model == null) {
			throw new NotFoundException("News not exists or has just been deleted");
		}

		String content = payload.getDesc();
		if (StringUtils.isNotEmpty(content)) {
			model.setDesc(content);
		}

		String title = payload.getTitle();
		if (StringUtils.isNotEmpty(title)) {
			model.setTitle(title);
		}

		String thumbnailUrl = payload.getThumbnailUrl();
		if (StringUtils.isNotEmpty(thumbnailUrl)) {
			model.setThumbnailUrl(thumbnailUrl);
		}

		String bannerUrl = payload.getBannerUrl();
		if (StringUtils.isNotEmpty(bannerUrl)) {
			model.setBannerUrl(bannerUrl);
		}

		int type = payload.getType();
		if (type >= 0) {
			model.setNewsType(type);
		}

		boolean read = payload.isRead();
		model.setRead(read);

		model.setUpdatedAt(DateUtils.getCurrentDate());
		model.setUpdatedBy(userId);

		String s = payload.getApproval();
		if (StringUtils.isNotEmpty(s)) {
			model.setApproval(Integer.valueOf(s));
		}

		model = this.newsRepository.save(model);

		return NewsDTO.from(model);
	}

	public void deleteNews(Long id) {
		this.newsRepository.delete(id);
	}

	private Comparator<NewsDTO> newsCompare() {

		return new Comparator<NewsDTO>() {
			@Override
			public int compare(NewsDTO o1, NewsDTO o2) {

				Date updatedAt2 = o2.getUpdatedAt();
				if (updatedAt2 == null) {
					updatedAt2 = o2.getCreatedAt();
				}

				Date updatedAt1 = o1.getUpdatedAt();
				if (updatedAt1 == null) {
					updatedAt1 = o1.getCreatedAt();
				}

				if (updatedAt2 != null && updatedAt1 != null) {
					return updatedAt2.compareTo(updatedAt1);
				}

				return o2.getId().compareTo(o1.getId());
			}
		};
	}

	/**
	 * Save Advertising.
	 *
	 * @param userId
	 * @param poiId
	 * @param entity
	 */
	public void saveAdvertising(String userId, Long poiId, NewsDTO entity) {
		PointOfInterest poi = pointOfInterestRepository.findOne(poiId);
		if (poi == null) {
			throw new NotFoundException("Poi not found");
		}
		News news = this.toEntity(entity);
		news.setCreatedBy(userId);
		news.setUpdatedBy(userId);
		news.setPoi(poi);
		news.setLongitude(poi.longitude());
		news.setLatitude(poi.latitude());
		news.setTitle(poi.getName());
		news.setNewsType(1);// 1 is only for shop adv.
		news.setApproval(0);
		if (poi.getAvatarImage() != null) {
			news.setThumbnailUrl(poi.getAvatarImage().get(0).getUrl());
		}
		newsRepository.save(news);
	}

	/**
	 * Convert DTO to News.
	 *
	 * @param dto
	 * @return News
	 */
	private News toEntity(NewsDTO dto) {
		News entity = new News();
		entity.setLatitude(dto.getLatitude());
		entity.setLongitude(dto.getLongitude());
		entity.setTitle(dto.getTitle());
		entity.setDesc(dto.getDesc());
		entity.setThumbnailUrl(dto.getThumbnailUrl());
		entity.setBannerUrl(dto.getBannerUrl());
		entity.setNewsType(dto.getType());
		entity.setRead(dto.isRead());
		entity.setCreatedAt(DateUtils.getCurrentDate());
		entity.setUpdatedAt(DateUtils.getCurrentDate());
		return entity;
	}

	/**
	 * TODO
	 *
	 * @param latitude
	 * @param longitude
	 * @param radius
	 * @param pageable
	 * @return List of NewsDTO
	 */
	public List<NewsDTO> getNewsOld(double latitude, double longitude, int radius, Pageable pageable) {
		// 1 is approved
		int approved = 1;
		if (radius == 0) {
			List<News> news = newsRepository.findByApprovalOrderByNewsTypeAscCreatedAtAsc(approved, pageable);
			List<NewsDTO> results = news.stream().map(NewsDTO::from).collect(Collectors.toList());
			return results;
		}
		int size = pageable.getPageSize();
		int pageNumber = pageable.getPageNumber();
		// get news from system.
		List<News> systemNews = newsRepository.findByApprovalAndNewsTypeOrderByCreatedAtAsc(approved, 0, pageable);

		List<News> poiNews = null;
		if (!systemNews.isEmpty()) {
			// checking page=0,size=4
			int sysSize = systemNews.size();
			if (sysSize < size) {
				int sizePoi = size - sysSize;
				// get news from poi.
				StoredProcedureQuery query = em.createNamedStoredProcedureQuery("geo_dist");
				query.setParameter("orig_latitude", latitude);
				query.setParameter("orig_longitude", longitude);
				query.setParameter("radius", CommonUtils.metersToMiles(radius));
				query.setParameter("paging", (pageNumber * sizePoi));
				query.setParameter("size", sizePoi);
				query.setParameter("approval", 1);
				query.execute();
				poiNews = query.getResultList();
			} else {
				List<NewsDTO> sysResults = systemNews.stream().map(NewsDTO::from).collect(Collectors.toList());
				return sysResults;
			}
		} else {
			StoredProcedureQuery query = em.createNamedStoredProcedureQuery("geo_dist");
			query.setParameter("orig_latitude", latitude);
			query.setParameter("orig_longitude", longitude);
			query.setParameter("radius", CommonUtils.metersToMiles(radius));
			query.setParameter("paging", (pageNumber * size));
			query.setParameter("size", size);
			query.setParameter("approval", 1);
			query.execute();
			poiNews = query.getResultList();
		}
		// merge news between system and poi.
		List<NewsDTO> newsDTO = null;
		Stream<News> results = Stream.concat(systemNews.stream(), poiNews.stream());
		newsDTO = results.map(NewsDTO::from).collect(Collectors.toList());
		return newsDTO;
	}

	/**
	 * Get news with geo-distance.(BE v1)
	 *
	 * @param latitude
	 * @param longitude
	 * @param radius
	 * @param pageable
	 * @return
	 */
	public List<NewsDTO> getNews(double latitude, double longitude, int radius, Pageable pageable) {
		// 1 is approved
		int approved = 1;
		if (latitude == 0 && longitude == 0 && radius == 0) {
			List<News> news = newsRepository.findByApprovalOrderByNewsTypeAscCreatedAtAsc(approved, pageable);
			List<NewsDTO> results = news.stream().map(NewsDTO::from).collect(Collectors.toList());
			return results;
		}
		if (latitude > 0 && longitude > 0 && radius == 0) {
			radius = 10000000;
		}
		int size = pageable.getPageSize();
		int pageNumber = pageable.getPageNumber();
		// get news from Procedure.
		StoredProcedureQuery query = em.createNamedStoredProcedureQuery("geo_dist");
		query.setParameter("orig_latitude", latitude);
		query.setParameter("orig_longitude", longitude);
		query.setParameter("radius", CommonUtils.metersToMiles(radius));
		query.setParameter("paging", (pageNumber * size));
		query.setParameter("size", size);
		query.setParameter("approval", 1);
		query.execute();
		List<Object[]> poiObj = query.getResultList();
		// set distance.
		List<News> poiNews = new ArrayList<>();
		poiObj.stream().forEach(item -> {
			News news = (News) item[0];
			String val = String.valueOf(item[1]);
			double distance = Double.valueOf(val);
			news.setDistance(CommonUtils.milestoMeters(distance));
			poiNews.add(news);
		});

		List<NewsDTO> newsDTO = null;
		// get news from system.
		List<News> systemNews = newsRepository.findByApprovalAndNewsTypeOrderByCreatedAtAsc(approved, 0, pageable);
		// merge news between system and poi.
		Stream<News> results = Stream.concat(systemNews.stream(), poiNews.stream());
		newsDTO = results.map(NewsDTO::from).collect(Collectors.toList());
		return newsDTO;
	}

	/**
	 * Mapping object[] to dto.
	 *
	 * @param newsDTO
	 * @param poiIdList
	 * @param newsObjs
	 * @param isLocation
	 */
	private void processMapping(List<NewsDTO> newsDTO, List<Long> poiIdList, List<Object[]> newsObjs,
			boolean isLocation) {
		for (Object[] obj : newsObjs) {
			NewsDTO dto = new NewsDTO();
			dto.setId(Long.valueOf(String.valueOf(obj[0])));
			dto.setTitle(String.valueOf(obj[1]));
			if (obj[2] != null) {
				String desc = EmojiParser.parseToUnicode(String.valueOf(obj[2]));
				dto.setDesc(desc);
			}
			dto.setThumbnailUrl(String.valueOf(obj[3]));
			dto.setBannerUrl(String.valueOf(obj[4]));
			dto.setType(Integer.valueOf(String.valueOf(obj[5])));
			dto.setRead(Boolean.parseBoolean(String.valueOf(obj[6])));
			if (obj[7] != null) {
				dto.setUserId(String.valueOf(obj[7]));
			}
			if (obj[8] != null) {
				dto.setLatitude(Double.valueOf(String.valueOf(obj[8])));
			}
			if (obj[9] != null) {
				dto.setLongitude(Double.valueOf(String.valueOf(obj[9])));
			}
			if (obj[10] != null) {
				Long poiId = Long.valueOf(String.valueOf(obj[10]));
				dto.setPoiId(poiId);
				poiIdList.add(poiId);
			}
			if (isLocation) {
				if (obj[11] != null) {
					String val = String.valueOf(obj[11]);
					double distance = Double.valueOf(val);
					dto.setDistance(CommonUtils.milestoMeters(distance));
				}
				if (obj.length > 12) { // checking for old version, because it do not have index 12.
					if (obj[12] != null) {
						dto.setLastNotification((Date) obj[12]);
					}
				}
				dto.setDiscount(Integer.valueOf(String.valueOf(obj[13])));
			} else {
				if (obj[11] != null) {
					dto.setLastNotification((Date) obj[11]);
				}
				dto.setDiscount(Integer.valueOf(String.valueOf(obj[12])));
			}
			
			newsDTO.add(dto);
		}
	}

	/**
	 * Get user actions and inject into News data.
	 *
	 * @param news
	 * @param poiIdList
	 * @param userId
	 * @return List of News.
	 */
	private List<NewsDTO> getUserActions(List<NewsDTO> news, List<Long> poiIdList, String userId) {
		List<PoiActionDTO> actions = null;
		if (!StringUtils.isBlank(userId) && !poiIdList.isEmpty()) {
			int n = poiIdList.size();
			Long[] ids = poiIdList.toArray(new Long[n]);
			actions = poiActionRepository.getPoiAction(ids, userId);
		}
		List<NewsDTO> results = new ArrayList<NewsDTO>();
		for (NewsDTO iNews : news) {
			if (iNews.getPoiId() != null && actions != null) {
				for (PoiActionDTO action : actions) {
					if (iNews.getPoiId().equals(action.getPoiId())) {
						JSONObject userAction = new JSONObject();
						switch (action.getStatus()) {
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
						default:
							break;
						}
						iNews.setUserAction(userAction);
					}
				}
			}
			results.add(iNews);
		}
		return results;
	}

	/**
	 * Get news with geo-distance.(BE v0)
	 *
	 * @param latitude
	 * @param longitude
	 * @param radius
	 * @param pageable
	 * @return
	 */
	public List<NewsDTO> getNews_Enhance(String userId, double latitude, double longitude, int radius,
			Pageable pageable) {
		// 1 is approved
		int approved = 1;
		List<Long> poiIdList = new ArrayList<>();
		List<NewsDTO> news = new ArrayList<>();

		// if device do not open location.
		if (latitude == 0 && longitude == 0 && radius == 0) {
			List<Object[]> newsObjs = newsRepository.getNewsByApproval(approved, pageable);
			processMapping(news, poiIdList, newsObjs, false);
			// get user action
			List<NewsDTO> results = getUserActions(news, poiIdList, userId);
			return results;
		}

		// if device open location.
		if (latitude > 0 && longitude > 0 && radius == 0) {
			radius = 10000000;
		}
		int size = pageable.getPageSize();
		int pageNumber = pageable.getPageNumber();
		// get news from Procedure.
		StoredProcedureQuery query = em.createNamedStoredProcedureQuery("geo_dist_new_enhance");
		query.setParameter("orig_latitude", latitude);
		query.setParameter("orig_longitude", longitude);
		query.setParameter("radius", CommonUtils.metersToMiles(radius));
		query.setParameter("paging", (pageNumber * size));
		query.setParameter("size", size);
		query.setParameter("approval", 1);
		query.execute();
		List<Object[]> poiObj = query.getResultList();
		// process mapping
		processMapping(news, poiIdList, poiObj, true);
		// get user action
		List<NewsDTO> poiNews = getUserActions(news, poiIdList, userId);
		// get system news
		List<NewsDTO> systemNews = newsRepository.getByApprovalAndNewsTypeOrderByCreatedAtAsc(approved, 0);
		// merge system news and poi news
		Stream<NewsDTO> resultsStream = Stream.concat(systemNews.stream(), poiNews.stream());
		// return a list
		List<NewsDTO> resutls = resultsStream.collect(Collectors.toList());
		return resutls;
	}

	public List<NewsDTO> getAllNewsOrderByTypeAndCreatedAt() {
		List<News> news = newsRepository.findAllByOrderByNewsTypeAscCreatedAtDesc();
		List<NewsDTO> results = news.stream().map(NewsDTO::from).collect(Collectors.toList());
		return results;
	}

	/**
	 * Get news with geo-distance.(BE v1.1)
	 *
	 * @param userId
	 * @param nearest
	 * @param pageable
	 * @return List of NewsDTO
	 */
	public List<NewsDTO> getNews_Enhance_V11(String userId, StoreProcedureParams nearest) {
		// 1 is approved
		int approved = 1;
		// get system news
		// List<NewsDTO> systemNews =
		// newsRepository.getByApprovalAndNewsTypeOrderByCreatedAtAsc(approved, 0);
		if (!nearest.getCategoryIds().isEmpty()) {
			int size = nearest.getSize();
			int pageNumber = nearest.getPage();
			PageRequest pageRequest = new PageRequest(pageNumber, size);
			List<Long> poiIdList = new ArrayList<>();
			List<NewsDTO> news = new ArrayList<>();
			// if device do not open location.
			if (nearest.getLatitude() == 0 && nearest.getLongitude() == 0) {
				List<Object[]> newsObjs = null;
				if (nearest.getCategoryIds().equalsIgnoreCase(ConstantUtils.ALL)) {
					// -1 is get all news, 1 is get only for news of poi
					newsObjs = newsRepository.getNewsByApproval_V11(1, true, null, approved, pageRequest);
				} else {
					Long[] catIds = CommonUtils.toLongNumber(nearest.getCategoryIds());
					// 1 only get newst type of shop.
					newsObjs = newsRepository.getNewsByApproval_V11(1, false, catIds, approved, pageRequest);
				}
				processMapping(news, poiIdList, newsObjs, false);
				return news;
			}
			
			// if device open location.
			StoredProcedureQuery query = em.createNamedStoredProcedureQuery("geo_dist_new_enhance_v1");
			query.setParameter("orig_latitude", nearest.getLatitude());
			query.setParameter("orig_longitude", nearest.getLongitude());
			query.setParameter("radius", CommonUtils.metersToMiles(nearest.getRadius()));
			query.setParameter("paging", (pageNumber * size));
			query.setParameter("size", size);
			query.setParameter("approval", 1);
			query.setParameter("categoryIds", nearest.getCategoryIds());
			query.execute();
			List<Object[]> poiObj = query.getResultList();
			// process mapping
			processMapping(news, poiIdList, poiObj, true);
			// get user action
			List<NewsDTO> poiNews = getUserActions(news, poiIdList, userId);
			
			// Fixbug Paging
			// // merge system news and poi news
			// if (nearest.getCategoryIds().equalsIgnoreCase(ConstantUtils.ALL)) {
			//   Stream<NewsDTO> resultsStream = poiNews.stream();
			//   // return a list
			//   List<NewsDTO> resutls = resultsStream.skip(pageNumber * size).limit(size).collect(Collectors.toList());
			//   return resutls;
			// }
			return poiNews;
		} else {
			return null;
		}
	}

	/**
	 * Get news by poiId.
	 * 
	 * @param poiId
	 * @param pageable
	 * @return List of NewsDTO
	 */
	public List<NewsDTO> getNewsByPoiId(Long poiId, Pageable pageable) {
		List<NewsDTO> dtos = new ArrayList<>();
		PointOfInterest poi = pointOfInterestRepository.findOne(poiId);
		if (poi != null) {
			List<News> entities = newsRepository.findByPoiOrderByCreatedAtDesc(poi, pageable);
			if (entities != null) {
				dtos = entities.stream().map(NewsDTO::from).collect(Collectors.toList());
			}
		}
		return dtos;
	}

}
