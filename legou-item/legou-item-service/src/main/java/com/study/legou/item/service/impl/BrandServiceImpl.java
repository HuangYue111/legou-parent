package com.study.legou.item.service.impl;

import com.legou.core.service.impl.CrudServiceImpl;
import com.study.legou.item.dao.BrandDao;
import com.study.legou.item.po.Brand;
import com.study.legou.item.po.Category;
import com.study.legou.item.service.BrandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class BrandServiceImpl extends CrudServiceImpl<Brand> implements BrandService {
    @Override
    @Transactional(readOnly = false)
    public boolean saveOrUpdate(Brand entity) {
        boolean result = super.saveOrUpdate(entity);

        ((BrandDao) getBaseMapper()).deleteCategoryByBrand(entity.getId()); //删除商品和分类的关联

        //添加商品和分类的关联
        Long[] roleIds = entity.getCategoryIds();
        if (null != roleIds) {
            for (Long roleId : roleIds) {
                ((BrandDao) getBaseMapper()).insertCategoryAndBrand(roleId, entity.getId());
            }
        }
        return result;

    }

    @Override
    public List<Category> selectCategoryByBrand(Long id) {
        return ((BrandDao) getBaseMapper()).selectCategoryByBrand(id);
    }
}
