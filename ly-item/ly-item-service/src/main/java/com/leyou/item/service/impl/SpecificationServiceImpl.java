package com.leyou.item.service.impl;

import com.leyou.item.bean.SpecGroup;
import com.leyou.item.bean.SpecParam;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Srd
 * @date 2020/5/21  3:17
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    /**
     * 根据cid查询规格参数分组表
     * @param cid 商品分类id
     * @return
     */
    @Override
    public List<SpecGroup> querySpecGroups(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        return this.specGroupMapper.select(specGroup);
    }

    /**
     * 根据gid查询规格参数组下的参数名
     * @param gid 规格参数分组id
     * @param gid 分类id
     * @return
     */
    @Override
    public List<SpecParam> querySpecParam(Long gid,Long cid,Boolean searching,Boolean generic) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        specParam.setGeneric(generic);
        return this.specParamMapper.select(specParam);
    }

    /**
     * 添加规格组
     *
     * @param cid
     * @param name
     */
    @Override
    public void addSpecGroup(Long cid, String name) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        specGroup.setName(name);
        this.specGroupMapper.insert(specGroup);
    }

    /**
     * 修改规格组
     * @param cid
     * @param name
     * @param id
     */
    @Override
    public void editSpecGroup(Long cid,String name, Long id) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setId(id);
        specGroup.setCid(cid);
        specGroup.setName(name);
        this.specGroupMapper.updateByPrimaryKey(specGroup);
    }

    /**
     * 根据规格分组id删除规格分组
     *
     * @param id
     */
    @Override
    public void deleteSpecGroup(Long id) {
        this.specGroupMapper.deleteByPrimaryKey(id);
    }

    /**
     * 新增规格参数
     * @param specParam
     */
    @Override
    public void addSpecParam(SpecParam specParam) {
        this.specParamMapper.insert(specParam);
    }

    /**
     * 根据规格参数id删除规格参数
     * @param id
     */
    @Override
    public void deleteSpecParam(Long id) {
        this.specParamMapper.deleteByPrimaryKey(id);
    }

    /**
     * 查询规格参数组，以及组内的所有规格参数
     * 把querySpecGroups() 和 querySpecParam()的结果进行封装
     * @param cid
     * @return
     */
    @Override
    public List<SpecGroup> querySpecsByCid(Long cid) {
//        查询规格参数组
        List<SpecGroup> groups = this.querySpecGroups(cid);
        groups.forEach(group -> {
            group.setSpecParam(this.querySpecParam(group.getId(),null,null,null));
        });
        return groups;
    }
}
