package com.leyou.search.service.impl;

import com.leyou.item.bean.Brand;
import com.leyou.item.bean.Category;
import com.leyou.item.bean.SpecParam;
import com.leyou.search.bean.Goods;
import com.leyou.search.bean.SearchRequest;
import com.leyou.search.bean.SearchResult;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Srd
 * @date 2020/7/27  1:21
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private SpecificationClient specificationClient;

    /**
     * 分页查询
     *
     * @param searchRequest searchRequest
     * @return SearchResult
     */
    @Override
    public SearchResult search(SearchRequest searchRequest) {
        String key = searchRequest.getKey();
        if (key == null) {
            return null;
        }
//        基本查询
        QueryBuilder queryBuilderWithFilter = buildBasicQueryWithFilter(searchRequest);

//        Spring Data JPA中默认分页从0开始，前台传来的是从1开始，需要减1
        int page = searchRequest.getPage() - 1;
        Integer size = SearchRequest.getSize();

//        要排序的字段
        String sortBy = searchRequest.getSortBy();
//        排序方式，true降序，false升序
        Boolean descending = searchRequest.getDescending();
//        添加排序操作，如果排序字段不为空，进行排序
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        if (StringUtils.isNotBlank(sortBy)) {
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(sortBy).order(descending ? SortOrder.DESC : SortOrder.ASC));
        }

//        聚合
        String categoryAggName = "categoryAgg";
        String brandAggName = "brandAgg";

        NativeSearchQuery queryBuilder = nativeSearchQueryBuilder
                .withSourceFilter(new FetchSourceFilter(new String[]{"id", "skus", "subTitle"}, null))
                .withQuery(queryBuilderWithFilter)
                .withPageable(PageRequest.of(page, size))
                .build();

//        商品分类聚合
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
//        品牌聚合
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));

        SearchHits<Goods> searchHits = this.restTemplate.search(queryBuilder, Goods.class);
        List<Goods> goodsList = new ArrayList<>();
        searchHits.forEach(searchHit -> goodsList.add(searchHit.getContent()));

//        商品分类、品牌 聚合结果
        List<Category> categories = getCategoryAggResult(searchHits.getAggregations().get(categoryAggName));
        List<Brand> brands = getBrandAggResult(searchHits.getAggregations().get(brandAggName));

        long total = searchHits.getTotalHits();
        long totalPage = (total + size - 1) / size;

//        判断商品分类数量，来判断是否需要对规格参数进行聚合
        List<Map<String, Object>> specs = null;
        if (categories.size() == 1) {
            specs = getSpec(categories.get(0).getId(), queryBuilder);
        }
        return new SearchResult(total, totalPage, goodsList, categories, brands, specs);
    }

    private QueryBuilder buildBasicQueryWithFilter(SearchRequest searchRequest) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 基本查询条件
        queryBuilder.must(QueryBuilders.matchQuery("all", searchRequest.getKey()).operator(Operator.AND));
        // 过滤条件构建器
        BoolQueryBuilder filterQueryBuilder = QueryBuilders.boolQuery();
        // 整理过滤条件
        Map<String, Object> filter = searchRequest.getFilter();
        for (Map.Entry< String, Object> entry : filter.entrySet()) {
            String key = entry.getKey();
            String value = (String) entry.getValue();
            // 商品分类和品牌要特殊处理
            if (key != "cid3" && key != "brandId") {
                key = "specs." + key + ".keyword";
            }
            // 字符串类型，进行term查询
            filterQueryBuilder.must(QueryBuilders.termQuery(key, value));
        }
        // 添加过滤条件
        queryBuilder.filter(filterQueryBuilder);
        return queryBuilder;
    }

    private List<Map<String, Object>> getSpec(Long id, NativeSearchQuery queryBuilder) {
        try {
//            根据分类查询规格参数
            List<SpecParam> params = this.specificationClient.querySpecParam(null, id, true, null);

//            创建集合，保存规格过滤条件
            List<Map<String, Object>> specs = new ArrayList<>();

            NativeSearchQueryBuilder query = new NativeSearchQueryBuilder()
                    .withQuery(queryBuilder.getQuery());

//            聚合规格参数
            for (SpecParam param : params) {
                String name = param.getName();
                query.addAggregation(AggregationBuilders.terms(name).field("specs." + name + ".keyword"));
            }

            Aggregations aggregations = this.restTemplate.search(query.build(), Goods.class).getAggregations();

            Map<String, Aggregation> asMap = aggregations.getAsMap();

//            打印结果测试
            for (Map.Entry<String, Aggregation> entry : asMap.entrySet()) {
                System.out.println(entry.getKey() + "-----" + entry.getValue().toString());
            }

            for (SpecParam param : params) {
                Map<String, Object> map = new HashMap<>(16);
                String name = param.getName();
                ParsedStringTerms stringTerms = (ParsedStringTerms) asMap.get(name);
                List<String> val = new ArrayList<>();
                for (Terms.Bucket bucket : stringTerms.getBuckets()) {
                    val.add(bucket.getKeyAsString());
                }
                map.put("k",name);
                map.put("options",val);

                specs.add(map);
            }

            return specs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Brand> getBrandAggResult(Aggregation aggregation) {

        try {
            ParsedLongTerms longTerms = (ParsedLongTerms) aggregation;
            List<Long> bids = new ArrayList<>();
            for (Terms.Bucket bucket : longTerms.getBuckets()) {
                bids.add(bucket.getKeyAsNumber().longValue());
            }
            return this.brandClient.queryBrandByIds(bids);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Category> getCategoryAggResult(Aggregation aggregation) {
        try {
            List<Category> categories = new ArrayList<>();
            ParsedLongTerms longTerms = (ParsedLongTerms) aggregation;
            List<Long> cids = new ArrayList<>();

            for (Terms.Bucket bucket : longTerms.getBuckets()) {
                cids.add(bucket.getKeyAsNumber().longValue());
            }
            List<String> names = this.categoryClient.queryNameByIds(cids);
            for (int i = 0; i < names.size(); i++) {
                Category category = new Category();
                category.setId(cids.get(i));
                category.setName(names.get(i));
                categories.add(category);
            }
            return categories;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
