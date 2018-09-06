package vn.com.omart.messenger.application.request;

import lombok.Data;

@Data
public class MessageCMD {

    private String recipientId;
    private Long timestamp;
    private Object payload;
    private String shopId;

}