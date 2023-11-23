package com.study.legou.item.controller;


import java.util.List;

import com.legou.core.controller.BaseController;
import com.study.legou.item.po.SpecParam;
import com.study.legou.item.service.SpecParamService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/item/param")
@CrossOrigin
public class SpecParamController extends BaseController<SpecParamService, SpecParam> {
    @ApiOperation(value="查询", notes="根据实体条件查询参数")
    @PostMapping(value = "/select-param-by-entity")
    public List<SpecParam> selectSpecParamApi(@RequestBody SpecParam entity) {
        return service.list(entity);
    }


}