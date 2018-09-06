package vn.com.omart.messenger.domain.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "omart_call_status")
public class CallStatus implements Serializable {

	private static final long serialVersionUID = -8813311256632010236L;

	@Id
	@Column(name = "id", columnDefinition = "int")
	@GeneratedValue
	private Long id;

	@Column(name = "call_sid", columnDefinition = "varchar")
	private String callSid;

	@Column(name = "parent_call_sid", columnDefinition = "varchar")
	private String parentCallSid;

	@Column(name = "status", columnDefinition = "int")
	private int status;

	@Column(name = "created_at", columnDefinition = "timestamp")
	private Date createdAt;

	@Column(name = "sender_id", columnDefinition = "varchar")
	private String from;

	@Column(name = "recipient_id", columnDefinition = "varchar")
	private String to;

	@Column(name = "call_duration", columnDefinition = "int")
	private int callDuration;

	@Column(name = "completed_at", columnDefinition = "timestamp")
	private Date completedAt;

	@Column(name = "started_at", columnDefinition = "timestamp")
	private Date startedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCallSid() {
		return callSid;
	}

	public void setCallSid(String callSid) {
		this.callSid = callSid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public int getCallDuration() {
		return callDuration;
	}

	public void setCallDuration(int callDuration) {
		this.callDuration = callDuration;
	}

	public Date getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(Date completedAt) {
		this.completedAt = completedAt;
	}

	public String getParentCallSid() {
		return parentCallSid;
	}

	public void setParentCallSid(String parentCallSid) {
		this.parentCallSid = parentCallSid;
	}

	public Date getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(Date startedAt) {
		this.startedAt = startedAt;
	}

}
