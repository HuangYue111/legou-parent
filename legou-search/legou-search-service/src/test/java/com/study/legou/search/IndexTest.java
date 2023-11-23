package com.study.legou.search;

import com.study.legou.search.po.Goods;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;

@SpringBootTest
public class IndexTest {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    public void createIndex() {
        //创建索引
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(Goods.class);
        indexOperations.createMapping(Goods.class);
    }
}
