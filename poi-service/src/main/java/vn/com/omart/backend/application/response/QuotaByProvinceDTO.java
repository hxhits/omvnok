package vn.com.omart.backend.application.response;

import lombok.Data;
import vn.com.omart.sharedkernel.application.model.dto.EntityMapper;

@Data
public class QuotaByProvinceDTO {

    private Integer provinceId;
    private String provinceName;
    private Long totalAll;
    private Long totalInDay;

    public static class QueryMapper implements EntityMapper<QuotaByProvinceDTO, Object[]> {

        @Override
        public QuotaByProvinceDTO map(Object[] entity) {
            QuotaByProvinceDTO dto = new QuotaByProvinceDTO();


            dto.setProvinceId((Integer) entity[0]);
            dto.setProvinceName((String) entity[1]);
            dto.setTotalAll(Long.valueOf(String.valueOf(entity[2])));
            dto.setTotalInDay(Long.valueOf(String.valueOf(entity[3])));

            return dto;
        }

        @Override
        public void map(Object[] entity, QuotaByProvinceDTO dto) {

        }
    }
}
