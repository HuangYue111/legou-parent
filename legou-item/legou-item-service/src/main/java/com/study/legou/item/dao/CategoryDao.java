package com.study.legou.item.dao;

import com.legou.core.dao.ICrudDao;
import com.study.legou.item.po.Category;

/**
 * 分类Dao
 * 不用动态SQL语句查询，所以不用映射文件
 */
public interface CategoryDao extends ICrudDao<Category> {
}
