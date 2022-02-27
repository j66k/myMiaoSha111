package com.imooc.miaosha.dao;

import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.OrderInfo;
import org.apache.ibatis.annotations.*;

/**
 * @Package: com.imooc.miaosha.dao
 * @ClassName: OrderDao
 * @Author: jjt
 * @CreateTime: 2022/2/27 21:00
 * @Description:
 */
@Mapper
public interface OrderDao {

    //按照用户id和商品的id来查询秒杀的订单
    @Select("select *from miaosha_order where user_id=#{userId} and goods_id=#{goodsId}")
   public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

    //把正常的订单插入

    //第二个注解是把最后插入的id返回
    @Insert("insert into order_info(user_id,goods_id,goods_name,goods_count,goods_price,order_channel,status,create_date)" +
            "values(#{userId},#{goodsId},#{goodsName},#{goodsCount},#{goodsPrice},#{orderChannel},#{status},#{createDate}) ")
    @SelectKey(keyColumn = "id",keyProperty = "id",resultType = long.class,before = false,statement = "select last_insert_id()")
   public long insertOrder(OrderInfo orderInfo);

    //插入秒杀订单

    @Insert("insert into miaosha_order(user_id,order_id,goods_id)values(#{userId},#{orderId},#{goodsId}) ")
   public void insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);
}
