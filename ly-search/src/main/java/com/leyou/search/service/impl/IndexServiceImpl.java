package com.leyou.search.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.bean.Sku;
import com.leyou.item.bean.SpecParam;
import com.leyou.item.bean.SpuDetail;
import com.leyou.item.bo.SpuBo;
import com.leyou.search.bean.Goods;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.service.IndexService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Srd
 * @date 2020/7/22  15:53
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    /**
     * 构建商品
     * @param spuBo
     * @return
     */
    @Override
    public Goods buildGoods(SpuBo spuBo) {
        Long spuId = spuBo.getId();
//        获取sku
        List<Sku> skus = this.goodsClient.querySkuBySpuId(spuId);
//        获取spuDetail
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spuId);
//        获取categoryName
        List<String> nameByIds = this.categoryClient.queryNameByIds(Arrays.asList(spuBo.getCid1(), spuBo.getCid2(), spuBo.getCid3()));
//        获取通用属性
        List<SpecParam> genericParams = this.specificationClient.querySpecParam(null, spuBo.getCid3(), null, true);
//        获取特有属性
        List<SpecParam> specialParams = this.specificationClient.querySpecParam(null, spuBo.getCid3(), null, false);

//        处理sku
        List<Long> prices = new ArrayList<>();
        List<Map<String,Object>> skuList = new ArrayList<>();
        skus.forEach(sku -> {
//            获取skuList中的price
            prices.add(sku.getPrice());
            Map<String, Object> map = new HashMap<>(16);
            map.put("id",sku.getId());
            map.put("title",sku.getTitle());
            map.put("image",StringUtils.isBlank(sku.getImages()) ? "" : sku.getImages().split(",")[0]);
            map.put("price",sku.getPrice());
            skuList.add(map);
        });

//        处理通用属性
        Map<Long,String> genericMap = JsonUtils.parseMap(spuDetail.getGenericSpec(),Long.class,String.class);
//        处理特有属性
        Map<Long,List<String>> specialMap = JsonUtils.nativeRead(
                spuDetail.getSpecialSpec(),
                new TypeReference<Map<Long, List<String>>>() {
        });
        Map<String,Object> specs = new HashMap<>(16);
        genericParams.forEach(param -> {
            assert genericMap != null;
            String value = genericMap.get(param.getId());
            if (param.getNumeric()){
//                数值类型，需要存储一个分段
                value = this.chooseSegment(value,param);
            }
            specs.put(param.getName(),value);
        });
        specialParams.forEach(param -> {
            assert specialMap != null;
            specs.put(param.getName(),specialMap.get(param.getId()));
        });

        Goods goods = new Goods();
        goods.setId(spuId);
        goods.setAll(spuBo.getTitle() + StringUtils.join(nameByIds," "));
        goods.setSubTitle(spuBo.getSubTitle());
        goods.setBrandId(spuBo.getBrandId());
        goods.setCid1(spuBo.getCid1());
        goods.setCid2(spuBo.getCid2());
        goods.setCid3(spuBo.getCid3());
        goods.setCreateTime(spuBo.getCreateTime());
        goods.setPrice(prices);
        goods.setSkus(JsonUtils.serialize(skuList));
        goods.setSpecs(specs);

        return goods;
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }
}
