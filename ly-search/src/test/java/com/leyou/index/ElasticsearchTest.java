package com.leyou.index;

import com.leyou.search.LyEsApplication;
import com.leyou.search.bean.Goods;
import com.leyou.search.repository.GoodsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Srd
 * @date 2020/7/22  1:45
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LyEsApplication.class)
public class ElasticsearchTest {

    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Autowired
    private GoodsRepository goodsRepository;

    /**
     * 当Goods实体类的@Document注解参数 createIndex 为 false 时，需要手动创建索引
     * createIndex默认为true，当使用默认参数时，在程序启动时会自动创建索引
     */
    @Test
    public void createIndex() {
//        1. 设置索引信息，绑定实体类
        IndexOperations indexOperations = restTemplate.indexOps(Goods.class);
//        2. 创建索引
        indexOperations.create();
//        3. 创建索引映射
        Document mapping = indexOperations.createMapping();
//        4.写入索引
        indexOperations.putMapping(mapping);
    }

    @Test
    public void deleteIndex() {
        restTemplate.indexOps(Goods.class).delete();
    }
}
