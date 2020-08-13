package com.leyou.search.controller;

import com.leyou.search.bean.SearchRequest;
import com.leyou.search.bean.SearchResult;
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
    public ResponseEntity<SearchResult> queryByPage(@RequestBody SearchRequest searchRequest) {
        SearchResult searchResult = this.searchService.search(searchRequest);
        if (searchResult == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(searchResult);
    }
}
