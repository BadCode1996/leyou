package com.leyou.item.service;

import com.leyou.common.bean.PageResult;
import com.leyou.item.bean.Brand;

import java.util.List;

/**
 * @author Srd
 * @date 2020/5/19  15:36
 */
public interface BrandService {

    /**
     * 分页查询品牌
     * @param page 当前页
     * @param rows 页容量
     * @param desc 是否降序
     * @param key 搜索关键字
     * @param sortBy 排序字段
     * @return
     */
    PageResult<Brand> queryBrandByPage(Integer page, Integer rows, Boolean desc, String key, String sortBy);

    /**
     * 新增品牌
     * @param brand
     * @param cids
     */
    void addBrand(Brand brand, List<Long> cids);

    /**
     * 修改品牌
     * @param brand
     * @param cids
     */
    void editBrand(Brand brand, List<Long> cids);

    /**
     * 根据品牌id删除品牌
     * @param bid
     */
    void deleteBrand(Long bid);

    /**
     * 根据分类查询品牌
     * @param cid
     * @return
     */
    List<Brand> queryBrandByCid(Long cid);

    /**
     * 根据ids查询品牌集合
     * @param ids
     * @return
     */
    List<Brand> queryBrandByIds(List<Long> ids);
}
