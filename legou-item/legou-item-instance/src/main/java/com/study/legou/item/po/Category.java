package com.study.legou.item.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.legou.core.po.BaseTreeEntity;
import lombok.Data;


/**
 * @Title: 商品分类
 * 因为Category是一个树形结构，所以需要继承BaseTreeEntity
 */
@Data
@TableName("category_")
public class Category extends BaseTreeEntity {

    @TableField("is_parent_")
    private Boolean isParent = false; //是否为父节点

    @TableField(exist = false)
    private Integer isRoot = 0; //值=1 ： 查询根节点条件

    public String getLabel() { //treeselect需要的属性
        return this.getTitle();
    }

}