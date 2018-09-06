package vn.com.omart.backend.application;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.com.omart.backend.application.response.PoiBODOrderDTO;
import vn.com.omart.backend.application.response.UserBODDTO;
import vn.com.omart.backend.domain.model.UserBOD;
import vn.com.omart.backend.domain.model.UserBODRepository;
import vn.com.omart.sharedkernel.application.model.error.AlreadyExistingException;

@Service
public class UserBODService {

	@Autowired
	private UserBODRepository userBODRepository;

	@Autowired
	private PoiBODService poiBODService;

	@Transactional(readOnly = false)
	public void save(UserBODDTO dto) {
		// checking username is existing
		UserBOD entity = userBODRepository.findByUserName(dto.getUserName());
		if (entity != null) {
			throw new AlreadyExistingException("User name : " + dto.getUserName() + " is existing in system");
		}
		entity = UserBODDTO.toEntity(dto);
		String userId = UUID.randomUUID().toString().replaceAll("-", "");
		entity.setId(userId);
		entity.setPassword(entity.getPassword());
		entity = userBODRepository.save(entity);
		if (!dto.getPoiIdStr().isEmpty()) {
			poiBODService.save(entity, dto.getPoiIdStr());
		}
	}

	public UserBODDTO login(UserBODDTO dto) {
		UserBODDTO userInfo = new UserBODDTO();
		UserBOD entity = userBODRepository.findByUserNameAndPassword(dto.getUserName(), dto.getPassword());
		if (entity != null) {
			userInfo = UserBODDTO.toBasicDTO(entity);
			PageRequest pageable = new PageRequest(0, 20);
			List<PoiBODOrderDTO> poiBODOrders = poiBODService.getPoiBODOrders(entity, pageable);
			if (poiBODOrders != null) {
				userInfo.setPoiBODOrders(poiBODOrders);
			}
			return userInfo;
		}
		return null;
	}

	/**
	 * Get all.
	 * 
	 * @param pageable
	 * @return List of UserBODDTO
	 */
	public List<UserBODDTO> getAll(Pageable pageable) {
		List<UserBOD> entities = userBODRepository.findAll(pageable).getContent();
		if (!entities.isEmpty()) {
			List<UserBODDTO> dtos = entities.stream().map(UserBODDTO::toBasicDTO).collect(Collectors.toList());
			return dtos;
		}
		return new ArrayList<>();
	}

}
