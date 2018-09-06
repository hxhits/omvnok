package vn.com.omart.messenger.application;

import lombok.Data;

@Data
public class ImagePayload extends Payload {
    private String image;

    public ImagePayload(String image) {
        this.image = image;
    }
}
