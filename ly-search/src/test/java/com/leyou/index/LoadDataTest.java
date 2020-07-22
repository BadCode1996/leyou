package com.leyou.index;

import com.leyou.common.bean.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.search.LyEsApplication;
import com.leyou.search.bean.Goods;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.IndexService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Srd
 * @date 2020/7/23  1:21
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LyEsApplication.class)
public class LoadDataTest {

    @Autowired
    private IndexService indexService;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Test
    public void loadData() {
        int page = 1;
        int rows = 100;
        int size = 0;
        do {
            PageResult<SpuBo> result = this.goodsClient.querySpuByPage(null, true, page, rows);
            List<SpuBo> spuBos = result.getItems();
//            spuBos转goods
            List<Goods> goods = spuBos.stream().map(spuBo ->
                    this.indexService.buildGoods(spuBo))
                    .collect(Collectors.toList());
            this.goodsRepository.saveAll(goods);
            size = spuBos.size();
            page++;
        } while (size == 100);
    }
}
