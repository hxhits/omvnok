package vn.com.omart.backend.port.adapter.rest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;
import vn.com.omart.backend.application.AppCommonService;
import vn.com.omart.backend.application.PoiPictureService;
import vn.com.omart.backend.application.response.DistrictDTO;
import vn.com.omart.backend.application.response.ImageDTO;
import vn.com.omart.backend.application.response.ProvinceDTO;
import vn.com.omart.backend.application.response.WardDTO;
import vn.com.omart.backend.port.adapter.thumbor.ThumborService;
import vn.com.omart.sharedkernel.StringUtils;
import vn.com.omart.sharedkernel.application.model.error.ApplicationException;

@RestController
@RequestMapping("/v1/app")
@Slf4j
public class AppCommonResource {

  @Autowired
  private ThumborService thumborService;

  @Autowired
  private AppCommonService appService;

  @Autowired
  private PoiPictureService poiPictureService;

  @PostMapping(value = {"/image"}, produces = {"image/*", MediaType.APPLICATION_JSON_UTF8_VALUE})
  public ImageDTO attach(@RequestParam(value = "filename", required = false, defaultValue = "omart") String imgName, @RequestBody byte[] bytes) {
    return new ImageDTO(thumborService.uploadImage(imgName, bytes));
  }

  @PostMapping(value = "/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ImageDTO attachMultiPart(@RequestParam(value = "filename", required = false, defaultValue = "omart") String imgName, @RequestParam("file") MultipartFile file) {
    if ("omart".equals(imgName)) {
      imgName = file.getOriginalFilename();
    }
    try {
        imgName = file.getOriginalFilename().replaceAll(" ", "");
        imgName = StringUtils.removeAccent(imgName);
      return new ImageDTO(thumborService.uploadImage(imgName, file.getBytes()));
    } catch (IOException e) {
      throw new ApplicationException("Cannot get file from MultiPart form");
    }
  }

  @GetMapping(value = {"/provinces"})
  public List<ProvinceDTO> getProvinces() {
    return this.appService.getProvinces();
  }

  @GetMapping(value = {"/provinces/{provinceId}/districts"})
  public List<DistrictDTO> getDistricts(@PathVariable(value = "provinceId") Long provinceId) {
    return this.appService.getDistricts(provinceId);
  }

  @GetMapping(value = {"/districts/{districtId}/wards"})
  public List<WardDTO> getWards(@PathVariable(value = "districtId") Long districtId) {
    return this.appService.getWards(districtId);
  }
}
