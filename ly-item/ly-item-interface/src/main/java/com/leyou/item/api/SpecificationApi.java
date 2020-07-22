package com.leyou.item.api;

import com.leyou.item.bean.SpecParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Srd
 * @date 2020/7/22  15:50
 */
@RequestMapping("spec")
public interface SpecificationApi {

    /**
     * 通用接口
     * http://api.leyou.com/api/item/spec/params?gid=3
     * http://api.leyou.com/api/item/spec/params?cid=324
     * @param gid
     * @param cid
     * @param searching
     * @param generic
     * @return
     */
    @GetMapping("params")
    List<SpecParam> querySpecParam(
            @RequestParam(value = "gid",required = false)Long gid,
            @RequestParam(value = "cid",required = false)Long cid,
            @RequestParam(value = "searching",required = false)Boolean searching,
            @RequestParam(value = "generic",required = false)Boolean generic
    );
}
