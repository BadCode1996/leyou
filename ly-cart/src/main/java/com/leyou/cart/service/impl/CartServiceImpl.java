package com.leyou.cart.service.impl;

import com.leyou.auth.bean.UserInfo;
import com.leyou.cart.bean.Cart;
import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.service.CartService;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.bean.Sku;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Srd
 * @date 2020/8/27  16:27
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private GoodsClient goodsClient;

    static final String KEY_PREFIX = "ly:cart:uid:";

    static final Logger LOGGER = LoggerFactory.getLogger(CartService.class);

    /**
     * 添加购物车
     *
     * @param cart
     */
    @Override
    public void addCart(Cart cart) {
//        获取登录用户
        UserInfo loginUser = LoginInterceptor.getLoginUser();
//        Redis的key
        String key = KEY_PREFIX + loginUser.getId();
//        获取hash操作对象
        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(key);

        Long skuId = cart.getSkuId();
        Integer num = cart.getNum();
        Boolean hasKey = ops.hasKey(skuId.toString());
        if (hasKey) {
//            Redis中存在key，获取购物车数据
            String json = ops.get(skuId.toString()).toString();
            cart = JsonUtils.parse(json, Cart.class);
//            修改购物车内商品数量
            cart.setNum(cart.getNum() + num);
        }else {
//            redis中不存在，新增购物车数据
            cart.setUserId(loginUser.getId());
            Sku sku = this.goodsClient.querySkuById(skuId);
            if (null == sku){
                LOGGER.error("添加购物车的商品不存在：skuId:{}", skuId);
                throw new RuntimeException();
            }
            cart.setSkuId(sku.getId());
            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "":StringUtils.split(sku.getImages(),",")[0]);
            cart.setPrice(sku.getPrice());
            cart.setTitle(sku.getTitle());
            cart.setOwnSpec(sku.getOwnSpec());
        }
//        将购物车数据写入redis
        ops.put(cart.getSkuId().toString(),JsonUtils.serialize(cart));
    }

    /**
     * 查询carts
     *
     * @return
     */
    @Override
    public List<Cart> queryCarts() {
//        获取登录用户
        UserInfo loginUser = LoginInterceptor.getLoginUser();
//        判断是否存在购物车
        String key = KEY_PREFIX + loginUser.getId();
        if (!redisTemplate.hasKey(key)){
//            不存在，直接返回
            return null;
        }
        BoundHashOperations<String, Object, Object> ops = this.redisTemplate.boundHashOps(key);
        List<Object> carts = ops.values();
//        判断是否有数据
        if (CollectionUtils.isEmpty(carts)){
            return null;
        }
        return carts.stream().map(o -> JsonUtils.parse(o.toString(),Cart.class)).collect(Collectors.toList());
    }

    /**
     * 更新购物车内商品数量
     *
     * @param skuId
     * @param num
     */
    @Override
    public void updateNum(Long skuId, Integer num) {
//        获取登录用户
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        String key = KEY_PREFIX + loginUser.getId();
        BoundHashOperations<String, Object, Object> ops = this.redisTemplate.boundHashOps(key);
//        获取购物车
        String json = ops.get(skuId.toString()).toString();
        Cart cart = JsonUtils.parse(json, Cart.class);
        cart.setNum(num);
//        重新写入Redis
        ops.put(skuId.toString(),JsonUtils.serialize(cart));
    }

    /**
     * 删除Cart
     *
     * @param skuId
     */
    @Override
    public void deleteCart(Long skuId) {
//        获取登录用户
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        String key = KEY_PREFIX + loginUser.getId();
        BoundHashOperations<String, Object, Object> ops = this.redisTemplate.boundHashOps(key);
//        删除redis内的cart
        ops.delete(skuId.toString());
    }
}
