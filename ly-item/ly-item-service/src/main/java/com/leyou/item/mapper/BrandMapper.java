package com.leyou.item.mapper;

import com.leyou.item.bean.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Srd
 * @date 2020/5/19  16:49
 */
public interface BrandMapper extends Mapper<Brand> {

    /**
     * 新增品牌和商品分类中间表
     * @param cid 商品分类id,category_id
     * @param bid 品牌id,brand_id
     */
    @Insert("insert into tb_category_brand (category_id,brand_id) values (#{cid},#{bid})")
    void insertCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    /**
     * 根据brand_id删除品牌和商品分类中间表
     * @param bid
     */
    @Delete("delete from tb_category_brand where brand_id = #{bid}")
    void deleteCateGoryBrandByBid(@Param("bid") Long bid);

    /**
     * 根据category_id查询品牌和商品分类中间表
     * @param cid
     * @return
     */
    @Select("select b.* from tb_brand b left join tb_category_brand cb on b.id = cb.brand_id where cb.category_id = #{cid}")
    List<Brand> selectCateGoryBrandByCid(@Param("cid") Long cid);
}
