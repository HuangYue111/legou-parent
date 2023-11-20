package com.study.legou.item.dao;

import com.legou.core.dao.ICrudDao;
import com.study.legou.item.po.Brand;
import com.study.legou.item.po.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BrandDao extends ICrudDao<Brand> {
    List<Category> selectCategoryByBrand(Long id);

    void deleteCategoryByBrand(Long id);

    void insertCategoryAndBrand(@Param("categoryId") Long roleId, @Param("brandId") Long id);
}
