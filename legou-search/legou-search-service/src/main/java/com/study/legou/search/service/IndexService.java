package com.study.legou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.legou.common.utils.JsonUtils;
import com.study.legou.item.po.Sku;
import com.study.legou.item.po.SpecParam;
import com.study.legou.item.po.Spu;
import com.study.legou.item.po.SpuDetail;
import com.study.legou.search.client.CategoryClient;
import com.study.legou.search.client.SkuClient;
import com.study.legou.search.client.SpecParamClient;
import com.study.legou.search.client.SpuDetailClient;
import com.study.legou.search.dao.GoodsDao;
import com.study.legou.search.po.Goods;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

/**
 * @Title: 索引服务类
 */
@Service
public class IndexService {

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private SpecParamClient specParamClient;

    @Autowired
    private SkuClient skuClient;

    @Autowired
    private SpuDetailClient spuDetailClient;

    /**
     * 根据spu构建索引类型
     *
     * @param spu
     * @return
     */
    public Goods buildGoods(Spu spu) {
        Long id = spu.getId();
        //准备数据

        //商品分类名称
        List<String> names = this.categoryClient.queryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        String all = spu.getTitle() + " " + StringUtils.join(names, " ");

        //sku集合
        List<Sku> skus = skuClient.selectSkusBySpuId(spu.getId());

        //处理sku
        //把商品价格取出单独存放，便于展示
        List<Long> prices = new ArrayList<>();
        List<Map<String, Object>> skuList = new ArrayList<>();


        for (Sku sku : skus) {
            prices.add(sku.getPrice());
            Map<String, Object> skuMap = new HashMap<>();
            skuMap.put("id", sku.getId());
            skuMap.put("title", sku.getTitle());
            skuMap.put("image", StringUtils.isBlank(sku.getImages()) ? "" : sku.getImages().split(",")[0]);
            skuMap.put("price", sku.getPrice());
            skuList.add(skuMap);
        }

        //spec
        Map<String, Object> specs = new HashMap<>();
        //spuDetail
        SpuDetail spuDetail = spuDetailClient.edit(spu.getId());

        //通用规格参数值
        Map<String, String> genericMap = JsonUtils.parseMap(spuDetail.getGenericSpec(), String.class, String.class);
        //特有规格参数的值
        Map<String, List<String>> specialMap = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<String, List<String>>>() {
        });

        //查询分类对应的规格参数
        SpecParam specParam = new SpecParam();
        specParam.setCid(spu.getCid3());
        specParam.setSearching(true);
        List<SpecParam> params = specParamClient.selectSpecParamApi(specParam);

        for (SpecParam param : params) {
            //今后显示的名称
            String name = param.getName();//品牌，机身颜色
            //通用参数
            Object value = null;
            if (param.getGeneric()) {
                //通用参数
                value = genericMap.get(name);

                if (param.getNumeric()) {
                    //数值类型需要加分段
                    value = this.chooseSegment(value.toString(), param);
                }
            } else {//特有参数
                value = specialMap.get(name);

            }
            if (null == value) {
                value = "其他";
            }
            specs.put(name, value);
        }

        Goods goods = new Goods();
        goods.setId(spu.getId());
        //这里如果要加品牌，可以再写个BrandClient，根据id查品牌
        goods.setAll(all);
        goods.setSubTitle(spu.getSubTitle());
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setPrice(prices);
        goods.setSkus(JsonUtils.serialize(skuList));
        goods.setSpecs(specs);

        return goods;
    }

    //因为过滤参数中有一类比较特殊，就是数值区间,所以在存入时进行处理
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();//4.5  4-5英寸
                }
                break;
            }
        }
        return result;
    }


    /**
     * 根据商品id删除索引
     *
     * @param id
     */
    public void deleteIndex(Long id) {
        goodsDao.deleteById(id);
    }

}