package com.study.legou.search;

import com.study.legou.item.po.Spu;
import com.study.legou.search.client.SpuClient;
import com.study.legou.search.dao.GoodsDao;
import com.study.legou.search.po.Goods;
import com.study.legou.search.service.IndexService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SearchApplication.class)
public class ESLoadDataTest {

    @Autowired
    private IndexService indexService;

    @Autowired
    private SpuClient spuClient;

    @Autowired
    private GoodsDao goodsDao;

    @Test
    public void loadData() {
        // 查询spu
//            PageResult<SpuBO> result = this.goodsClient.querySpuByPage(page, rows, true, null);
//            List<SpuBO> spus = result.getItems();
        List<Spu> spus = spuClient.selectAll();

        // spu转为goods
        List<Goods> goods = spus.stream().map(spu -> this.indexService.buildGoods(spu))
                .collect(Collectors.toList());

        // 把goods放入索引库
        goodsDao.saveAll(goods);

    }
}
