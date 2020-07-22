package com.leyou.search.repository;

import com.leyou.search.bean.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 创建GoodsRepository
 * @author Srd
 * @date 2020/7/22  1:44
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
