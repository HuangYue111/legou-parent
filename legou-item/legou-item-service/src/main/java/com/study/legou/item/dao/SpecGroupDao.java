package com.study.legou.item.dao;

import com.legou.core.dao.ICrudDao;
import com.study.legou.item.po.SpecGroup;

import java.util.List;

public interface SpecGroupDao extends ICrudDao<SpecGroup> {
    public List<SpecGroup> selectList(SpecGroup specGroup);
}