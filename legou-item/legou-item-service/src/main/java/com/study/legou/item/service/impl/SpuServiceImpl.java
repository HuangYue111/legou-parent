package com.study.legou.item.service.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.legou.core.service.impl.CrudServiceImpl;
import com.study.legou.item.po.Sku;
import com.study.legou.item.po.Spu;
import com.study.legou.item.service.SkuService;
import com.study.legou.item.service.SpuDetailService;
import com.study.legou.item.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SpuServiceImpl extends CrudServiceImpl<Spu> implements SpuService {

    @Autowired
    private SpuDetailService spuDetailService;

    @Autowired
    private SkuService skuService;

    @Override
    @Transactional
    public void saveSpu(Spu spu) {
        //保存spu
        this.saveOrUpdate(spu);
        //保存spuDetail
        if (null == spu.getSpuDetail().getId()) {
            spu.getSpuDetail().setId(spu.getId());
            spuDetailService.save(spu.getSpuDetail());
        } else {
            spuDetailService.updateById(spu.getSpuDetail());
        }
        //保存sku
        //删除spu的所有sku
        skuService.remove(Wrappers.<Sku>query().eq("spu_id_", spu.getId()));
        //添加新的sku
        for (Sku sku : spu.getSkus()) {
            sku.setSpuId(spu.getId());
            skuService.save(sku);
        }
    }

}