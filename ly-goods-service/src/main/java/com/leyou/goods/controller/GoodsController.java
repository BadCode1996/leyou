package com.leyou.goods.controller;

import com.leyou.goods.service.FileService;
import com.leyou.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @author Srd
 * @date 2020/8/14  14:42
 */
@Controller
@RequestMapping("item")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private FileService fileService;

    /**
     * 跳转到商品详情页
     * @param model
     * @param id
     * @return
     */
    @GetMapping("{id}.html")
    public String toItemPage(Model model, @PathVariable("id") Long id){
        Map<String,Object> modelMap = this.goodsService.loadModel(id);
        model.addAllAttributes(modelMap);

        if(!this.fileService.exists(id)){
            this.fileService.syncCreateHtml(id);
        }

        return "item";
    }
}
