package com.study.legou.item.service;


import com.legou.core.service.ICrudService;
import com.study.legou.item.po.Brand;
import com.study.legou.item.po.Category;

import java.util.List;

public interface BrandService extends ICrudService<Brand> {
    /**
     * 根据商品id查询分类
     * @param id
     * @return
     */
    List<Category> selectCategoryByBrand(Long id);
}
