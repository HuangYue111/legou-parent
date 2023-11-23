package com.study.legou.canal.listener;
//通过MyEventListener监听器，通过canal监听category_数据变化量，同步分类数据到redis缓存

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.legou.canal.client.CategoryClient;
import com.study.legou.canal.client.PageFeign;
import com.study.legou.item.po.Category;
import com.xpand.starter.canal.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

// 事件监听的注解 监听数据库的变化
@CanalEventListener
public class MyEventListener {
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private PageFeign pageFeign;


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @ListenPoint(destination = "example", schema = "legou", table = {"category_"}, eventType = {CanalEntry.EventType.UPDATE,CanalEntry.EventType.INSERT,CanalEntry.EventType.DELETE})
    public void onEvent4(CanalEntry.EventType eventType, CanalEntry.RowData rowData) throws JsonProcessingException {
        List<Category> list = categoryClient.list(new Category());
        ObjectMapper objectMapper = new ObjectMapper();
        stringRedisTemplate.boundValueOps("category_list").set(objectMapper.writeValueAsString(list));
    }


    @ListenPoint(destination = "example",
            schema = "legou",
            table = {"spu"},
            eventType = {CanalEntry.EventType.UPDATE, CanalEntry.EventType.INSERT, CanalEntry.EventType.DELETE})
    public void onEventCustomSpu(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {

        //判断操作类型
        if (eventType == CanalEntry.EventType.DELETE) {
            String spuId = "";
            List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
            for (CanalEntry.Column column : beforeColumnsList) {
                if (column.getName().equals("id")) {
                    spuId = column.getValue();//spuid
                    break;
                }
            }
            //todo 删除静态页

        }else{
            //新增 或者 更新
            List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
            String spuId = "";
            for (CanalEntry.Column column : afterColumnsList) {
                if (column.getName().equals("id")) {
                    spuId = column.getValue();
                    break;
                }
            }
            //更新 生成静态页
            pageFeign.createHtml(Long.valueOf(spuId));
        }
    }

}