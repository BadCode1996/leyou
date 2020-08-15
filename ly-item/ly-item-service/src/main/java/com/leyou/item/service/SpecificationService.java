package com.leyou.item.service;

import com.leyou.item.bean.SpecGroup;
import com.leyou.item.bean.SpecParam;

import java.util.List;

/**
 * @author Srd
 * @date 2020/5/21  3:12
 */
public interface SpecificationService {

    /**
     * 根据cid查询规格参数分组表
     * @param cid 商品分类id
     * @return
     */
    List<SpecGroup> querySpecGroups(Long cid);

    /**
     * 根据gid查询规格参数组下的参数名
     * @param gid 规格参数分组id
     * @param cid 分类id
     * @param searching
     * @param generic
     * @return
     */
    List<SpecParam> querySpecParam(Long gid,Long cid,Boolean searching,Boolean generic);

    /**
     * 添加规格组
     * @param cid
     * @param name
     */
    void addSpecGroup(Long cid, String name);

    /**
     *修改规格组
     * @param cid
     * @param name
     * @param id
     */
    void editSpecGroup(Long cid,String name, Long id);

    /**
     * 根据规格分组id删除规格分组
     * @param id
     */
    void deleteSpecGroup(Long id);

    /**
     * 新增规格参数
     * @param specParam
     */
    void addSpecParam(SpecParam specParam);

    /**
     * 根据规格参数id删除规格参数
     * @param id
     */
    void deleteSpecParam(Long id);

    /**
     * 查询规格参数组，以及组内的所有规格参数
     * @param cid
     * @return
     */
    List<SpecGroup> querySpecsByCid(Long cid);
}
