package vn.com.omart.backend.port.adapter.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.backend.application.DelivererService;
import vn.com.omart.backend.application.response.DelivererDTO;
import vn.com.omart.backend.domain.model.EmptyJsonResponse;

@RestController
@RequestMapping("/v1/deliverer")
public class DelivererResource {

	@Autowired
	private DelivererService delivererService;

	/**
	 * Shop add deliverer.
	 * 
	 * @param userId
	 * @param dto
	 * @return EmptyJsonResponse
	 */
	@PostMapping(value = "")
	public ResponseEntity<DelivererDTO> save(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@Valid @RequestBody(required = true) DelivererDTO dto) {
		DelivererDTO out = delivererService.save(dto);
		return new ResponseEntity<DelivererDTO>(out, HttpStatus.CREATED);
	}

	/**
	 * Get deliverers.
	 * 
	 * @param userId
	 * @param poiId
	 * @return List<DelivererDTO>
	 */
	@GetMapping(value = "/{poiId}")
	public ResponseEntity<List<DelivererDTO>> get(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "poiId", required = true) long poiId) {
		List<DelivererDTO> dtos = delivererService.get(poiId);
		return new ResponseEntity<List<DelivererDTO>>(dtos, HttpStatus.OK);
	}

	/**
	 * Delete deliverer.
	 * 
	 * @param userId
	 * @param id
	 * @return EmptyJsonResponse
	 */
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<EmptyJsonResponse> delete(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "id", required = true) long id) {
		delivererService.delete(id);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.OK);
	}
}
