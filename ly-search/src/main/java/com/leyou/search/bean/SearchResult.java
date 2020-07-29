package com.leyou.search.bean;

import com.leyou.common.bean.PageResult;
import com.leyou.item.bean.Brand;
import com.leyou.item.bean.Category;

import java.util.List;

/**
 * 扩展属性
 * @author Srd
 * @date 2020/7/28  15:18
 */
public class SearchResult extends PageResult<Goods> {

    /**
     * 分类集合
     */
    private List<Category> categories;
    /**
     * 品牌集合
     */
    private List<Brand> brands;

    public SearchResult(Long total, Long totalPage, List items, List<Category> categories, List<Brand> brands) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }
}
