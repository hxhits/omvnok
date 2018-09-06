package vn.com.omart.driver.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.vdurmont.emoji.EmojiParser;

import vn.com.omart.driver.support.JpaImageConverter;

@Entity
@Table(name = "omart_driver_info")
public class DriverInfo implements Serializable {

	private static final long serialVersionUID = 1457877207309139626L;

	@Id
	@Column(name = "user_id", columnDefinition = "varchar")
	private String userId;

	@Column(name = "full_name", columnDefinition = "varchar")
	private String fullName;

	@Column(name = "avatar", columnDefinition = "varchar")
	private String avatar;

	@Column(name = "date_of_birth", columnDefinition = "timestamp")
	private Date dateOfBirth;

	@Column(name = "identity_card", columnDefinition = "varchar")
	private String identityCard;

	@Column(name = "number_plate", columnDefinition = "varchar")
	private String numberPlate;

	@Column(name = "date_of_registration", columnDefinition = "timestamp")
	private Date dateOfRegistration;

	@Column(name = "phone_number", columnDefinition = "varchar")
	private String phoneNumber;

	@Column(name = "model", columnDefinition = "varchar")
	private String model;

	@Column(name = "is_blocked", columnDefinition = "bit")
	private boolean isBlocked;

	@Column(name = "rate_number", columnDefinition = "float")
	private float rateNumber;

	@Column(name = "total_star", columnDefinition = "int")
	private int totalStar;

	@Column(name = "contract_id", columnDefinition = "varchar")
	private String contractId;

	@Column(name = "account_type", columnDefinition = "int")
	private int accountType;

	@Column(name = "car_image", columnDefinition = "json")
	@Convert(converter = JpaImageConverter.class)
	private List<Image> carImages;

	@Column(name = "driver_licence_image", columnDefinition = "json")
	@Convert(converter = JpaImageConverter.class)
	private List<Image> driverLicenceImages;

	@Column(name = "identity_card_image", columnDefinition = "json")
	@Convert(converter = JpaImageConverter.class)
	private List<Image> identityCardImages;

	@Column(name = "reason", columnDefinition = "text")
	private String reason;

	@ManyToOne
	@JoinColumn(name = "active_province", referencedColumnName = "id", columnDefinition = "int")
	private Province province;

	@ManyToOne
	@JoinColumn(name = "car_type_id", referencedColumnName = "id", columnDefinition = "int")
	private CarType carType;

	@OneToOne(mappedBy = "driverInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	private Setting setting;

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

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public CarType getCarType() {
		return carType;
	}

	public void setCarType(CarType carType) {
		this.carType = carType;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Setting getSetting() {
		return setting;
	}

	public void setSetting(Setting setting) {
		this.setting = setting;
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
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

	public float getRateNumber() {
		return rateNumber;
	}

	public void setRateNumber(float rateNumber) {
		this.rateNumber = rateNumber;
	}

	public int getTotalStar() {
		return totalStar;
	}

	public void setTotalStar(int totalStar) {
		this.totalStar = totalStar;
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

	public String getReason() {
		if (this.reason != null) {
			return EmojiParser.parseToUnicode(reason);
		}
		return null;
	}

	public void setReason(String reason) {
		if (reason != null) {
			this.reason = EmojiParser.parseToAliases(reason);
		}
	}

}