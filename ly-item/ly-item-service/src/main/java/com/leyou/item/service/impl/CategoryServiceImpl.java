package com.leyou.item.service.impl;

import com.leyou.item.bean.Category;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.service.CategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Srd
 * @date 2020/5/19  2:37
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据父节点查询商品类目
     * @param pid
     * @return
     */
    @Override
    public List<Category> queryByParent(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        return this.categoryMapper.select(category);
    }

    /**
     * 根据品牌id，查询分类
     *
     * @param bid
     * @return
     */
    @Override
    public List<Category> queryByBrandId(Long bid) {
        return this.categoryMapper.queryByBrandId(bid);
    }

    /**
     * 查询多级分类名
     *
     * @param ids
     * @return
     */
    @Override
    public String queryNameByIds(List<Long> ids) {
        ArrayList<String> names = new ArrayList<>();
        ids.forEach(id -> {
            Category category = this.categoryMapper.selectByPrimaryKey(id);
            names.add(category.getName());
        });
        return StringUtils.join(names, "/");
    }

    /**
     * 根据分类id(category_id)获取分类名
     *
     * @param ids category_id
     * @return List<String> 分类名集合
     */
    @Override
    public List<String> queryCategoryNameByIds(List<Long> ids) {
        List<Category> categories = this.categoryMapper.selectByIdList(ids);
        List<String> names = new ArrayList<>();
        for (Category category : categories) {
            names.add(category.getName());
        }
        return names;
    }

    /**
     * 根据3级分类id，查询1~3级的分类
     *
     * @param id
     * @return
     */
    @Override
    public List<Category> queryAllByCid3(Long id) {
        Category category3 = this.categoryMapper.selectByPrimaryKey(id);
        Category category2 = this.categoryMapper.selectByPrimaryKey(category3.getParentId());
        Category category1 = this.categoryMapper.selectByPrimaryKey(category2.getParentId());
        return Arrays.asList(category1,category2,category3);
    }
}
