package com.leyou.cart.controller;

import com.leyou.cart.bean.Cart;
import com.leyou.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Srd
 * @date 2020/8/27  16:23
 */
@RestController
public class CartController {


    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){
        this.cartService.addCart(cart);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Cart>> queryCarts(){
        List<Cart> carts = this.cartService.queryCarts();
        if (null == carts){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(carts);
    }

    @PutMapping
    public ResponseEntity<Void> updateNum(@RequestParam("skuId") Long skuId,@RequestParam("num") Integer num){
        this.cartService.updateNum(skuId,num);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId") Long skuId){
        this.cartService.deleteCart(skuId);
        return ResponseEntity.ok().build();
    }
}
