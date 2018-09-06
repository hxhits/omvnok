package vn.com.omart.backend.application.response;

import lombok.Data;
import vn.com.omart.sharedkernel.application.model.dto.EntityMapper;

@Data
public class TotalUserDTO {

    private String title;
    private Long total;

    public static class QueryMapper implements EntityMapper<TotalUserDTO, Object[]> {

        @Override
        public TotalUserDTO map(Object[] entity) {
            TotalUserDTO dto = new TotalUserDTO();

            dto.setTitle((String) entity[0]);
            dto.setTotal(Long.valueOf(String.valueOf(entity[1])));

            return dto;
        }

        @Override
        public void map(Object[] entity, TotalUserDTO dto) {

        }
    }
}
