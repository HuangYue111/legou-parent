package com.study.legou.item.service;

import com.legou.core.service.ICrudService;
import com.study.legou.item.po.Category;

import java.util.List;

public interface CategoryService extends ICrudService<Category> {
    List<String> selectNamesByIds(List<Long> ids);
}
