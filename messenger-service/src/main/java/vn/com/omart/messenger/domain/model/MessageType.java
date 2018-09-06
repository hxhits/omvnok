package vn.com.omart.messenger.domain.model;

import java.util.Arrays;


public enum MessageType {

    TEXT("TEXT"),
    IMAGE("IMAGE");

    private String value;

    MessageType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static MessageType from(String text) {
        try {
            return MessageType.valueOf(text.toUpperCase());
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new IllegalArgumentException("'" + (text == null ? "null" : text) +
                "' is not a valid Message type. Valid values are " + Arrays.asList(MessageType.values()));
        }
    }
}

