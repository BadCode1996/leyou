package com.leyou.search.controller;

import com.leyou.common.bean.PageResult;
import com.leyou.search.bean.Goods;
import com.leyou.search.bean.SearchRequest;
import com.leyou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Srd
 * @date 2020/7/24  0:35
 */
@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping("page")
    public ResponseEntity<PageResult<Goods>> queryByPage(@RequestBody SearchRequest searchRequest) {
        PageResult<Goods> goodsPageResult = this.searchService.search(searchRequest);
        if (goodsPageResult == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(goodsPageResult);
    }
}
