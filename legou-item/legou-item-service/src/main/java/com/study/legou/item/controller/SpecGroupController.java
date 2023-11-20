package com.study.legou.item.controller;


import com.legou.core.controller.BaseController;
import com.legou.core.po.ResponseBean;
import com.study.legou.item.po.SpecGroup;
import com.study.legou.item.service.SpecGroupService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item/group")
@CrossOrigin
public class SpecGroupController extends BaseController<SpecGroupService, SpecGroup> {
    @ApiOperation(value="保存规格参数", notes="保存规格参数")
    @PostMapping("/save-group")
    public ResponseBean saveGroup(@RequestBody List<SpecGroup> specGroup) throws Exception {
        ResponseBean rm = new ResponseBean();
        try {
            if (specGroup != null && specGroup.size() > 0) {
                service.saveGroup(specGroup.get(0).getCid(), specGroup);
            }
        } catch (Exception e) {
            e.printStackTrace();
            rm.setSuccess(false);
            rm.setMsg("保存失败");
        }
        return rm;
    }
}
