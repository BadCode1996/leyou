package com.leyou.item.controller;

import com.leyou.common.bean.PageResult;
import com.leyou.item.bean.Sku;
import com.leyou.item.bean.Spu;
import com.leyou.item.bean.SpuDetail;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Srd
 * @date 2020/5/25  16:16
 */
@RestController
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

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
    public ResponseEntity<PageResult<SpuBo>> querySpuByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows
    ) {
        PageResult<SpuBo> result = this.goodsService.querySpuByPage(key, saleable, page, rows);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 新增商品
     * http://api.leyou.com/api/item/goods
     * @param spu
     * @return
     */
    @PostMapping("goods")
    public ResponseEntity<Void> addGoods(@RequestBody SpuBo spu){
        try {
            this.goodsService.save(spu);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//

    /**
     * 修改商品
     * http://api.leyou.com/api/item/goods
     * put请求
     * @param spu
     * @return
     */
    @PutMapping("goods")
    public ResponseEntity<Void> editGoods(@RequestBody SpuBo spu){
        try {
            this.goodsService.edit(spu);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 修改商品，根据id回显商品内容
     * http://api.leyou.com/api/item/spu/detail/2
     * @param id
     * @return
     */
    @GetMapping("spu/detail/{id}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("id")Long id){

        SpuDetail spuDetail = this.goodsService.querySpuDetailBySpuId(id);
        if (spuDetail == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(spuDetail);
    }

    /**
     * 修改商品，根据spu_id获取sku，回显数据
     * http://api.leyou.com/api/item/sku/list?id=2
     * @param id
     * @return
     */
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id")Long id){
        List<Sku> list = this.goodsService.querySkuBySpuId(id);
        if (list == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(list);
    }

    /**
     * 对商品进行上下架操作
     * http://api.leyou.com/api/item/spu/saleable/2
     * @param id
     * @return
     */
    @PutMapping("spu/saleable/{id}")
    public ResponseEntity<Void> changeSaleable(@PathVariable Long id){
        this.goodsService.changeSaleable(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
