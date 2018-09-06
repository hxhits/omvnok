package vn.com.omart.driver.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vn.com.omart.driver.common.constant.CommonConstant;
import vn.com.omart.driver.dto.CarTypeDTO;
import vn.com.omart.driver.dto.DriverInfoDTO;
import vn.com.omart.driver.dto.ImageDTO;
import vn.com.omart.driver.dto.InitialCarDTO;
import vn.com.omart.driver.entity.DriverInfo;
import vn.com.omart.driver.service.CarTypeService;
import vn.com.omart.driver.service.DriverInfoService;
import vn.com.omart.driver.service.ServiceUtil;
import vn.com.omart.driver.service.ThumborService;

@RestController
@RequestMapping("/v1/driver")
public class InitialDriverController {

	@Autowired
	private CarTypeService carTypeService;

	@Autowired
	private ServiceUtil serviceUtil;

	@Autowired
	private DriverInfoService driverInfoService;

	@Autowired
	private ThumborService thumborService;

	/**
	 * Initial
	 * 
	 * @param userId
	 * @param language
	 * @return InitialCarDTO
	 */
	@GetMapping(value = "/initial")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public InitialCarDTO initial(@RequestHeader(value = "X-User-Id", required = false, defaultValue = "") String userId,
			@RequestHeader(value = "lang", required = false, defaultValue = "vi") String language) {
		InitialCarDTO dto = carTypeService.initial(language, userId);
		return dto;
	}

	/**
	 * Get car type.
	 * 
	 * @param language
	 * @return
	 */
	@GetMapping(value = "/initial/car-types")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<CarTypeDTO> getCarType(
			@RequestHeader(value = "lang", required = false, defaultValue = "vi") String language) {
		List<CarTypeDTO> dtos = carTypeService.getCarTypes(language);
		return dtos;
	}

	/**
	 * Get driver profile.
	 * 
	 * @param userId
	 * @return DriverInfoDTO
	 */
	@GetMapping(value = "/profile")
	public ResponseEntity<DriverInfoDTO> getUserProfile(@RequestHeader(value = "X-User-Id", required = true) String userId) {
		DriverInfoDTO userProfile = driverInfoService.getDriverProfile(userId);
		if (userProfile != null) {
			return new ResponseEntity<DriverInfoDTO>(userProfile, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * Create info.
	 * 
	 * @param dto
	 * @return EmptyJsonResponse
	 */
	@ResponseBody
	@PostMapping(value = "/create-info")
	public ResponseEntity<Void> saveDriverInfo(@RequestBody(required = true) DriverInfoDTO dto) {
		driverInfoService.save(dto);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	
	/**
	 * Update info.
	 * 
	 * @param dto
	 * @return EmptyJsonResponse
	 */
	@ResponseBody
	@PutMapping(value = "/update-info")
	public ResponseEntity<Void> updateDriverInfo(@RequestHeader(value = "X-User-Id", required = true) String userId,
			@RequestBody(required = true) DriverInfoDTO dto) {
		driverInfoService.update(userId, dto);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Checking existing by phone.
	 * 
	 * @param phone
	 * @return
	 */
	@ResponseBody
	@GetMapping(value = "/phone/{phone}/existing")
	public ResponseEntity<Void> isDriverExisting(@PathVariable(required = true, value = "phone") String phone) {
		boolean isExisting = driverInfoService.isDriverExistingByPhoneNumber(phone);
		if (isExisting) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * Upload picture to thumbor Server.
	 *
	 * @param imgName
	 * @param file
	 * @return ImageDTO.
	 * @throws IOException
	 */
	@ResponseBody
	@PostMapping(value = "/picture-upload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ImageDTO attachMultiPart(
			@RequestParam(value = "filename", required = false, defaultValue = "driver") String imgName,
			@RequestParam("file") MultipartFile file) throws IOException {
		if (CommonConstant.DRIVER.equals(imgName)) {
			imgName = file.getOriginalFilename();
		}
		return new ImageDTO(thumborService.uploadImage(imgName, file.getBytes()));
	}
	
	@GetMapping(value = "/update-contract-id")
	@ResponseStatus(HttpStatus.OK)
	public void initial() {
		driverInfoService.updateContractId();
	}

}
