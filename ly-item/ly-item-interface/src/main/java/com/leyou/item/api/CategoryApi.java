package com.leyou.item.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Srd
 * @date 2020/7/22  0:51
 */
@RequestMapping("category")
public interface CategoryApi {

    /**
     * 根据分类id(category_id)获取分类名
     * @param ids category_id
     * @return List<String> 分类名集合
     */
    @GetMapping("names")
    List<String> queryNameByIds(@RequestParam("ids") List<Long> ids);
}
