package com.study.legou.search.dao;


import com.study.legou.search.po.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GoodsDao extends ElasticsearchRepository<Goods, Long> {

}