package vn.com.omart.backend.domain.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
@Table(name = "omart_recruitment")
public class Recruitment implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "id", columnDefinition = "int")
	private Long id;

	@OneToOne
	@JoinColumn(name = "position_id", referencedColumnName = "id", columnDefinition = "int")
	private RecruitmentPosition recruitmentPosition;

	@OneToOne
	@JoinColumn(name = "position_level_id", referencedColumnName = "id", columnDefinition = "int", nullable=true)
	private RecruitmentPositionLevel recruitmentPositionLevel;

	@OneToMany(mappedBy = "recruitment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	List<RecruitmentApply> recruitmentApplies;

	@OneToMany(mappedBy = "recruit", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	List<PoiNotification> poiNotifications;

	@Column(name = "salary", columnDefinition = "varchar")
	private String salary;

	@Column(name = "work_location", columnDefinition = "varchar")
	private String workLocation;

	@Column(name = "education_level", columnDefinition = "varchar")
	private String educationLevel;

	@Column(name = "experience_level", columnDefinition = "varchar")
	private String experienceLevel;

	@Column(name = "quantity", columnDefinition = "int")
	private int quantity;

	@Column(name = "created_at", columnDefinition = "timestamp")
	private Date createdAt;

	@Column(name = "updated_at", columnDefinition = "timestamp")
	private Date updatedAt;

	@Temporal(TemporalType.DATE)
	@Column(name = "expired_at", columnDefinition = "timestamp")
	private Date expiredAt;

	@Column(name = "description", columnDefinition = "text")
	private String description;

	@Column(name = "requirement", columnDefinition = "text")
	private String requirement;

	@Column(name = "benefit", columnDefinition = "text")
	private String benefit;

	@Column(name = "contact_info", columnDefinition = "varchar")
	private String contactInfo;

	@Column(name = "view_count", columnDefinition = "int")
	private int viewCount;

	@Column(name = "position_type", columnDefinition = "varchar")
	private String positionType;

	@Column(name = "state", columnDefinition = "int")
	private int state;

	@Column(name = "is_deleted", columnDefinition = "bit")
	private boolean isDeleted;

	@Column(name = "title", columnDefinition = "varchar")
	private String title;

	@ManyToOne
	private PointOfInterest poi;

	public List<RecruitmentApply> getRecruitmentApply(boolean isDeleted) {
		return recruitmentApplies.stream().filter(re -> re.isDeleted() == isDeleted).collect(Collectors.toList());
	}

}
