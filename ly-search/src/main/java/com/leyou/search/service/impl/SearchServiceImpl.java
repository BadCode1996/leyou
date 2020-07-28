package com.leyou.search.service.impl;

import com.leyou.common.bean.PageResult;
import com.leyou.search.bean.Goods;
import com.leyou.search.bean.SearchRequest;
import com.leyou.search.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    /**
     * 分页查询
     *
     * @param searchRequest
     * @return
     */
    @Override
    public PageResult<Goods> search(SearchRequest searchRequest) {
        String key = searchRequest.getKey();
        if (key == null) {
            return null;
        }
//        Spring Data JPA中默认分页从0开始，前台传来的是从1开始，需要减1
        Integer page = searchRequest.getPage() - 1;
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

        NativeSearchQuery queryBuilder = nativeSearchQueryBuilder
                .withSourceFilter(new FetchSourceFilter(new String[]{"id", "skus", "subTitle"}, null))
                .withQuery(QueryBuilders.matchQuery("all", key))
                .withPageable(PageRequest.of(page, size))
                .build();

        SearchHits<Goods> searchHits = this.restTemplate.search(queryBuilder, Goods.class);
        List<Goods> goodsList = new ArrayList<>();
        searchHits.forEach(searchHit -> goodsList.add(searchHit.getContent()));

        long total = searchHits.getTotalHits();
        long totalPage = (total + size - 1) / size;
        return new PageResult<>(total, totalPage, goodsList);
    }
}
