package com.leyou.goods.service;

/**
 * @author Srd
 * @date 2020/8/17  0:44
 */
public interface FileService {

    /**
     * 创建静态页面
     * @param id
     * @throws Exception
     */
    void createHtml(Long id) throws Exception;

    /**
     * 判断某个商品的页面是否存在
     * @param id
     * @return
     */
    boolean exists(Long id);

    /**
     * 判断某个商品的页面是否存在
     * @param id
     */
    void syncCreateHtml(Long id);

    /**
     * 删除静态页面
     * @param id
     */
    void deleteHtml(Long id);
}
