package com.study.legou.item.controller;

import com.legou.core.controller.BaseController;
import com.legou.core.po.ResponseBean;
import com.study.legou.item.po.Spu;
import com.study.legou.item.service.SpuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title:
 */
@RestController
@RequestMapping(value = "/item/spu")
@CrossOrigin
public class SpuController extends BaseController<SpuService, Spu> {

    @ApiOperation(value="保存商品信息", notes="保存商品信息")
    @PostMapping("/save-spu")
    public ResponseBean saveSpu(@RequestBody Spu spu) throws Exception {
        ResponseBean rm = new ResponseBean();
        try {
            service.saveSpu(spu);
        } catch (Exception e) {
            e.printStackTrace();
            rm.setSuccess(false);
            rm.setMsg("保存失败");
        }
        return rm;
    }

    @ApiOperation(value="查询所有", notes="查询所有spu")
    @GetMapping("/list-all")
    public List<Spu> selectAll() {
        return service.list(new Spu());
    }


}