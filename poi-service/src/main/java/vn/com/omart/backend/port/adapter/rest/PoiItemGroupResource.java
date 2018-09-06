package vn.com.omart.backend.port.adapter.rest;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.com.omart.backend.application.ItemGroupService;
import vn.com.omart.backend.application.PoiItemGroupService;
import vn.com.omart.backend.application.response.ItemDTO;
import vn.com.omart.backend.application.response.PoiItemGroupDTO;
import vn.com.omart.backend.domain.model.EmptyJsonResponse;

@RestController
@RequestMapping("/v1/item-group")
public class PoiItemGroupResource {

	@Autowired
	private PoiItemGroupService poiItemGroupService;

	@Autowired
	private ItemGroupService itemGroupService;

	/**
	 * Create group.
	 * 
	 * @param userId
	 * @param dto
	 * @return HttpStatus
	 */
	@PostMapping(value = "")
	public ResponseEntity<EmptyJsonResponse> create(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@Valid @RequestBody(required = true) PoiItemGroupDTO dto) {
		poiItemGroupService.save(dto);
		return new ResponseEntity<EmptyJsonResponse>(HttpStatus.CREATED);
	}

	/**
	 * Update group.
	 * 
	 * @param userId
	 * @param id
	 * @param dto
	 * @return EmptyJsonResponse
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<EmptyJsonResponse> update(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "id", required = true) Long id, @RequestBody(required = true) PoiItemGroupDTO dto) {
		poiItemGroupService.update(id, dto);
		return new ResponseEntity<EmptyJsonResponse>(HttpStatus.OK);
	}

	/**
	 * Delete
	 * 
	 * @param userId
	 * @param id
	 * @return EmptyJsonResponse
	 */
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<EmptyJsonResponse> delete(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@PathVariable(value = "id", required = true) Long id) {
		poiItemGroupService.delete(id);
		return new ResponseEntity<EmptyJsonResponse>(HttpStatus.OK);
	}

	/**
	 * Get Groups by poiId.
	 * 
	 * @param userId
	 * @param poiId
	 * @param pageable
	 * @return List of PoiItemGroupDTO
	 */
	@GetMapping(value = "/poiId/{poi-id}")
	public ResponseEntity<List<PoiItemGroupDTO>> getGroupsByPoiId(
			@RequestHeader(value = "X-User-Id", required = false) String userId,
			@PathVariable(value = "poi-id", required = true) Long poiId,
			@PageableDefault(page = 0, size = Integer.MAX_VALUE) Pageable pageable) {
		List<PoiItemGroupDTO> dtos = poiItemGroupService.getGroupsByPoiId(poiId, pageable);
		return new ResponseEntity<List<PoiItemGroupDTO>>(dtos, HttpStatus.OK);
	}

	/**
	 * Get Item by group and poi.
	 * 
	 * @param userId
	 * @param poiId
	 * @param groupId
	 * @param pageable
	 * @return List of Item
	 */
	@GetMapping(value = "/{id}/poiId/{poi-id}/items")
	public ResponseEntity<List<ItemDTO>> getItemsByGroupAndPoi(
			@RequestHeader(value = "X-User-Id", required = false) String userId,
			@PathVariable(value = "poi-id", required = true) Long poiId,
			@PathVariable(value = "id", required = true) Long groupId,
			@PageableDefault(page = 0, size = Integer.MAX_VALUE) Pageable pageable) {
		List<ItemDTO> dtos = itemGroupService.getItems(poiId, groupId, pageable);
		return new ResponseEntity<List<ItemDTO>>(dtos, HttpStatus.OK);
	}

}
