package vn.com.omart.backend.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.omart.backend.application.response.DashboardDTO;
import vn.com.omart.backend.application.response.QuotaByProvinceDTO;
import vn.com.omart.backend.application.response.QuotaDTO;
import vn.com.omart.backend.application.response.TotalUserDTO;
import vn.com.omart.backend.domain.model.DashboardRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DashboardService {

    private final DashboardRepository dashboardRepository;

    @Autowired
    public DashboardService(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

//    public List<Quota> getListQuota(String userId) {
//        return this.dashboardRepository.listQuota(userId)
//            .stream()
//            .map(new Quota.QueryMapper()::map)
//            .collect(Collectors.toList());
////        log.debug("asd");
//    }

    public DashboardDTO getDashboardQuota() {

        List<QuotaDTO> quotas = this.dashboardRepository.getQuotaByDayAndProvince()
            .stream().map(new QuotaDTO.QueryMapper()::map)
            .collect(Collectors.toList());

        List<QuotaByProvinceDTO> quotasByProvince = this.dashboardRepository.getTotalByProvince()
            .stream().map(new QuotaByProvinceDTO.QueryMapper()::map)
            .collect(Collectors.toList());

        Long totalShop = this.dashboardRepository.totalShop();
        Long totalSalesman = this.dashboardRepository.totalSalesman();

        List<TotalUserDTO> totalActivatedUsers = this.dashboardRepository.getTotalActivatedUserByTitle()
            .stream().map(new TotalUserDTO.QueryMapper()::map)
            .collect(Collectors.toList());

        List<TotalUserDTO> totalUsers = this.dashboardRepository.getTotalUserByTitle()
            .stream().map(new TotalUserDTO.QueryMapper()::map)
            .collect(Collectors.toList());

        // TODO: Remove quotas after deploy new
        return new DashboardDTO(totalShop, totalSalesman, quotas, quotasByProvince, totalActivatedUsers, totalUsers);
    }

}
