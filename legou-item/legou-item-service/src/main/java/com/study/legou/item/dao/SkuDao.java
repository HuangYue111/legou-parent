package com.study.legou.item.dao;


import com.legou.core.dao.ICrudDao;
import com.study.legou.item.po.Sku;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Title: Sku DAO类
 */
public interface SkuDao extends ICrudDao<Sku> {

    @Select("select * from sku_ where spu_id_ = #{skuId}")
    public List<Sku> findBySkuId(Integer skuId);


}