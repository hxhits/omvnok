package vn.com.omart.driver.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vn.com.omart.driver.common.constant.DriverType.AccountType;
import vn.com.omart.driver.common.constant.DriverType.LockType;
import vn.com.omart.driver.entity.DriverInfo;
import vn.com.omart.driver.entity.Image;

public class DriverInfoDTO {

	private String userId;
	private String fullName;
	private String avatar;
	private Date dateOfBirth;
	private String identityCard;
	private String numberPlate;
	private Date dateOfRegistration;
	private Long provinceId;
	private Long carTypeId;
	private String password;
	private String phoneNumber;
	private String model;
	private SettingDTO setting;
	private List<Image> carImages;
	private List<Image> driverLicenceImages;
	private List<Image> identityCardImages;
	private float rateNumber;
	private String contractId;
	private int accountType;
	private boolean isBlocked;
	private String reason;
	private LockType action;

	private static List<Image> getImages(List<Image> image) {
		if (image == null) {
			return new ArrayList<>();
		} else {
			return image;
		}
	}

	public static DriverInfo toEntity(DriverInfoDTO dto) {
		DriverInfo entity = new DriverInfo();
		entity.setAvatar(dto.getAvatar());
		entity.setDateOfBirth(dto.getDateOfBirth());
		entity.setFullName(dto.getFullName());
		entity.setIdentityCard(dto.getIdentityCard());
		entity.setNumberPlate(dto.getNumberPlate());
		entity.setModel(dto.getModel());
		entity.setCarImages(getImages(dto.getCarImages()));
		entity.setDriverLicenceImages(getImages(dto.getDriverLicenceImages()));
		entity.setIdentityCardImages(getImages(dto.getIdentityCardImages()));
		entity.setAccountType(AccountType.GENERAL.getId());
		return entity;
	}

	public static DriverInfo toEntity(DriverInfo entity, DriverInfoDTO dto) {
		entity.setAvatar(dto.getAvatar());
		entity.setDateOfBirth(dto.getDateOfBirth());
		entity.setFullName(dto.getFullName());
		entity.setIdentityCard(dto.getIdentityCard());
		entity.setNumberPlate(dto.getNumberPlate());
		entity.setModel(dto.getModel());
		entity.setCarImages(getImages(dto.getCarImages()));
		entity.setDriverLicenceImages(getImages(dto.getDriverLicenceImages()));
		entity.setIdentityCardImages(getImages(dto.getIdentityCardImages()));
		return entity;
	}

	public static DriverInfo toBasicEntity(DriverInfo entity, DriverInfoDTO dto) {
		entity.setFullName(dto.getFullName());
		entity.setNumberPlate(dto.getNumberPlate());
		entity.setModel(dto.getModel());
		return entity;
	}

	public static DriverInfoDTO toDTO(DriverInfo entity) {
		DriverInfoDTO dto = new DriverInfoDTO();
		dto.setAvatar(entity.getAvatar());
		dto.setFullName(entity.getFullName());
		dto.setIdentityCard(entity.getIdentityCard());
		dto.setCarTypeId(entity.getCarType().getId());
		dto.setDateOfBirth(entity.getDateOfBirth());
		dto.setDateOfRegistration(entity.getDateOfRegistration());
		dto.setNumberPlate(entity.getNumberPlate());
		dto.setProvinceId(entity.getProvince().getId());
		dto.setUserId(entity.getUserId());
		dto.setPhoneNumber(entity.getPhoneNumber());
		dto.setModel(entity.getModel());
		dto.setCarImages(getImages(entity.getCarImages()));
		dto.setDriverLicenceImages(getImages(entity.getDriverLicenceImages()));
		dto.setIdentityCardImages(getImages(entity.getIdentityCardImages()));
		dto.setRateNumber(entity.getRateNumber());
		dto.setAccountType(entity.getAccountType());
		dto.setContractId(entity.getContractId());
		dto.setBlocked(entity.isBlocked());
		if (entity.getReason() != null) {
			dto.setReason(entity.getReason());
		}
		return dto;
	}

	public float getRateNumber() {
		return rateNumber;
	}

	public void setRateNumber(float rateNumber) {
		this.rateNumber = rateNumber;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public String getNumberPlate() {
		return numberPlate;
	}

	public void setNumberPlate(String numberPlate) {
		this.numberPlate = numberPlate;
	}

	public Date getDateOfRegistration() {
		return dateOfRegistration;
	}

	public void setDateOfRegistration(Date dateOfRegistration) {
		this.dateOfRegistration = dateOfRegistration;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public Long getCarTypeId() {
		return carTypeId;
	}

	public void setCarTypeId(Long carTypeId) {
		this.carTypeId = carTypeId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public SettingDTO getSetting() {
		return setting;
	}

	public void setSetting(SettingDTO setting) {
		this.setting = setting;
	}

	public List<Image> getCarImages() {
		return carImages;
	}

	public void setCarImages(List<Image> carImages) {
		this.carImages = carImages;
	}

	public List<Image> getDriverLicenceImages() {
		return driverLicenceImages;
	}

	public void setDriverLicenceImages(List<Image> driverLicenceImages) {
		this.driverLicenceImages = driverLicenceImages;
	}

	public List<Image> getIdentityCardImages() {
		return identityCardImages;
	}

	public void setIdentityCardImages(List<Image> identityCardImages) {
		this.identityCardImages = identityCardImages;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public int getAccountType() {
		return accountType;
	}

	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public LockType getAction() {
		return action;
	}

	public void setAction(LockType action) {
		this.action = action;
	}

}
