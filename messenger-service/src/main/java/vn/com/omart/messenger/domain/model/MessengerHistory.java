package vn.com.omart.messenger.domain.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

@Entity
@Table(name = "messenger_history")
@ToString
@Builder
@AllArgsConstructor
public class MessengerHistory {

  @Id
  @Column(name = "id", columnDefinition = "int")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "sender", columnDefinition = "varchar")
  private String sender;

  @Column(name = "recipient", columnDefinition = "varchar")
  private String recipient;

  @Column(name = "timestamp", columnDefinition = "TIMESTAMP")
  private Date timestamp;

  @Column(name = "type", columnDefinition = "varchar")
  private String type;

  @Column(name = "content", columnDefinition = "varchar")
  private String content;

  @Column(name = "shop_id", columnDefinition = "int")
  private Long shopId;

  @Column(name = "sender_deleted", columnDefinition = "bit")
  private boolean senderDeleted;

  @Column(name = "recipient_deleted", columnDefinition = "bit")
  private boolean recipientDeleted;

  // GETTERS

  public String sender() {
    return sender;
  }

  public String recipient() {
    return recipient;
  }

  public Date timestamp() {
    return timestamp;
  }

  public String content() {
    return content;
  }

  public MessengerHistory() {
    super();
  }
}
