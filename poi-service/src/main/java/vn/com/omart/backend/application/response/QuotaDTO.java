package vn.com.omart.backend.application.response;

import lombok.Data;
import vn.com.omart.sharedkernel.application.model.dto.EntityMapper;

@Data
public class QuotaDTO {

    private String userId;
    private String name;
    private String username;
    private Integer provinceId;
    private String provinceName;
    private Long actuallyQuantity;

    public static class QueryMapper implements EntityMapper<QuotaDTO, Object[]> {

        @Override
        public QuotaDTO map(Object[] entity) {
            QuotaDTO dto = new QuotaDTO();

            dto.setUserId((String) entity[0]);
            dto.setName((String) entity[1]);
            dto.setUsername((String) entity[2]);
            dto.setProvinceId((Integer) entity[3]);
            dto.setProvinceName((String) entity[4]);
            dto.setActuallyQuantity(Long.valueOf(String.valueOf(entity[5])));

            return dto;
        }

        @Override
        public void map(Object[] entity, QuotaDTO dto) {

        }
    }
}
