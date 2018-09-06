package vn.com.omart.backend.application.request;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

@Data
@ToString
public class ItemCmd {

    @Data
    @ToString
    public static class CreateOrUpdate {

        @NotBlank
        private String name;

        private String description;

        private double unitPrice;
        private boolean isOutOfStock;

        private List<ImageCmd> avatarImage;
        private List<ImageCmd> coverImage;
        private List<ImageCmd> featuredImage;
        private List<Long> groupIds;
    }

    @Data
    @ToString
    public static class ImageCmd {
        private String url;
    }

}
