package com.leyou.item.bean;

import javax.persistence.*;
import java.util.List;

/**
 * @author Srd
 */
@Table(name = "tb_spec_group")
public class SpecGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cid;

    private String name;

    /**
     * 添加一个SpecParam集合，保存SpecGroup组下的所有规格参数
     */
    @Transient
    private List<SpecParam> specParam;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SpecParam> getSpecParam() {
        return specParam;
    }

    public void setSpecParam(List<SpecParam> specParam) {
        this.specParam = specParam;
    }

    @Override
    public String toString() {
        return "SpecGroup{" +
                "id=" + id +
                ", cid=" + cid +
                ", name='" + name + '\'' +
                ", specParam=" + specParam +
                '}';
    }
}