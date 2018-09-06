package vn.com.omart.backend.port.adapter.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.backend.application.MagazineService;
import vn.com.omart.backend.application.response.MagazineDTO;
import vn.com.omart.backend.application.response.RssDTO;
import vn.com.omart.backend.constants.Device;

@RestController
@RequestMapping("/v1")
public class MagazineResource {

	@Autowired
	private MagazineService magazineService;

	/**
	 * Create a magazine.
	 * 
	 * @param userId
	 * @param MagazineDTO
	 * @return
	 */
	@RequestMapping(value = "/magazine", method = RequestMethod.POST)
	public ResponseEntity<MagazineDTO> createMagazine(
			@RequestHeader(value = "X-User-Id", required = false) String userId,
			@RequestHeader(value = "client", required = false, defaultValue = "mobile") Device device,
			@RequestBody(required = true) MagazineDTO request) {
		MagazineDTO entity = magazineService.createMagazine(userId, request, device);
		return new ResponseEntity<MagazineDTO>(entity, HttpStatus.CREATED);
	}

	/**
	 * Get magazines.
	 * 
	 * @param userId
	 * @param latitude
	 * @param longitude
	 * @param radius
	 * 
	 * @param pageable
	 * @return List of MagazineDTO
	 */
	@RequestMapping(value = "/magazine", method = RequestMethod.GET)
	public ResponseEntity<List<MagazineDTO>> getMagazines_huong(
			@RequestHeader(value = "X-User-Id", required = false) String userId,
			@RequestParam(value = "catId", required = false, defaultValue = "-1") Long catId,
			@PageableDefault(size = 10, page = 0) Pageable pageable) {
		return new ResponseEntity<List<MagazineDTO>>(
				magazineService.getMagazine(userId, catId, pageable), HttpStatus.OK);
	}

	/**
	 * Get RSS.
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/rss", method = RequestMethod.POST)
	public ResponseEntity<RssDTO> getRSS(
			@RequestHeader(value = "X-User-Id", required = false) String userId,
			@RequestHeader(value = "client", required = false, defaultValue = "mobile") Device device,
			@RequestBody(required = true) RssDTO request) {
		RssDTO entity = magazineService.getRSS(userId, request, device);
		return new ResponseEntity<RssDTO>(entity, HttpStatus.CREATED);
	}

}
