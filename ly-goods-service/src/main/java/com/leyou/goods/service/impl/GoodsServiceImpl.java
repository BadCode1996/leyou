package com.leyou.goods.service.impl;

import com.leyou.goods.client.BrandClient;
import com.leyou.goods.client.CategoryClient;
import com.leyou.goods.client.GoodsClient;
import com.leyou.goods.client.SpecificationClient;
import com.leyou.goods.service.GoodsService;
import com.leyou.item.bean.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Srd
 * @date 2020/8/14  15:44
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;


    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsService.class);

    /**
     * 封装数据Spu、Sku集合、SpecGroup、Categories、Brand、sku特有参数
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> loadModel(Long id) {
        try {
//            模型数据
            HashMap<String, Object> modelMap = new HashMap<>(16);

//            查询spu
            Spu spu = this.goodsClient.querySpuById(id);
//            查询spuDetail
            SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(id);
//            查询sku集合
            List<Sku> skus = this.goodsClient.querySkuBySpuId(id);
//            查询规格参数SpecGroup
            List<SpecGroup> groups = this.specificationClient.querySpecsByCid(spu.getCid3());
//            查询Categories
            List<Category> categories = getCategories(spu);
//            查询Brand
            List<Brand> brands = this.brandClient.queryBrandByIds(Arrays.asList(spu.getBrandId()));
//            查询sku特有参数,并把结果数据处理为id:name键值对格式，4:机身颜色 5:内存
            List<SpecParam> params = this.specificationClient.querySpecParam(null, spu.getCid3(), null, false);
            HashMap<Long, String> paramMap = new HashMap<>(16);
            for (SpecParam param : params) {
                paramMap.put(param.getId(),param.getName());
            }

            modelMap.put("spu",spu);
            modelMap.put("spuDetail",spuDetail);
            modelMap.put("skus",skus);
            modelMap.put("groups",groups);
            if (categories != null){
                modelMap.put("categories",categories);
            }
            modelMap.put("brand",brands.get(0));
            modelMap.put("paramMap",paramMap);

            return modelMap;
        } catch (Exception e) {
            LOGGER.error("加载商品数据出错,spuId:{}", id, e);
            return null;
        }
    }

    private List<Category> getCategories(Spu spu) {
        try {
            List<String> names = this.categoryClient.queryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));

            Category category1 = new Category();
            category1.setName(names.get(0));
            category1.setId(spu.getCid1());

            Category category2 = new Category();
            category2.setName(names.get(1));
            category2.setId(spu.getCid2());

            Category category3 = new Category();
            category3.setName(names.get(2));
            category3.setId(spu.getCid3());

            return Arrays.asList(category1,category2,category3);
        } catch (Exception e) {
            LOGGER.error("查询商品分类出错，spuId：{}", spu.getId(), e);
            return null;
        }
    }
}
