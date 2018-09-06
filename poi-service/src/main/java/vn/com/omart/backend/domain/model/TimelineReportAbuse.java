package vn.com.omart.backend.domain.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.vdurmont.emoji.EmojiParser;

@Entity
@Table(name = "omart_timeline_report_abuse")
public class TimelineReportAbuse implements Serializable {

	private static final long serialVersionUID = 3015085058655527035L;

	@Id
	@Column(name = "id", columnDefinition = "int")
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name = "timeline_id", referencedColumnName = "id", columnDefinition = "int")
	private Timeline timeline;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_from", referencedColumnName = "id", columnDefinition = "int")
	private UserProfile userFrom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_to", referencedColumnName = "id", columnDefinition = "int")
	private UserProfile userTo;

	@Column(name = "reason", columnDefinition = "varchar")
	private String reason;

	@Column(name = "report_type", columnDefinition = "int")
	private int reportType;
	
	@Column(name = "created_at", columnDefinition = "timestamp")
	private Date createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timeline getTimeline() {
		return timeline;
	}

	public void setTimeline(Timeline timeline) {
		this.timeline = timeline;
	}

	public UserProfile getUserFrom() {
		return userFrom;
	}

	public void setUserFrom(UserProfile userFrom) {
		this.userFrom = userFrom;
	}

	public UserProfile getUserTo() {
		return userTo;
	}

	public void setUserTo(UserProfile userTo) {
		this.userTo = userTo;
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

	public int getReportType() {
		return reportType;
	}

	public void setReportType(int reportType) {
		this.reportType = reportType;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}
