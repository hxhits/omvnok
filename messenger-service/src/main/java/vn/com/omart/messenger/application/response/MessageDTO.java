package vn.com.omart.messenger.application.response;

import lombok.Data;
import vn.com.omart.messenger.application.ImagePayload;
import vn.com.omart.messenger.application.TextPayload;
import vn.com.omart.messenger.port.adapter.rest.MessengerResource;
import vn.com.omart.sharedkernel.application.model.dto.EntityMapper;

@Data
public class MessageDTO {

	private String senderId;
	private String recipientId;
	private String shopId;
	private Long timestamp;
	private String senderName;
	private Object payload;
	private String channelSID;

	public static class HistoryMapper implements EntityMapper<MessageDTO, MessengerResource.History> {

		@Override
		public MessageDTO map(MessengerResource.History history) {
			MessageDTO dto = new MessageDTO();
			dto.setSenderId(history.getSender());
			dto.setRecipientId(history.getRecipient());
			dto.setTimestamp(history.getTimestamp());

			Integer strShopId = history.getShopId();
			if (strShopId != null) {
				dto.setShopId(String.valueOf(strShopId));
			} else {
				dto.setShopId("");
			}

			switch (history.getType()) {
			case TEXT:
				dto.setPayload(new TextPayload(history.getContent()));
				break;
			case IMAGE:
				dto.setPayload(new ImagePayload(history.getContent()));
				break;
			}

			return dto;
		}

		@Override
		public void map(MessengerResource.History entity, MessageDTO dto) {

		}
	}
}
