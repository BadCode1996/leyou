package com.leyou.item.api;

import com.leyou.item.bean.Brand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Srd
 * @date 2020/7/28  15:23
 */
@RequestMapping("brand")
public interface BrandApi {

    /**
     * 根据ids查询品牌集合
     * @param ids
     * @return
     */
    @GetMapping("list")
    List<Brand> queryBrandByIds(@RequestParam("ids")List<Long> ids);
}
