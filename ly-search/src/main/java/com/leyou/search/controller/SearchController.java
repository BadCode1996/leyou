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


    /**
     * http://api.leyou.com/api/search/test1?key=123
     *
     * @param key id
     * @return null
     */
    @PostMapping("test1")
    public ResponseEntity<PageResult<Goods>> test1(@RequestParam String key) {
        System.out.println("key = " + key);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * http://api.leyou.com/api/search/test2/123
     *
     * @param id id
     * @return null
     */
    @PostMapping("test2/{id}")
    public ResponseEntity<PageResult<Goods>> test2(@PathVariable("id") Long id) {
        System.out.println("id = " + id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
