package com.imooc.miaosha.dao;

import com.imooc.miaosha.domain.Goods;
import com.imooc.miaosha.domain.MiaoshaGoods;
import com.imooc.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    //修改库存信息
    @Update("update miaosha_goods set stock_count=stock_count-1 where goods_id=#{goodsId} and stock_count >0")//从对象中取出名称为goodsId的值
    void reduceStock(MiaoshaGoods g);
}
