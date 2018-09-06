package vn.com.omart.backend.application.response;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import vn.com.omart.backend.application.util.DateUtils;
import vn.com.omart.backend.domain.model.UserBOD;

public class UserBODDTO {

	private String id;
	@NotEmpty
	private String bodName;
	@NotEmpty
	private String userName;
	@NotEmpty
	private String password;
	private Long createdAt;
	private Long updatedAt;
	@NotNull
	private String poiIdStr;
	
	private List<PoiBODOrderDTO> poiBODOrders;

	public static UserBOD toEntity(UserBODDTO dto) {
		UserBOD entity = new UserBOD();
		entity.setBodName(dto.getBodName());
		entity.setPassword(dto.getPassword());
		entity.setUserName(dto.getUserName());
		entity.setCreatedAt(DateUtils.getCurrentDate());
		entity.setUpdatedAt(DateUtils.getCurrentDate());
		return entity;
	}

	public static UserBODDTO toBasicDTO(UserBOD entity) {
		UserBODDTO dto = new UserBODDTO();
		dto.setBodName(entity.getBodName());
		dto.setUserName(entity.getUserName());
		dto.setId(entity.getId());
		dto.setCreatedAt(entity.getCreatedAt().getTime());
		return dto;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBodName() {
		return bodName;
	}

	public void setBodName(String bodName) {
		this.bodName = bodName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getPoiIdStr() {
		return poiIdStr;
	}

	public void setPoiIdStr(String poiIdStr) {
		this.poiIdStr = poiIdStr;
	}

	public List<PoiBODOrderDTO> getPoiBODOrders() {
		return poiBODOrders;
	}

	public void setPoiBODOrders(List<PoiBODOrderDTO> poiBODOrders) {
		this.poiBODOrders = poiBODOrders;
	}

}
