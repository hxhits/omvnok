package vn.com.omart.backend.application.response;

import lombok.Data;
import vn.com.omart.backend.domain.model.Owner;
import vn.com.omart.backend.port.adapter.userprofile.UserResponse;
import vn.com.omart.sharedkernel.application.model.dto.EntityMapper;

@Data
public class POIOwnerDTO {

    private String id;
    private String name;
    private String avatar;
    private String phoneNumber;
    private int remainingTrialDays;
    private boolean isPaid;
    private boolean isNew;
    private String categoriesSelectedStr;
    private String provinceDistrictSelectedStr;
    private String recruitmentPositionLevelsSelectedStr;
    private String recruitmentProvinceDistrictSelectedStr;
    private Long poiId;
    
    public static POIOwnerDTO from(Owner model) {
        POIOwnerDTO dto = new POIOwnerDTO();

//        dto.id = model.id();
        dto.id = model.userId();
        dto.name = model.name();
        dto.avatar = model.avatar();
        dto.phoneNumber = model.phoneNumber();

        return dto;
    }

    public static class Mapper implements EntityMapper<POIOwnerDTO, UserResponse> {

        @Override
        public POIOwnerDTO map(UserResponse entity) {
            POIOwnerDTO dto = new POIOwnerDTO();

            dto.id = entity.getId();
            dto.name = entity.getFirstname();
            dto.avatar = entity.getAvatar();
            dto.phoneNumber = entity.getPhoneNumber();

            return dto;
        }

        @Override
        public void map(UserResponse entity, POIOwnerDTO dto) {

        }
    }

}
