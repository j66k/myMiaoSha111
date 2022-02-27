package com.imooc.miaosha.dao;

import com.imooc.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Package: com.imooc.miaosha.dao
 * @ClassName: GoodsDao
 * @Author: jjt
 * @CreateTime: 2022/2/27 15:05
 * @Description:
 */
@Mapper
public interface GoodsDao {
    //实现查出一个列表出来
    @Select("select g.* ,mg.stock_Count,mg.start_Date,mg.end_Date ,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id=g.id")
    public List<GoodsVo> listGoodsVo();

    //按照id查询一个商品
//    @Select("select g.* ,mg.stock_Count,mg.start_Date,mg.end_Date ,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id=g.id where g.id =#{goodsId} ")
//    public GoodsVo getGoodsVoByGoodsId(@Param("goodsId")long goodsId);

    @Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id where g.id = #{goodsId}")
    public GoodsVo getGoodsVoByGoodsId(@Param("goodsId")long goodsId);
}
