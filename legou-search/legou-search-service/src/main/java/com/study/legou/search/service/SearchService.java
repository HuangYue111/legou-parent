package com.study.legou.search.service;

import com.study.legou.item.po.Brand;
import com.study.legou.item.po.Category;
import com.study.legou.item.po.SpecParam;
import com.study.legou.search.client.BrandClient;
import com.study.legou.search.client.CategoryClient;
import com.study.legou.search.client.SpecParamClient;
import com.study.legou.search.dao.GoodsDao;
import com.study.legou.search.po.Goods;
import com.study.legou.search.po.SearchRequest;
import com.study.legou.search.po.SearchResult;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
//对用户输入的key进行基本的搜索
public class SearchService {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private SpecParamClient specParamClient;

    //搜索业务
    public SearchResult search(SearchRequest searchRequest) {
        //1.获取关键字
        String key = searchRequest.getKey();


        //2.构建查询工具对象
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "subTitle", "skus"}, null));


        //3.构造复合查询条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.hasText(key)) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("all", key));
        }
        //...过滤条件
        if (searchRequest.getFilter() != null && searchRequest.getFilter().size() > 0) {
            BoolQueryBuilder filterQueryBuilder = QueryBuilders.boolQuery();
            //取出map中的实体
            for (Map.Entry<String, String> entry : searchRequest.getFilter().entrySet()) {
                String k = entry.getKey();
                String v = entry.getValue();
                // 商品分类和品牌不用前后加修饰
                if (k != "cid3" && k != "brandId") {
                    k = "specs." + k + ".keyword";
                }
                // 字符串类型，进行term查询
                filterQueryBuilder.must(QueryBuilders.termQuery(k, v));
            }

            boolQueryBuilder.filter(filterQueryBuilder);
        }


        //分页
        int currentPage = searchRequest.getPage();
        int pageSize = searchRequest.getSize();
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        //排序
        String sortBy = searchRequest.getSortBy();
        if (StringUtils.hasText(sortBy)) {
            Boolean descending = searchRequest.getDescending();
            SortBuilder sortBuilder;
            if (descending) {
                sortBuilder = SortBuilders.fieldSort(sortBy).order(SortOrder.ASC);
            } else {
                sortBuilder = SortBuilders.fieldSort(sortBy).order(SortOrder.DESC);
            }
            //构建排序条件
            searchQueryBuilder.withSort(sortBuilder);
        }


        //4.设置查询条件
        searchQueryBuilder.withQuery(boolQueryBuilder);
        //5.设置分页条件
        searchQueryBuilder.withPageable(pageable);


        //7.构建分组信息
        String brandAggsName = "brands";
        String categoryAggsName = "categories";
        searchQueryBuilder.addAggregation(AggregationBuilders.terms(brandAggsName).field("brandId"));
        searchQueryBuilder.addAggregation(AggregationBuilders.terms(categoryAggsName).field("cid3"));


        //8.进行查询
        NativeSearchQuery query = searchQueryBuilder.build();
        SearchHits<Goods> search = elasticsearchRestTemplate.search(query, Goods.class);

        //9.解析结果
        List<Goods> list = new ArrayList<>();
        for (SearchHit<Goods> goodsSearchHit : search) {
            Goods goods = goodsSearchHit.getContent();
            list.add(goods);
        }
        Page<Goods> page = new PageImpl<>(list, pageable, search.getTotalHits());
        //取分组信息,品牌分组
        Aggregations aggregations = search.getAggregations();
        Terms terms = (Terms) aggregations.get(brandAggsName);
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        List<Long> brandIds = new ArrayList<>();
        for (Terms.Bucket bucket : buckets) {
            brandIds.add(bucket.getKeyAsNumber().longValue());
        }
        List<Brand> brands = brandClient.selectBrandByIds(brandIds);
        //取分类分组
        Terms terms2 = (Terms) aggregations.get(categoryAggsName);
        List<? extends Terms.Bucket> buckets2 = terms2.getBuckets();
        List<Long> categoryIds = new ArrayList<>();
        for (Terms.Bucket bucket : buckets2) {
            categoryIds.add(bucket.getKeyAsNumber().longValue());
        }
        List<String> names = categoryClient.queryNameByIds(categoryIds);
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            Category category = new Category();
            category.setId(categoryIds.get(i));
            category.setTitle(names.get(i));
            categories.add(category);
        }

        //10.如果分类信息是一个，查询规格参数分组信息
        List<Map<String, Object>> specs = new ArrayList<>();
        if (categories != null && categories.size() == 1) {
            //获得当前商品唯一分类的可查询的规格参数
            Long cid = categories.get(0).getId();
            SpecParam specParam = new SpecParam();
            specParam.setCid(cid);
            specParam.setSearching(true);
            List<SpecParam> specParams = specParamClient.selectSpecParamApi(specParam);
            for (SpecParam param : specParams) {
                String name = param.getName();
                searchQueryBuilder.addAggregation(AggregationBuilders.terms(name).field("specs." + name + ".keyword"));
            }
            NativeSearchQuery query2 = searchQueryBuilder.build();
            SearchHits<Goods> search1 = elasticsearchRestTemplate.search(query2, Goods.class);
            Aggregations aggregations1 = search1.getAggregations();
            for (SpecParam param : specParams) {
                Map<String, Object> map = new HashMap<>();
                String name = param.getName();
                Terms terms1 = (Terms) aggregations1.get(name);
                List<? extends Terms.Bucket> buckets1 = terms1.getBuckets();
                List<String> val = new ArrayList<>();
                for (Terms.Bucket bucket : buckets1) {
                    val.add(bucket.getKeyAsString());
                }
                map.put("k", name);
                map.put("options", val);
                specs.add(map);
            }

        }

        return new SearchResult(page.getTotalElements(), page.getTotalPages(), page.getContent(), categories, brands, specs);

    }
}