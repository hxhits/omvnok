package vn.com.omart.backend.domain.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "omart_user_cv")
public class UserCv implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id", columnDefinition = "int")
	private Long id;

	@Column(name = "user_id", columnDefinition = "varchar")
	private String userId;

	@Column(name = "id_card_number", columnDefinition = "varchar")
	private String idCardNumber;

	@Column(name = "id_card_date", columnDefinition = "timestamp")
	private Date idCardDate;

	@Column(name = "id_card_place", columnDefinition = "varchar")
	private String idCardPlace;

	@Column(name = "fullname", columnDefinition = "varchar")
	private String fullname;

	@Column(name = "image_url", columnDefinition = "text")
	private String imageUrl;

	@Column(name = "date_of_birth", columnDefinition = "timestamp")
	private Date dateOfBirth;

	@Column(name = "sex", columnDefinition = "bit")
	private int sex;

	@Column(name = "phone_number", columnDefinition = "varchar")
	private String phoneNumber;

	@Column(name = "email", columnDefinition = "varchar")
	private String email;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "province", columnDefinition = "int")
	private Province province;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "district", columnDefinition = "int")
	private District district;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ward", columnDefinition = "int")
	private Ward ward;

	@Column(name = "street", columnDefinition = "varchar")
	private String street;

	@Column(name = "experience_level", columnDefinition = "int")
	private int experienceLevel;

	@Column(name = "education_level", columnDefinition = "int")
	private int educationLevel;

	@Column(name = "education_name", columnDefinition = "varchar")
	private String educationName;

	@Column(name = "language", columnDefinition = "int")
	private int language;

	@Column(name = "description", columnDefinition = "text")
	private String description;

	@Column(name = "created_at", columnDefinition = "timestamp")
	private Date createdAt;

	@Column(name = "updated_at", columnDefinition = "timestamp")
	private Date updatedAt;

	public String getAddress() {
		return (this.getStreet() == null ? "" : this.getStreet() + ", ")
				+ (this.getWard() == null ? "" : this.getWard().name() + ", ")
				+ (this.getDistrict() == null ? "" : this.getDistrict().name() + ", ")
				+ (this.getProvince() == null ? "" : this.getProvince().name());
	}

}
