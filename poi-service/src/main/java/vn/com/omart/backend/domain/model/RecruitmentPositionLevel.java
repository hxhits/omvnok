package vn.com.omart.backend.domain.model;

import java.io.Serializable;
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
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "omart_recruitment_position_level")
public class RecruitmentPositionLevel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id", columnDefinition = "int")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "position_id", referencedColumnName = "id", columnDefinition = "int")
	private RecruitmentPosition recruitmentPosition;

	@OneToMany(mappedBy = "recruitmentPositionLevel", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	List<PoiNotification> poiNotifications;

	@Column(name = "name", columnDefinition = "varchar")
	private String name;

	@Column(name = "name_en", columnDefinition = "varchar")
	private String nameEn;

	@Column(name = "is_disabled", columnDefinition = "int")
	private Boolean isDisabled;
	
	@Column(name="title",columnDefinition="varchar")
	private String title;
	
	@Column(name="title_en",columnDefinition="varchar")
	private String titleEn;

}
