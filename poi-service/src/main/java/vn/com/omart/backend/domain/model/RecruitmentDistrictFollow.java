package vn.com.omart.backend.domain.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "omart_recruitment_district_follow")
public class RecruitmentDistrictFollow implements Serializable{

	private static final long serialVersionUID = 0L;

	@Id
	@GeneratedValue
	@Column(name = "id", columnDefinition = "int")
	private Long id;

	@Column(name = "user_id", columnDefinition = "varchar")
	private String userId;

	@Column(name = "province", columnDefinition = "int")
	private Long provinceId;

	@Column(name = "district", columnDefinition = "int")
	private Long districtId;

}