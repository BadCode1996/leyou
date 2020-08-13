package com.leyou.item.service;

import com.leyou.item.bean.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Srd
 * @date 2020/5/19  2:36
 */
public interface CategoryService {

    /**
     * 根据父节点查询商品类目
     * @param pid
     * @return
     */
    List<Category> queryByParent(Long pid);

    /**
     * 根据品牌id，查询分类
     * @param bid
     * @return
     */
    List<Category> queryByBrandId(Long bid);

    /**
     * 查询多级分类名
     * @param ids
     * @return
     */
    String queryNameByIds(List<Long> ids);

    /**
     * 根据分类id(category_id)获取分类名
     * @param ids category_id
     * @return List<String> 分类名集合
     */
    List<String> queryCategoryNameByIds(List<Long> ids);

    /**
     * 根据3级分类id，查询1~3级的分类
     * @param id
     * @return
     */
    List<Category> queryAllByCid3(Long id);
}
