package vn.com.omart.backend.domain.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "omart_recruitment_apply")
public class RecruitmentApply implements Serializable {

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

	@ManyToOne
	@JoinColumn(name = "recruitment_id", referencedColumnName = "id", columnDefinition = "int")
	private Recruitment recruitment;

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

	@Column(name = "salary_expected", columnDefinition = "varchar")
	private String salaryExpected;

	@Column(name = "description", columnDefinition = "text")
	private String description;

	@Column(name = "created_at", columnDefinition = "timestamp")
	private Date createdAt;

	@Column(name = "updated_at", columnDefinition = "timestamp")
	private Date updatedAt;

	@Column(name = "is_deleted", columnDefinition = "bit")
	private boolean isDeleted;

	@Column(name = "status", columnDefinition = "int")
	private int status;

	public String getAddress() {
		return (this.getStreet() == null ? "" : this.getStreet() + ", ")
				+ (this.getWard() == null ? "" : this.getWard().name() + ", ")
				+ (this.getDistrict() == null ? "" : this.getDistrict().name() + ", ")
				+ (this.getProvince() == null ? "" : this.getProvince().name());
	}

}
