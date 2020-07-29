package com.leyou.search.service.impl;

import com.leyou.item.bean.Brand;
import com.leyou.item.bean.Category;
import com.leyou.search.bean.Goods;
import com.leyou.search.bean.SearchRequest;
import com.leyou.search.bean.SearchResult;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
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
import java.util.List;

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
//        Spring Data JPA中默认分页从0开始，前台传来的是从1开始，需要减1
        int page = searchRequest.getPage() - 1;
        Integer size = SearchRequest.getSize();

//        要排序的字段
        String sortBy = searchRequest.getSortBy();
//        排序方式，true降序，false升序
        Boolean descending = searchRequest.getDescending();
//        添加排序操作
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        if (StringUtils.isNotBlank(sortBy)){
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(sortBy).order(descending ? SortOrder.DESC : SortOrder.ASC));
        }

//        聚合
        String categoryAggName = "categoryAgg";
        String brandAggName = "brandAgg";

        NativeSearchQuery queryBuilder = nativeSearchQueryBuilder
                .withSourceFilter(new FetchSourceFilter(new String[]{"id", "skus", "subTitle"},null))
                .withQuery(QueryBuilders.matchQuery("all", key))
                .withPageable(PageRequest.of(page, size))
                .build();

        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));

        SearchHits<Goods> searchHits = this.restTemplate.search(queryBuilder, Goods.class);
        List<Goods> goodsList = new ArrayList<>();
        searchHits.forEach(searchHit -> goodsList.add(searchHit.getContent()));

        Aggregations aggregations = searchHits.getAggregations();

        List<Category> categories = getCategoryAggResult(aggregations.get(categoryAggName));
        List<Brand> brands = getBrandAggResult(aggregations.get(brandAggName));

        long total = searchHits.getTotalHits();
        long totalPage = (total + size - 1) / size;
        return new SearchResult(total, totalPage, goodsList,categories,brands);
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

            /*for (LongTerms.Bucket bucket : longTerms.getBuckets()) {
                cids.add(bucket.getKeyAsNumber().longValue());
            }*/
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
