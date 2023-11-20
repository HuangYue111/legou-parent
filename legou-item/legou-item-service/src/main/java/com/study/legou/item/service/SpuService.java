package com.study.legou.item.service;

import com.legou.core.service.ICrudService;
import com.study.legou.item.po.Spu;

public interface SpuService extends ICrudService<Spu> {

    /**
     * 保存spu，包括如下表的数据
     *      spu
     *      spuDetail
     *      skus
     * @param spu
     */
    public void saveSpu(Spu spu);

}