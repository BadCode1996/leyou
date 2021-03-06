package com.leyou.search.service;

import com.leyou.common.bean.PageResult;
import com.leyou.search.bean.Goods;
import com.leyou.search.bean.SearchRequest;
import com.leyou.search.bean.SearchResult;

/**
 * @author Srd
 * @date 2020/7/27  1:21
 */
public interface SearchService {

    /**
     * 分页查询
     * @param searchRequest
     * @return
     */
    SearchResult search(SearchRequest searchRequest);
}
