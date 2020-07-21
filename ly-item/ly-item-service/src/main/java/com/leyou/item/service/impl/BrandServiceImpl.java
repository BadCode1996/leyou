package com.leyou.item.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.common.bean.PageResult;
import com.leyou.item.bean.Brand;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.service.BrandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Srd
 * @date 2020/5/19  16:51
 */
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 分页查询品牌
     *
     * @param page   当前页
     * @param rows   页容量
     * @param desc   是否降序
     * @param key    搜索关键字
     * @param sortBy 排序字段
     * @return
     */
    @Override
    public PageResult<Brand> queryBrandByPage(Integer page, Integer rows, Boolean desc, String key, String sortBy) {
//        开始分页
        PageHelper.startPage(page, rows);
//        过滤
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)) {
            example.createCriteria().andLike("name", "%" + key + "%").orEqualTo("letter", key.toUpperCase());
        }
        if (StringUtils.isNotBlank(sortBy)) {
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }
        Page<Brand> pageInfo = (Page<Brand>) this.brandMapper.selectByExample(example);
        return new PageResult<>(pageInfo.getTotal(), pageInfo);
    }

    /**
     * 新增品牌
     * @param brand
     * @param cids
     */
    @Override
    public void addBrand(Brand brand, List<Long> cids) {
        brandMapper.insert(brand);
        cids.forEach(cid ->{
            brandMapper.insertCategoryBrand(cid,brand.getId());
        });
    }

    /**
     * 修改品牌
     * @param brand
     * @param cids
     */
    @Override
    public void editBrand(Brand brand, List<Long> cids) {
//        更新tb_brand
        this.brandMapper.updateByPrimaryKey(brand);

//        更新tb_category_brand
//        先删除旧数据
        this.brandMapper.deleteCateGoryBrandByBid(brand.getId());
//        再新增新数据
        cids.forEach(cid ->{
            brandMapper.insertCategoryBrand(cid,brand.getId());
        });

    }

    /**
     * 根据品牌id删除品牌
     * @param bid
     */
    @Override
    public void deleteBrand(Long bid) {
//        删除tb_brand表数据
        this.brandMapper.deleteByPrimaryKey(bid);
//        删除tb_category_brand表数据
        this.brandMapper.deleteCateGoryBrandByBid(bid);
    }

    /**
     * 根据分类查询品牌
     * @param cid
     * @return
     */
    @Override
    public List<Brand> queryBrandByCid(Long cid) {
        return this.brandMapper.selectCateGoryBrandByCid(cid);
    }
}
