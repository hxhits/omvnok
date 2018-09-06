package vn.com.omart.backend.port.adapter.rest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.omart.backend.application.CategoryService;
import vn.com.omart.backend.application.response.CategoryDTO;
import vn.com.omart.sharedkernel.application.model.error.UnauthorizedAccessException;

import java.util.List;

@RestController
@RequestMapping("/v1/apps/sales")
@Slf4j
public class SalesPOIResource {

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = {"/subcategories"})
    public List<CategoryDTO> getSubCategory(
        @RequestHeader(value = "X-User-Id") String userId
    ) {
        if (StringUtils.isBlank(userId)) {
            throw new UnauthorizedAccessException("Missing user-id");
        }

        return categoryService.getSubCategories();
    }

    @GetMapping(value = {"/subcategories"}, headers = { "X-API-Version=1.1" })
    public List<CategoryDTO> getSubCategory_V1(
        @RequestHeader(value = "X-User-Id") String userId
    ) {
        if (StringUtils.isBlank(userId)) {
            throw new UnauthorizedAccessException("Missing user-id");
        }

        return categoryService.getSubCategories_v1();
    }

}
