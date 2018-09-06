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
@Table(name = "omart_user_cv_notification")
public class UserCvNotification implements Serializable {

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

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "poi_id", columnDefinition = "int")
	private PointOfInterest poi;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "apply_id", columnDefinition = "int")
	private RecruitmentApply apply;

	@Column(name = "title", columnDefinition = "text")
	private String title;

	@Column(name = "content", columnDefinition = "text")
	private String content;

	@Column(name = "type", columnDefinition = "int")
	private Long type;

	@Column(name = "created_by", columnDefinition = "varchar")
	private String createdBy;

	@Column(name = "created_at", columnDefinition = "timestamp")
	private Date createdAt;

	@Column(name = "updated_by", columnDefinition = "varchar")
	private String updatedBy;

	@Column(name = "updated_at", columnDefinition = "timestamp")
	private Date updatedAt;

	@Column(name = "is_deleted", columnDefinition = "int")
	private boolean isDeleted;

	@Column(name = "status", columnDefinition = "int")
	private int status;

	@Column(name = "notification_type", columnDefinition = "int")
	private int notificationType;
	
	@Column(name = "result", columnDefinition = "int")
	private int result;

}
