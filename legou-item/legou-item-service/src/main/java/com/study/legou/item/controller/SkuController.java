package com.study.legou.item.controller;

import com.legou.core.controller.BaseController;
import com.study.legou.item.po.Sku;
import com.study.legou.item.service.SkuService;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title:
 */
@RestController
@RequestMapping(value = "/item/sku")
@CrossOrigin
public class SkuController extends BaseController<SkuService, Sku> {

    @ApiOperation(value="查询spu对应的sku", notes="根据spuId查询sku集合")
    @GetMapping("/select-skus-by-spuid/{id}")
    public List<Sku> selectSkusBySpuId(@PathVariable("id") Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        return service.list(sku);
    }


}