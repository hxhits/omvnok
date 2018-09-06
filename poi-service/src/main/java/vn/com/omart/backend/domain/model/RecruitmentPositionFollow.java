package vn.com.omart.backend.domain.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "omart_recruitment_position_follow")
public class RecruitmentPositionFollow implements Serializable {

	private static final long serialVersionUID = 0L;

	@Id
	@GeneratedValue
	@Column(name = "id", columnDefinition = "int")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "position_id", referencedColumnName = "id", columnDefinition = "int")
	private RecruitmentPositionLevel recruitmentPosition;

	@Column(name = "user_id", columnDefinition = "varchar")
	private String userId;

}