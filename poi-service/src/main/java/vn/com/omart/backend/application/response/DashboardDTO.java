package vn.com.omart.backend.application.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DashboardDTO {
    private Long totalShop;
    private Long totalSalesman;

    private List<QuotaDTO> quotas;
    private List<QuotaByProvinceDTO> quotasByProvince;

    private List<TotalUserDTO> totalActivatedUsers;
    private List<TotalUserDTO> totalUsers;
}
