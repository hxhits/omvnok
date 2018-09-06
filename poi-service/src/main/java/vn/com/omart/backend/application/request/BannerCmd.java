package vn.com.omart.backend.application.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BannerCmd {

    @Data
    @ToString
    public static class CreateOrUpdate {
        private String imageUrl;
    }
}
