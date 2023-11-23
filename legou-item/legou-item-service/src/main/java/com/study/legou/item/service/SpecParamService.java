package com.study.legou.item.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.legou.core.service.ICrudService;
import com.study.legou.item.po.SpecParam;

import java.util.List;

public interface SpecParamService extends ICrudService<SpecParam> {
    public List<SpecParam> list(SpecParam entity);
}