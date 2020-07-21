package com.leyou.item.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.common.bean.PageResult;
import com.leyou.item.bean.*;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.*;
import com.leyou.item.service.CategoryService;
import com.leyou.item.service.GoodsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PutMapping;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Srd
 * @date 2020/5/25  16:23
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    /**
     * 分页查询商品列表
     * @param key      搜索条件
     * @param saleable 是否上下架
     * @param page     当前页
     * @param rows     页容量
     * @return PageResult<SpuBo>
     */
    @Override
    public PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows) {
//        分页，最多查询100条数据
        PageHelper.startPage(page, Math.min(rows, 100));

//        查询条件
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

//        过滤上下架
        if (saleable != null) {
            criteria.orEqualTo("saleable", saleable);
        }

//        搜索条件过滤
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key+ "%");
        }

        Page<Spu> pageInfo = (Page<Spu>) this.spuMapper.selectByExample(example);

        List<SpuBo> list = pageInfo.getResult().stream().map(spu -> {
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu, spuBo);

//            获取分类名，需要引入CategoryService
            String name = this.categoryService.queryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            spuBo.setCname(name);

//            获取品牌名称
            Brand brand = this.brandMapper.selectByPrimaryKey(spu.getBrandId());
            spuBo.setBname(brand.getName());

            return spuBo;
        }).collect(Collectors.toList());

        return new PageResult<>(pageInfo.getTotal(),list);
    }

    /**
     * 新增商品
     * @param spu 封装的前台数据
     */
    @Override
    @Transactional()
    public void save(SpuBo spu) {
//        保存spu
        spu.setSaleable(true);
        spu.setValid(true);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        this.spuMapper.insert(spu);

//        保存spu详情 spu_detail
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        this.spuDetailMapper.insert(spuDetail);
        saveSkuAndStock(spu);
    }

    /**
     * 根据spu_id查询spu_detail,回显商品内容
     *
     * @param id spu_id
     * @return SpuDetail
     */
    @Override
    public SpuDetail querySpuDetailBySpuId(Long id) {
        return this.spuDetailMapper.selectByPrimaryKey(id);
    }

    /**
     * 修改商品，根据id获取sku，回显数据
     * @param id spu_id
     * @return List<Sku>
     */
    @Override
    public List<Sku> querySkuBySpuId(Long id) {
        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> skus = this.skuMapper.select(sku);
        skus.forEach(s -> s.setStock(this.stockMapper.selectByPrimaryKey(s.getId()).getStock()));
        return skus;
    }

    /**
     * 修改商品
     * @param spu spu
     */
    @Override
    @Transactional
    public void edit(SpuBo spu) {
//        修改spu
        spu.setLastUpdateTime(new Date());
        spu.setCreateTime(null);
        spu.setValid(null);
        spu.setSaleable(null);
        this.spuMapper.updateByPrimaryKeySelective(spu);

//        修改spu详情，spu_detail
        this.spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());

//        修改sku，sku信息是变动的，可能有些数据不存在，所以需要先删除再添加
//        获取以前的sku数据
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        List<Sku> skuList = this.skuMapper.select(sku);
//        判断以前的数据是否存在，存在就删除
        if (!CollectionUtils.isEmpty(skuList)){
//            得到sku_id
            List<Long> ids = skuList.stream().map(Sku::getId).collect(Collectors.toList());

//            删除sku
            Sku record = new Sku();
            record.setSpuId(spu.getId());
            this.skuMapper.delete(record);

//            删除stock
            Example example = new Example(Stock.class);
            example.createCriteria().andIn("skuId",ids);
        }
//        添加sku和stock
        saveSkuAndStock(spu);
    }

    /**
     * 对商品进行上下架操作
     *
     * @param id
     */
    @Override
    public void changeSaleable(Long id) {
        Spu spu = this.spuMapper.selectByPrimaryKey(id);
        spu.setSaleable(!spu.getSaleable());
        this.spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 通用方法
     * @param spu spu
     */
    private void saveSkuAndStock(SpuBo spu) {
//        保存sku和库存信息
        for (Sku sku : spu.getSkus()) {
            if (!sku.getEnable()){
                continue;
            }
//            保存sku
            sku.setSpuId(spu.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insert(sku);

//            保存库存信息
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insert(stock);
        }
    }
}
