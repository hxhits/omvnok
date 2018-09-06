package vn.com.omart.backend.domain.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "omart_recruitment_position")
public class RecruitmentPosition implements Serializable {

	// Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id", columnDefinition = "int")
	private Long id;

	@OneToMany(mappedBy = "recruitmentPosition", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	List<RecruitmentPositionLevel> recruitmentPositionLevels;

	@Column(name = "name", columnDefinition = "varchar")
	private String name;

	@Column(name = "name_en", columnDefinition = "varchar")
	private String nameEn;

	@Column(name = "order", columnDefinition = "int")
	private Long order;

	@Column(name = "is_disabled", columnDefinition = "int")
	private Boolean isDisabled;
	
	@Column(name = "image", columnDefinition = "varchar")
	private String image;

}
