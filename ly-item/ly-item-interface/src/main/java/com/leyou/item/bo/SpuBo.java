package com.leyou.item.bo;

import com.leyou.item.bean.Sku;
import com.leyou.item.bean.Spu;
import com.leyou.item.bean.SpuDetail;

import javax.persistence.Transient;
import java.util.List;


/**
 * @author Srd
 * @date 2020/5/25  14:47
 */
public class SpuBo extends Spu {
    private String cname;
    private String bname;

    @Transient
    SpuDetail spuDetail;// 商品详情
    @Transient
    List<Sku> skus;// sku列表

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public SpuDetail getSpuDetail() {
        return spuDetail;
    }

    public void setSpuDetail(SpuDetail spuDetail) {
        this.spuDetail = spuDetail;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }
}
