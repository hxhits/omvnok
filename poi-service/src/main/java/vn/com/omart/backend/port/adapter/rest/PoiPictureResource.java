package vn.com.omart.backend.port.adapter.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.backend.application.PoiPictureService;

@RestController
@RequestMapping("/v1/pois/picture")
public class PoiPictureResource {

//	@Autowired
//	private PoiPictureService poiPictureService;
//	@GetMapping(value = "/convert")
//	public ResponseEntity<Void> convertImageUrl() {
//		poiPictureService.convertImageUrl();
//		return new ResponseEntity<Void>(HttpStatus.OK);
//	}
}
