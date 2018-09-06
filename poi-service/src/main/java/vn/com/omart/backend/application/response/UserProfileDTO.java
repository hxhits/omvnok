package vn.com.omart.backend.application.response;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

import vn.com.omart.backend.constants.OmartType.Commons;
import vn.com.omart.backend.constants.OmartType.UserFriendRequestState;
import vn.com.omart.backend.domain.model.UserProfile;

public class UserProfileDTO {

	private Long id;
	private String userId;
	@NotNull
	private String name;
	private String avatar;
	private String cover;
	private Long dateOfBirth;
	private ProvinceDTO province;
	private Long provinceId;
	@NotNull
	private String phone;
	private int sex;
	private String password;
	
	public Long poiId = 0L;
	public String categoriesSelectedStr = "";
	public String provinceDistrictSelectedStr = "";

	public String recruitmentPositionLevelsSelectedStr = "";
	public String recruitmentProvinceDistrictSelectedStr = "";

	public int remainingTrialDays;
	public boolean paid;
	private int omartCoin;
	private Commons friendState;
	
	public static UserProfileDTO toBasicDTO(UserProfile entity) {
		UserProfileDTO dto = new UserProfileDTO();
		dto.setId(entity.getId());
		if (entity.getAvatar() != null) {
			dto.setAvatar(entity.getAvatar());
		}
		dto.setName(entity.getName());
		dto.setPhone(entity.getPhone());
		dto.setUserId(entity.getUserId());
		return dto;
	}
	
	public static UserProfileDTO toDTO(UserProfile entity) {
		UserProfileDTO dto = new UserProfileDTO();
		dto.setId(entity.getId());
		if (entity.getAvatar() != null) {
			dto.setAvatar(entity.getAvatar());
		}
		if (entity.getCover() != null) {
			dto.setCover(entity.getCover());
		}
		if (entity.getDateOfBirth() != null) {
			dto.setDateOfBirth(entity.getDateOfBirth().getTime());
		}
		dto.setName(entity.getName());
		dto.setPhone(entity.getPhone());
		if (entity.getProvince() != null) {
			if(entity.getProvince().name() == null) {
				
			} else {
				dto.setProvince(ProvinceDTO.from(entity.getProvince()));
			}
		}
		dto.setSex(entity.getSex());
		dto.setUserId(entity.getUserId());
		dto.setOmartCoin(entity.getCoin());
		return dto;
	}

	public static UserProfile toEntity(UserProfileDTO dto) {
		UserProfile entity = new UserProfile();
		if (StringUtils.isNotBlank(dto.getAvatar())) {
			entity.setAvatar(dto.getAvatar());
		}
		if (StringUtils.isNotBlank(dto.getCover())) {
			entity.setCover(dto.getCover());
		}
		if (dto.getDateOfBirth() != null) {
			entity.setDateOfBirth(new Date(dto.getDateOfBirth()));
		}
		entity.setName(dto.getName());
		entity.setPhone(dto.getPhone());
		entity.setSex(dto.getSex());
		entity.setUserId(dto.getUserId());
		entity.setCoin(0);
		return entity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public Long getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Long dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public ProvinceDTO getProvince() {
		return province;
	}

	public void setProvince(ProvinceDTO province) {
		this.province = province;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getPoiId() {
		return poiId;
	}

	public void setPoiId(Long poiId) {
		this.poiId = poiId;
	}

	public String getCategoriesSelectedStr() {
		return categoriesSelectedStr;
	}

	public void setCategoriesSelectedStr(String categoriesSelectedStr) {
		this.categoriesSelectedStr = categoriesSelectedStr;
	}

	public String getProvinceDistrictSelectedStr() {
		return provinceDistrictSelectedStr;
	}

	public void setProvinceDistrictSelectedStr(String provinceDistrictSelectedStr) {
		this.provinceDistrictSelectedStr = provinceDistrictSelectedStr;
	}

	public String getRecruitmentPositionLevelsSelectedStr() {
		return recruitmentPositionLevelsSelectedStr;
	}

	public void setRecruitmentPositionLevelsSelectedStr(String recruitmentPositionLevelsSelectedStr) {
		this.recruitmentPositionLevelsSelectedStr = recruitmentPositionLevelsSelectedStr;
	}

	public String getRecruitmentProvinceDistrictSelectedStr() {
		return recruitmentProvinceDistrictSelectedStr;
	}

	public void setRecruitmentProvinceDistrictSelectedStr(String recruitmentProvinceDistrictSelectedStr) {
		this.recruitmentProvinceDistrictSelectedStr = recruitmentProvinceDistrictSelectedStr;
	}

	public int getRemainingTrialDays() {
		return remainingTrialDays;
	}

	public void setRemainingTrialDays(int remainingTrialDays) {
		this.remainingTrialDays = remainingTrialDays;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public int getOmartCoin() {
		return omartCoin;
	}

	public void setOmartCoin(int omartCoin) {
		this.omartCoin = omartCoin;
	}

	public Commons getFriendState() {
		return friendState;
	}

	public void setFriendState(Commons friendState) {
		this.friendState = friendState;
	}
	
}
