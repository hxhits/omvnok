package vn.com.omart.messenger.application;

import lombok.Data;

@Data
public class TextPayload extends Payload {
    private String text;

    public TextPayload(String text) {
        this.text = text;
    }
}
