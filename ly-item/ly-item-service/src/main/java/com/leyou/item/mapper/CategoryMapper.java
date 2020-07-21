package com.leyou.item.mapper;

import com.leyou.item.bean.Category;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Srd
 * @date 2020/5/19  2:44
 */
public interface CategoryMapper extends Mapper<Category> , SelectByIdListMapper<Category,Long> {

    /**
     * 先根据品牌id在tb_category_brand表中，查出category_id，
     *      select category_id from tb_category_brand where brand_id = #{bid}
     * 再用category_id在tb_category表中查询分类
     *      select * from tb_category where id in ()
     * @param bid
     * @return
     */
    @Select("select * from tb_category where id in (select category_id from tb_category_brand where brand_id = #{bid})")
    List<Category> queryByBrandId(@Param("bid") Long bid);
}
