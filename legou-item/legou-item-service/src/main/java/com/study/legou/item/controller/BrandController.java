package com.study.legou.item.controller;

import com.legou.core.controller.BaseController;
import com.study.legou.item.po.Brand;
import com.study.legou.item.po.Category;
import com.study.legou.item.service.BrandService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/item/brand")
@CrossOrigin
public class BrandController extends BaseController<BrandService, Brand> {

    @Override
    public void afterEdit(Brand domain) {
        //生成角色列表, 如：1,3,4
        List<Category> categories = service.selectCategoryByBrand(domain.getId());
        Long[] ids = new Long[categories.size()];
        for (int i=0; i< categories.size(); i++) {
            ids[i] = categories.get(i).getId();
        }
        domain.setCategoryIds(ids);
    }

}
