package vn.com.omart.backend.port.adapter.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import vn.com.omart.backend.application.NewsService;
import vn.com.omart.backend.application.request.NewsCmd;
import vn.com.omart.backend.application.response.StoreProcedureParams;
import vn.com.omart.backend.application.response.NewsDTO;
import vn.com.omart.backend.domain.model.EmptyJsonResponse;
import vn.com.omart.backend.port.adapter.thumbor.ThumborService;
import vn.com.omart.sharedkernel.application.model.error.UnauthorizedAccessException;

@RestController
@RequestMapping("/v1")
@Slf4j
public class NewsResource {

	@Value("${thumbor.proxy}")
	private String thumborProxy;

	@Autowired
	private NewsService newsService;

	@Autowired
	private ThumborService thumborService;

	@PostMapping(value = "/newswithimage", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public NewsDTO createNewsWithImages(@RequestHeader(value = "X-User-Id") String userId,
			@RequestParam(value = "title") String title, @RequestParam(value = "desc") String desc,
			@RequestParam(value = "type", required = false, defaultValue = "0") int type,
			@RequestParam(value = "read", required = false, defaultValue = "false") boolean read,
			@RequestParam("thumbnail") MultipartFile thumbnail, @RequestParam("banner") MultipartFile banner) {

		String thumbnailUrl = thumborService.uploadImage(thumbnail);
		String bannerUrl = thumborService.uploadImage(banner);

		NewsCmd.CreateNew payload = new NewsCmd.CreateNew();
		payload.setTitle(title);

		payload.setBannerUrl(bannerUrl);
		payload.setThumbnailUrl(thumbnailUrl);
		payload.setDesc(desc);

		return newsService.createNews(userId, payload);
	}

	@PostMapping(value = { "/news" }, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public NewsDTO createNews(@RequestHeader(value = "X-User-Id") String userId,
			@RequestBody @Valid NewsCmd.CreateNew payload) throws IOException {

		if (StringUtils.isBlank(userId)) {
			throw new UnauthorizedAccessException("Missing user-id");
		}
		log.debug("payload={}", payload);

		String thumbnailUrl = payload.getThumbnailUrl();

		// If image does not hot on our thumbor server, then upload it to our thumbor
		// server.
		if (StringUtils.isNotEmpty(thumbnailUrl) && !thumbnailUrl.contains(thumborProxy)) {
			String thumborThumbnailUrl = thumborService.uploadImage(null, thumbnailUrl);
			payload.setThumbnailUrl(thumborThumbnailUrl);
		}

		String bannerUrl = payload.getBannerUrl();

		// If image does not hot on our thumbor server, then upload it to our thumbor
		// server.
		if (StringUtils.isNotEmpty(bannerUrl) && !bannerUrl.contains(thumborProxy)) {
			String thumborBannerUrl = thumborService.uploadImage(null, bannerUrl);
			payload.setBannerUrl(thumborBannerUrl);
		}

		return newsService.createNews(userId, payload);
	}

	// @PreAuthorize("hasRole(T(vn.com.omart.backend.ROLES).FUNC_CATEGORY_GET_DETAIL.role())")
	@GetMapping(value = { "/news/{newsId}" })
	public NewsDTO getNews(@PathVariable(value = "newsId") String newsId) {

		return this.newsService.getNews(Long.parseLong(newsId));
	}

	// @PreAuthorize("hasRole(T(vn.com.omart.backend.ROLES).FUNC_CATEGORY_GET_DETAIL.role())")
	@GetMapping(value = { "/news" })
	public List<NewsDTO> searchNews() {

		return this.newsService.getAllNews();
	}

	// @PreAuthorize("hasRole(T(vn.com.omart.backend.ROLES).FUNC_CATEGORY_UPDATE.role())")
	@PutMapping(value = { "/news/{newsId}" })
	public NewsDTO updateNews(@RequestHeader(value = "X-User-Id") String userId,
			@PathVariable(value = "newsId") String newsId, @RequestBody @Valid NewsCmd.CreateOrUpdate payload) {

		if (StringUtils.isBlank(userId)) {
			throw new UnauthorizedAccessException("Missing user-id");
		}

		log.debug("payload={}", payload);

		return this.newsService.updateNews(userId, Long.parseLong(newsId), payload);
	}

	// @PreAuthorize("hasRole(T(vn.com.omart.backend.ROLES).FUNC_CATEGORY_UPDATE.role())")
	@DeleteMapping(value = { "/news/{newsId}" })
	public void deleteNews(@RequestHeader(value = "X-User-Id") String userId,
			@PathVariable(value = "newsId") String newsId) {

		if (StringUtils.isBlank(userId)) {
			throw new UnauthorizedAccessException("Missing user-id");
		}

		log.debug("newsId={}", newsId);

		this.newsService.deleteNews(Long.parseLong(newsId));
	}

	/**
	 * Save Advertising.
	 *
	 * @param userId
	 * @param news
	 * @param poiId
	 * @return EmptyJsonResponse
	 */
	@RequestMapping(value = "/news/{poi-id}/advertisement", method = RequestMethod.POST)
	public ResponseEntity<EmptyJsonResponse> saveAdvertising(@RequestHeader(value = "X-User-Id") String userId,
			@RequestBody(required = true) NewsDTO news, @PathVariable(value = "poi-id", required = true) Long poiId) {
		newsService.saveAdvertising(userId, poiId, news);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.CREATED);
	}

	/**
	 * Get news List.
	 *
	 * @param latlng
	 * @param radius
	 * @param pageable
	 * @return List of
	 */
	@RequestMapping(value = "/news/", method = RequestMethod.GET)
	public ResponseEntity<List<NewsDTO>> getNews(@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude,
			@RequestParam(value = "radius", required = true) int radius,
			@PageableDefault(size = 400, page = 0) Pageable pageable) {
		List<NewsDTO> news = newsService.getNews(latitude, longitude, radius, pageable);
		if (news != null) {
			return new ResponseEntity<List<NewsDTO>>(news, HttpStatus.OK);
		}
		return new ResponseEntity<List<NewsDTO>>(new ArrayList<NewsDTO>(), HttpStatus.OK);
	}

	/**
	 * Get news List.
	 *
	 * @param latlng
	 * @param radius
	 * @param pageable
	 * @return List of
	 */
	@RequestMapping(value = "/news1/", method = RequestMethod.GET)
	public ResponseEntity<List<NewsDTO>> getNewsEnhance(
			@RequestHeader(value = "X-User-Id", required = false) String userId,
			@RequestParam(value = "latitude", required = false) Double latitude,
			@RequestParam(value = "longitude", required = false) Double longitude,
			@RequestParam(value = "radius", required = true) int radius,
			@PageableDefault(size = 400, page = 0) Pageable pageable) {
		List<NewsDTO> news = newsService.getNews_Enhance(userId, latitude, longitude, radius, pageable);
		if (news != null) {
			return new ResponseEntity<List<NewsDTO>>(news, HttpStatus.OK);
		}
		return new ResponseEntity<List<NewsDTO>>(new ArrayList<NewsDTO>(), HttpStatus.OK);
	}

	/**
	 * Get news List filter by category id. Note pageable in request body.
	 * 
	 * @param userId
	 * @param nearest
	 * @param pageable
	 * @return
	 */
	@RequestMapping(value = "/news", method = RequestMethod.POST, headers = { "X-API-Version=1.1" })
	public ResponseEntity<List<NewsDTO>> getNewsEnhance_V11(
			@RequestHeader(value = "X-User-Id", required = false) String userId,
			@RequestBody(required = true) StoreProcedureParams nearest) {
		List<NewsDTO> news = newsService.getNews_Enhance_V11(userId, nearest);
		if (news != null) {
			return new ResponseEntity<List<NewsDTO>>(news, HttpStatus.OK);
		}
		return new ResponseEntity<List<NewsDTO>>(new ArrayList<NewsDTO>(), HttpStatus.OK);
	}

	@RequestMapping(value = "/news/all", method = RequestMethod.GET)
	public ResponseEntity<List<NewsDTO>> getAllNews(
			@RequestHeader(value = "X-User-Id", required = false) String userId) {
		List<NewsDTO> news = newsService.getAllNewsOrderByTypeAndCreatedAt();
		if (news != null) {
			return new ResponseEntity<List<NewsDTO>>(news, HttpStatus.OK);
		}
		return new ResponseEntity<List<NewsDTO>>(new ArrayList<NewsDTO>(), HttpStatus.OK);
	}

	/**
	 * Get news by poiId.
	 * 
	 * @param poiId
	 * @param pageable
	 * @return List of news
	 */
	@GetMapping(value = "/news/poid/{poiId}")
	public ResponseEntity<List<NewsDTO>> getNewsByPoiId(@PathVariable(value = "poiId", required = true) Long poiId,
			@PageableDefault(page = 0, size = Integer.MAX_VALUE) Pageable pageable) {
		List<NewsDTO> dtos = newsService.getNewsByPoiId(poiId, pageable);
		return new ResponseEntity<List<NewsDTO>>(dtos, HttpStatus.OK);
	}
}
