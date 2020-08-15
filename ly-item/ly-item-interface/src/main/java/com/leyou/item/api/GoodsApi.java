package com.leyou.item.api;

import com.leyou.common.bean.PageResult;
import com.leyou.item.bean.Sku;
import com.leyou.item.bean.Spu;
import com.leyou.item.bean.SpuDetail;
import com.leyou.item.bo.SpuBo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Srd
 * @date 2020/7/22  15:56
 */
@RequestMapping
public interface GoodsApi {

    /**
     * http://api.leyou.com/api/item/spu/page?key=&saleable=true&page=1&rows=5
     *
     * @param key      搜索条件
     * @param saleable 是否上下架
     * @param page     当前页
     * @param rows     页容量
     * @return
     */
    @GetMapping("spu/page")
    PageResult<SpuBo> querySpuByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows
    );

    /**
     * 新增商品
     * http://api.leyou.com/api/item/goods
     * @param spu
     */
    @PostMapping("goods")
    void addGoods(@RequestBody SpuBo spu);

    /**
     * 修改商品
     * http://api.leyou.com/api/item/goods
     * put请求
     * @param spu
     */
    @PutMapping("goods")
    void editGoods(@RequestBody SpuBo spu);

    /**
     * 修改商品，根据id回显商品内容
     * http://api.leyou.com/api/item/spu/detail/2
     * @param id
     * @return
     */
    @GetMapping("spu/detail/{id}")
    SpuDetail querySpuDetailBySpuId(@PathVariable("id")Long id);

    /**
     * 修改商品，根据spu_id获取sku，回显数据
     * http://api.leyou.com/api/item/sku/list?id=2
     * @param id
     * @return
     */
    @GetMapping("sku/list")
    List<Sku> querySkuBySpuId(@RequestParam("id")Long id);

    /**
     * 对商品进行上下架操作
     * http://api.leyou.com/api/item/spu/saleable/2
     * @param id
     * @return
     */
    @PutMapping("spu/saleable/{id}")
    void changeSaleable(@PathVariable Long id);

    /**
     * 根据spu的id查询spu
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    public Spu querySpuById(@PathVariable("id") Long id);
}
