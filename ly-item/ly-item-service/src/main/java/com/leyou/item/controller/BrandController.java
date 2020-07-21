package com.leyou.item.controller;

import com.leyou.common.bean.PageResult;
import com.leyou.item.bean.Brand;
import com.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Srd
 * @date 2020/5/19  15:35
 */
@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 分页查询品牌
     * @param page 当前页
     * @param rows 页容量
     * @param desc 是否降序
     * @param key 搜索关键字
     * @param sortBy 排序字段
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "desc",defaultValue = "false") Boolean desc,
            @RequestParam(value = "key",required = false)String key,
            @RequestParam(value = "sortBy",required = false)String sortBy
    ){
        PageResult<Brand> pageResult = this.brandService.queryBrandByPage(page,rows,desc,key,sortBy);
        if (pageResult == null || pageResult.getItems().size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(pageResult);
    }

    /**
     * 新增品牌
     * http://api.leyou.com/api/item/brand
     *  post请求
     *  参数：brand对象，cids
     * @param brand
     * @param cids
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addBrand(Brand brand, @RequestParam("cids") List<Long> cids){
        this.brandService.addBrand(brand,cids);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 修改品牌
     * @param brand
     * @param cids
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> editBrand(Brand brand, @RequestParam("cids") List<Long> cids){
        this.brandService.editBrand(brand,cids);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * http://api.leyou.com/api/item/brand/324
     * 根据品牌id删除品牌
     * @param bid
     * @return
     */
    @DeleteMapping("{bid}")
    public ResponseEntity<Void> deleteBrand(@PathVariable("bid") Long bid){
        this.brandService.deleteBrand(bid);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * http://api.leyou.com/api/item/brand/cid/324
     * @param cid
     * @return
     */
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCid(@PathVariable("cid")Long cid){
        List<Brand> list = this.brandService.queryBrandByCid(cid);
        if (list == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(list);
    }


}
