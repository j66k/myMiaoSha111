package com.imooc.miaosha.service;

import com.imooc.miaosha.dao.OrderDao;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.redis.OrderKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Package: com.imooc.miaosha.service
 * @ClassName: OderService
 * @Author: jjt
 * @CreateTime: 2022/2/27 20:30
 * @Description:
 */

@Service
public class OrderService {
@Autowired
OrderDao orderDao;
@Autowired
RedisService redisService;
    //通过用户id以及商品id来查询秒杀的订单
    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(Long userId, long goodsId) {
    //这里优化一下，查看订单的时候不去数据库查，而是直接去查缓存
        //return orderDao.getMiaoshaOrderByUserIdGoodsId(userId,goodsId);
       return redisService.get(OrderKey.getMiaoshaOrderByUidGid,""+userId+"_"+goodsId,MiaoshaOrder.class);
    }


    //通过订单的id来获取普通订单
    public OrderInfo getOrderById(long orderId) {

        return orderDao.getOrderById(orderId);
    }

    //生成秒杀订单
    @Transactional//事务
    public OrderInfo createOrder(MiaoshaUser user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();//生成一个订单对象
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);//渠道
        orderInfo.setStatus(0);//0表示新建订单，未支付
        orderInfo.setUserId(user.getId());

        //调用dao中的方法，把这个对象插入
       long orderId =  orderDao.insertOrder(orderInfo);//返回订单id

        //创建完正常的订单，再创建秒杀的订单
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setOrderId(orderId);
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrder.setUserId(user.getId());

        //调用dao中插入秒杀订单的方法
        orderDao.insertMiaoshaOrder(miaoshaOrder);
        //把订单写入到redis中
        redisService.set(OrderKey.getMiaoshaOrderByUidGid,""+user.getId()+"_"+goods.getId(),miaoshaOrder);//把miaosahorder这个对象放进去
        return orderInfo;
    }


}
