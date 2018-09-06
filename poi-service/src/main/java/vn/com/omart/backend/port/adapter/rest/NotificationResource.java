package vn.com.omart.backend.port.adapter.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.com.omart.backend.application.NotificationService;
import vn.com.omart.backend.application.response.NotificationDTO;
import vn.com.omart.backend.domain.model.EmptyJsonResponse;

/**
 * Omart admin send notification to an user/group user.
 * 
 * @author ADMIN
 *
 */
@Controller
@RequestMapping("/v1/notification")
public class NotificationResource {

	@Autowired
	private NotificationService notificationService;

	/**
	 * Create notification.
	 * 
	 * @param userId
	 * @param dto
	 * @return HttpStatus
	 */
	@PostMapping(value = "")
	public ResponseEntity<EmptyJsonResponse> save(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestBody(required = true) NotificationDTO dto) {
		notificationService.save(userId, dto);
		return new ResponseEntity<EmptyJsonResponse>(new EmptyJsonResponse(), HttpStatus.CREATED);
	}

	/**
	 * Get notification
	 * 
	 * @param id
	 * @return NotificationDTO
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<NotificationDTO> getNotification(@PathVariable(required = true, value = "id") Long id) {
		NotificationDTO dto = notificationService.getNotification(id);
		return new ResponseEntity<NotificationDTO>(dto, HttpStatus.OK);
	}

	/**
	 * Get all notification.
	 * 
	 * @param pageable
	 * @return List of NotificationDTO
	 */
	@GetMapping(value = "/all")
	public ResponseEntity<List<NotificationDTO>> getAllNotification(
			@RequestHeader(value="X-User-Id",required=false,defaultValue="") String userId,
			@PageableDefault(page = 0, size = 20, direction = Direction.DESC, sort = "createdAt") Pageable pageable) {
		List<NotificationDTO> dtos = notificationService.getAllNotification(userId,pageable);
		return new ResponseEntity<List<NotificationDTO>>(dtos, HttpStatus.OK);
	}

}
