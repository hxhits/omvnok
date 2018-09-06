package vn.com.omart.backend.application.request;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

@Data
@ToString
public class CategoryCmd {

    @Data
    @ToString
    public static class CreateOrUpdate {
        @NotBlank
        private String name;
        
        private String keywords;

        private String imageUrl;

        private Long parentId;

        private String description;

        private String titleColor;

        private String backgroundColor;

        private Long order;
    }

    @Data
    @ToString
    public static class GroupCreateOrUpdate {

        @NotBlank
        private String name;
        
        private String keywords;

        private String description;

        private Long order;

    }

    @Data
    @ToString
    public static class CreateOrUpdateNew {

        @NotBlank
        private Long parentId;

        @NotBlank
        private String name;
        private String keywords;
        private String imageUrl;

        private String titleColor;

        private String backgroundColor;

        private Long order;

    }

    @Data
    @ToString
    public static class Update{

        @NotBlank
        private String verifyCode;

        @NotBlank
        private String name;
        
        private String keywords;
        
        @NotBlank
        private String imageUrl;
    }

}
