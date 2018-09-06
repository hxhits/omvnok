package vn.com.omart.backend.domain.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "push_notification_token_new")
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PushNotificationTokenNew {
	
	@EmbeddedId
	private PushNotificationTokenId id;

	@Column(name = "client", columnDefinition = "int")
	private int client;

	
	public PushNotificationTokenId getId() {
		return id;
	}

	public void setId(PushNotificationTokenId id) {
		this.id = id;
	}

	public int getClient() {
		return client;
	}

	public void setClient(int client) {
		this.client = client;
	}

}
