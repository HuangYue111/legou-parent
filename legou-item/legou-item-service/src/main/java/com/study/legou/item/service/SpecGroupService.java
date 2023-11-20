package com.study.legou.item.service;

import com.legou.core.service.ICrudService;
import com.study.legou.item.po.SpecGroup;

import java.util.List;

public interface SpecGroupService extends ICrudService<SpecGroup> {
    /**
     * 根据前台传递的规格参数列表，保存规格参数
     * @param cid
     * @param groups
     */
    public void saveGroup(Long cid, List<SpecGroup> groups);
}
