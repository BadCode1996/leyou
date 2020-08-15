package com.leyou.item.controller;

import com.leyou.item.bean.SpecGroup;
import com.leyou.item.bean.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Srd
 * @date 2020/5/21  3:09
 */
@RestController
@RequestMapping("spec")
public class SpecGroupController {

    @Autowired
    private SpecificationService specificationService;

    /**
     * http://api.leyou.com/api/item/spec/groups/77
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroups(@PathVariable("cid")Long cid){
        List<SpecGroup> list = this.specificationService.querySpecGroups(cid);
        if (list == null || list.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

    /**
     * 通用接口
     * http://api.leyou.com/api/item/spec/params?gid=3
     * http://api.leyou.com/api/item/spec/params?cid=324
     * @param gid
     * @param cid
     * @return
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> querySpecParam(
            @RequestParam(value = "gid",required = false)Long gid,
            @RequestParam(value = "cid",required = false)Long cid,
            @RequestParam(value = "searching",required = false)Boolean searching,
            @RequestParam(value = "generic",required = false)Boolean generic
    ){
        List<SpecParam> list = this.specificationService.querySpecParam(gid,cid,searching,generic);
        if (list == null || list.size() == 0){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(list);
    }

    /**
     * http://api.leyou.com/api/item/spec/group
     * @param cid
     * @param name
     * @return
     */
    @PostMapping("group")
    public ResponseEntity<Void> addSpecGroup(@RequestParam("cid")Long cid,@RequestParam("name")String name){
        this.specificationService.addSpecGroup(cid,name);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * http://api.leyou.com/api/item/spec/group
     * @param cid
     * @param name
     * @param id
     * @return
     */
    @PutMapping("group")
    public ResponseEntity<Void> editSpecGroup(@RequestParam("cid")Long cid, @RequestParam("name")String name,@RequestParam("id")Long id){
        this.specificationService.editSpecGroup(cid,name,id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 根据规格分组id删除规格分组
     * http://api.leyou.com/api/item/spec/group/15
     * @param id
     * @return
     */
    @DeleteMapping("group/{id}")
    public ResponseEntity<Void> deleteSpecGroup(@PathVariable("id") Long id){
        this.specificationService.deleteSpecGroup(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * http://api.leyou.com/api/item/spec/param
     * @param specParam
     * @return
     */
    @PostMapping("param")
    public ResponseEntity<Void> addSpecParam(@RequestBody()SpecParam specParam){
        this.specificationService.addSpecParam(specParam);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * http://api.leyou.com/api/item/spec/param/33
     * @param id
     * @return
     */
    @DeleteMapping("param/{id}")
    public ResponseEntity<Void> deleteSpecParam(@PathVariable("id") Long id){
        this.specificationService.deleteSpecParam(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 查询规格参数组，以及组内的所有规格参数
     * @param cid
     * @return
     */
    @GetMapping("{cid}")
    public  ResponseEntity<List<SpecGroup>> querySpecsByCid(@PathVariable("cid") Long cid){
        List<SpecGroup> groups = this.specificationService.querySpecsByCid(cid);
        if (groups == null || groups.size() == 0){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(groups);
    }
}
