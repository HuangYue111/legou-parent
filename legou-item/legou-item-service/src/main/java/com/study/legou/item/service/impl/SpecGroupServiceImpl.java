package com.study.legou.item.service.impl;

import com.legou.core.service.impl.CrudServiceImpl;
import com.study.legou.item.dao.SpecGroupDao;
import com.study.legou.item.dao.SpecParamDao;
import com.study.legou.item.po.SpecGroup;
import com.study.legou.item.po.SpecParam;
import com.study.legou.item.service.SpecGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SpecGroupServiceImpl extends CrudServiceImpl<SpecGroup> implements SpecGroupService {
    @Autowired
    private SpecParamDao specParamDao;

    @Override
    public List<SpecGroup> list(SpecGroup entity) {
        return ((SpecGroupDao) getBaseMapper()).selectList(entity);
    }

    @Override
    @Transactional
    public void saveGroup(Long cid, List<SpecGroup> groups) {
        //根据cid删除所有规格参数的分组和规格参数
        getBaseMapper().delete(Wrappers.<SpecGroup>query().eq("cid_", cid));
        specParamDao.delete(Wrappers.<SpecParam>query().eq("cid_", cid));

        //逐个添加规格参数分组和规格参数
        for (SpecGroup group : groups) {
            getBaseMapper().insert(group);
            for (SpecParam specParam : group.getParams()) {
                specParam.setGroupId(group.getId()); //页面groupId是防止重复自动+1，保存时要保存数据库中groupId
                specParamDao.insert(specParam);
            }
        }
    }
}